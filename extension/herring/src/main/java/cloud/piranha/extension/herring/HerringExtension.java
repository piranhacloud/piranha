/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.extension.herring;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;

import com.manorrock.herring.DefaultInitialContext;
import com.manorrock.herring.thread.ThreadInitialContextFactory;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import jakarta.annotation.Resource;

/**
 * The WebApplicationExtension that is responsible for setting up the proper
 * Context instance so it can be made available during web application
 * initialization and subsequently during request processing as well as
 * delivering listeners to set/remove the Context from the current thread.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @deprecated 
 */
@Deprecated(forRemoval = true, since = "21.9.0")
public class HerringExtension implements WebApplicationExtension {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(
            HerringExtension.class.getName());

    /**
     * Configure the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        LOGGER.log(DEBUG, "Configuring JNDI support by means of Manorrock Herring");
        if (System.getProperty(INITIAL_CONTEXT_FACTORY) == null) {
            LOGGER.log(INFO, INITIAL_CONTEXT_FACTORY + " was not set, setting it");
            System.setProperty(INITIAL_CONTEXT_FACTORY, ThreadInitialContextFactory.class.getName());
        }
        if (!System.getProperty(INITIAL_CONTEXT_FACTORY).equals(ThreadInitialContextFactory.class.getName())) {
            LOGGER.log(WARNING, INITIAL_CONTEXT_FACTORY + " is not set to " + ThreadInitialContextFactory.class.getName());
        }
        Context context = new DefaultInitialContext();

        Context proxyContext = (Context) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[] { Context.class },
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    try {
                        Object returnValue = null;
                        try {
                            returnValue = method.invoke(context, args);
                        } catch (InvocationTargetException e) {
                            boolean invoked = false;
                            try {
                                if (method.getName().equals("lookup") && e.getCause() instanceof NamingException) {
                                    String jndiName = args[0].toString();

                                    if (jndiName.startsWith("java:comp/env/")) {
                                        String classNameWithField = jndiName.substring("java:comp/env/".length());
                                        String[] classNameAndField =  classNameWithField.split("/");
                                        if (classNameAndField.length == 2) {
                                            String className = classNameAndField[0];
                                            String fieldName = classNameAndField[1];

                                            Class<?> beanClass = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                                            Field beanField = beanClass.getDeclaredField(fieldName);

                                            Resource[] resources = beanField.getAnnotationsByType(Resource.class);
                                            if (resources != null && resources.length > 0) {
                                                Resource resourceAnnnotation = resources[0];

                                                String lookup = resourceAnnnotation.lookup();
                                                if (!"".equals(lookup)) {
                                                    returnValue = method.invoke(context, new Object[] {lookup});
                                                    args = new Object[] {lookup};
                                                    invoked = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Throwable t) {
                                if (t instanceof InvocationTargetException invocationException &&
                                    invocationException.getTargetException() instanceof NamingException namingException) {
                                   throw namingException;
                                }
                            }

                            if (!invoked) {
                                throw e;
                            }
                        }

                        if (method.getName().equals("lookup") && returnValue instanceof Reference) {
                            returnValue = NamingManager.getObjectInstance(
                                returnValue, new CompositeName(args[0].toString()), null, null);
                        }

                        return returnValue;
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }
            });

        webApplication.setAttribute(Context.class.getName(), proxyContext);
        ThreadInitialContextFactory.setInitialContext(proxyContext);
        webApplication.addListener(HerringServletRequestListener.class.getName());
    }
}

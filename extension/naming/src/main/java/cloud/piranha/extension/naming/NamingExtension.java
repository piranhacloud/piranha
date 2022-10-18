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
package cloud.piranha.extension.naming;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Optional;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.naming.impl.DefaultInitialContext;
import jakarta.annotation.Resource;
import jakarta.enterprise.inject.spi.CDI;

/**
 * The WebApplicationExtension that is responsible for setting up the proper
 * Context instance so it can be made available during web application
 * initialization and subsequently during request processing as well as
 * delivering listeners to set/remove the Context from the current thread.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NamingExtension implements WebApplicationExtension {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(NamingExtension.class.getName());

    /**
     * Configure the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        LOGGER.log(DEBUG, "Configuring JNDI support");
        if (System.getProperty(INITIAL_CONTEXT_FACTORY) == null) {
            LOGGER.log(INFO, INITIAL_CONTEXT_FACTORY + " was not set, setting it");
            System.setProperty(INITIAL_CONTEXT_FACTORY, DefaultInitialContextFactory.class.getName());
        }
        if (!System.getProperty(INITIAL_CONTEXT_FACTORY).equals(DefaultInitialContextFactory.class.getName())) {
            LOGGER.log(WARNING, INITIAL_CONTEXT_FACTORY + " is not set to " + DefaultInitialContextFactory.class.getName());
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

                                            Resource[] resources = null;
                                            Class<?> type = null;

                                            try {
                                                Field beanField = beanClass.getDeclaredField(fieldName);
                                                resources = beanField.getAnnotationsByType(Resource.class);
                                                type = beanField.getType();
                                            } catch (NoSuchFieldException | SecurityException exception) {
                                                char chars[] = fieldName.toCharArray();
                                                chars[0] = Character.toUpperCase(chars[0]);
                                                String methodName = "set" + new String(chars);

                                                Optional<Method> optionalMethod = Arrays.stream(beanClass.getDeclaredMethods())
                                                      .filter(m -> m.getName().equals(methodName))
                                                      .filter(m -> m.getParameterCount() == 1)
                                                      .filter(m -> m.getAnnotationsByType(Resource.class) != null)
                                                      .findFirst(); // ignore overloaded for now

                                                if (optionalMethod.isPresent()) {
                                                    resources = optionalMethod.get().getAnnotationsByType(Resource.class);
                                                    type = optionalMethod.get().getParameterTypes()[0];
                                                }
                                            }

                                            if (resources != null && resources.length > 0) {
                                                Resource resourceAnnnotation = resources[0];

                                                String lookup = resourceAnnnotation.lookup();
                                                if (!"".equals(lookup)) {
                                                    returnValue = method.invoke(context, new Object[] {lookup});
                                                    args = new Object[] {lookup};
                                                    invoked = true;
                                                } else {
                                                    return CDI.current().select(type).get();
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
                                e.addSuppressed(t);
                            }

                            if (!invoked) {
                                throw e;
                            }
                        }

                        // De-referencing can eventually be moved to DefaultInitialContext
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

        DefaultInitialContextFactory.setInitialContext(proxyContext);
        webApplication.setAttribute(Context.class.getName(), proxyContext);
    }
}

/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.weld;

import cloud.piranha.core.api.ObjectInstanceManager;
import java.util.EventListener;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.inject.spi.Unmanaged;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The Weld variant of the ObjectInstanceManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WeldObjectInstanceManager implements ObjectInstanceManager {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(WeldObjectInstanceManager.class.getName());

    @Override
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException {
        T result = null;
        boolean constructed = false;
        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, filterClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Exception exception) {
            LOGGER.log(WARNING, "Unable to create Filter using CDI", exception);
        }
        if (!constructed) {
            try {
                result = filterClass.getDeclaredConstructor().newInstance();
            } catch (Exception exception) {
                throw new ServletException("Unable to create Listener using new", exception);
            }
        }
        return result;
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        T result = null;
        boolean constructed = false;
        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, clazz);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Exception exception) {
            LOGGER.log(WARNING, "Unable to create Listener using CDI", exception);
        }
        if (!constructed) {
            try {
                result = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception exception) {
                throw new ServletException("Unable to create Listener using new", exception);
            }
        }
        return result;
    }
    
    @Override
    public <T extends Servlet> T createServlet(Class<T> servletClass) throws ServletException {
        T result = null;
        boolean constructed = false;
        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, servletClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Exception exception) {
            LOGGER.log(WARNING, "Unable to create Servlet using CDI", exception);
        }
        if (!constructed) {
            try {
                result = servletClass.getDeclaredConstructor().newInstance();
            } catch (Exception exception) {
                throw new ServletException("Unable to create Servlet using new", exception);
            }
        }
        return result;
    }
}

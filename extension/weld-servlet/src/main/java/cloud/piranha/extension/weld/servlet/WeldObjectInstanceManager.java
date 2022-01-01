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
package cloud.piranha.extension.weld.servlet;

import cloud.piranha.core.api.ObjectInstanceManager;
import jakarta.enterprise.inject.spi.BeanManager;
import java.util.EventListener;
import jakarta.enterprise.inject.spi.Unmanaged;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import static java.lang.System.Logger.Level.DEBUG;;

/**
 * The Weld variant of the ObjectInstanceManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WeldObjectInstanceManager implements ObjectInstanceManager {
    
   /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(WeldObjectInstanceManager.class.getName());

    /**
     * Stores the BeanManager.
     */
    private BeanManager beanManager;

    @Override
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException {
        LOGGER.log(DEBUG, "Creating filter: {0}", filterClass);
        T result = null;
        boolean constructed = false;
        try {
            Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, filterClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }
        if (!constructed) {
            try {
                result = filterClass.getDeclaredConstructor().newInstance();
            } catch (Throwable throwable) {
                throw new ServletException(throwable);
            }
        }
        return result;
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> listenerClass) throws ServletException {
        LOGGER.log(DEBUG, "Creating listener: {0}", listenerClass);
        T result = null;
        boolean constructed = false;
        try {
            Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, listenerClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }
        if (!constructed) {
            try {
                result = listenerClass.getDeclaredConstructor().newInstance();
            } catch (Throwable throwable) {
                throw new ServletException(throwable);
            }
        }
        return result;
    }
    
    @Override
    public <T extends Servlet> T createServlet(Class<T> servletClass) throws ServletException {
        LOGGER.log(DEBUG, "Creating servlet: {0}", servletClass);
        T result = null;
        boolean constructed = false;
        try {
            Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, servletClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }
        if (!constructed) {
            try {
                result = servletClass.getDeclaredConstructor().newInstance();
            } catch (Throwable throwable) {
                throw new ServletException(throwable);
            }
        }
        return result;
    }

    /**
     * Set the BeanManager.
     * 
     * @param beanManager the BeanManager.
     */
    public void setManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }
}

/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.openwebbeans;

import com.manorrock.piranha.api.ObjectInstanceManager;
import java.util.EventListener;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Unmanaged;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * The OpenWebBeans object instance manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class OpenWebBeansObjectInstanceManager implements ObjectInstanceManager {

    /**
     * Create the Filter.
     *
     * @param <T> the return type.
     * @param filterClass the Filter class.
     * @return the Filter.
     * @throws ServletException when it fails to create the filter.
     */
    @Override
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException {
        T result = null;
        boolean constructed = false;
        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged(beanManager, filterClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }
        if (!constructed) {
            try {
                result = filterClass.newInstance();
            } catch (Throwable throwable) {
                throw new ServletException(throwable);
            }
        }
        return result;
    }

    /**
     * Create the listener.
     * 
     * @param <T> the type.
     * @param clazz the class.
     * @return the Listener.
     * @throws ServletException when it fails to create the listener.
     */
    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        T result = null;
        boolean constructed = false;
        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged(beanManager, clazz);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }
        if (!constructed) {
            try {
                result = clazz.newInstance();
            } catch (Throwable throwable) {
                throw new ServletException(throwable);
            }
        }
        return result;
    }
    
    /**
     * Create the Servlet.
     *
     * @param <T> the type.
     * @param servletClass the Servlet class.
     * @return the Servlet.
     * @throws ServletException when it fails to create the servlet.
     */
    @Override
    public <T extends Servlet> T createServlet(Class<T> servletClass) throws ServletException {
        T result = null;
        boolean constructed = false;
        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged(beanManager, servletClass);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }
        if (!constructed) {
            try {
                result = servletClass.newInstance();
            } catch (Throwable throwable) {
                throw new ServletException(throwable);
            }
        }
        return result;
    }
}

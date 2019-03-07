/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import com.manorrock.piranha.api.ObjectInstanceManager;
import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * The default object instance manager.
 *
 * <p>
 * This object instance manager does not do any injection and that is by design.
 * If you need injection into Servlets use the appropriate object instance
 * manager for your injection framework.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultObjectInstanceManager implements ObjectInstanceManager {

    /**
     * Create the filter.
     *
     * @param <T> the return type.
     * @param filterClass the filter class.
     * @return the filter.
     * @throws ServletException when a Filter error occurs.
     */
    @Override
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException {
        T result = null;
        try {
            result = filterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ServletException(exception);
        }
        return result;
    }
    
    /**
     * Create the listener.
     *
     * @param <T> the type.
     * @param clazz the class of the listener to create.
     * @return the listener.
     * @throws ServletException when it fails to create the listener.
     */
    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        T result;
        try {
            result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ServletException(exception);
        }
        return result;
    }

    /**
     * Create the servlet.
     *
     * @param <T> the return type.
     * @param servletClass the servlet class.
     * @return the servlet.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends Servlet> T createServlet(Class<T> servletClass) throws ServletException {
        T result;
        try {
            result = servletClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ServletException(exception);
        }
        return result;
    }
}

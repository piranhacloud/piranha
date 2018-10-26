/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.api;

import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * The object instance manager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ObjectInstanceManager {
    
    /**
     * Create the filter.
     * 
     * @param <T> the type.
     * @param filterClass the Filter class.
     * @return the Filter.
     * @throws ServletException when a Servlet error occurs.
     */
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException;

    /**
     * Create the listener.
     * 
     * @param <T> the type.
     * @param clazz the class.
     * @return the Listener.
     * @throws ServletException 
     */
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException;

    /**
     * Create the servlet.
     *
     * @param <T> the type.
     * @param clazz the Servlet class.
     * @return the Servlet.
     * @throws ServletException when a Servlet error occurs.
     */
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException;
}

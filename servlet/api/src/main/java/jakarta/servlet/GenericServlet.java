/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package jakarta.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

/**
 * The GenericServlet API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class GenericServlet implements Servlet, ServletConfig, Serializable {

    /**
     * Stores the servlet config.
     */
    private ServletConfig servletConfig;

    /**
     * Constructor.
     */
    public GenericServlet() {
    }

    /**
     * Destroy the servlet.
     */
    @Override
    public void destroy() {
    }

    /**
     * Get the init parameter.
     *
     * @param name the name.
     * @return the value, or null.
     *
     */
    @Override
    public String getInitParameter(String name) {
        return servletConfig.getInitParameter(name);
    }

    /**
     * Get the init parameter names.
     *
     * @return the init parameter names.
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return servletConfig.getInitParameterNames();
    }

    /**
     * Get the servlet config.
     *
     * @return the servlet config.
     */
    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    @Override
    public ServletContext getServletContext() {
        return servletConfig.getServletContext();
    }

    /**
     * Get the servlet info.
     *
     * @return ""
     */
    @Override
    public String getServletInfo() {
        return "";
    }

    /**
     * Get the servlet name.
     *
     * @return the servlet name.
     */
    @Override
    public String getServletName() {
        return servletConfig.getServletName();
    }

    /**
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being placed into service. See {@link Servlet#init}.
     *
     * <p>
     * This implementation stores the {@link ServletConfig} object it receives
     * from the servlet container for later use. When overriding this form of
     * the method, call <code>super.init(config)</code>.
     *
     * @param config the <code>ServletConfig</code> object that contains
     * configuration information for this servlet
     *
     * @exception ServletException if an exception occurs that interrupts the
     * servlet's normal operation
     *
     * @see UnavailableException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
        this.init();
    }

    /**
     * Init the servlet.
     *
     * @throws ServletException when a servlet error occurs.
     */
    public void init() throws ServletException {
    }

    /**
     * Log the message.
     *
     * @param message the message.
     */
    public void log(String message) {
        getServletContext().log(getServletName() + ": " + message);
    }

    /**
     * Log the message.
     *
     * @param message the message.
     * @param throwable the throwable.
     */
    public void log(String message, Throwable throwable) {
        getServletContext().log(getServletName() + ": " + message, throwable);
    }

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public abstract void service(ServletRequest request, ServletResponse response) throws ServletException, IOException;
}

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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.ServletEnvironment;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import static jakarta.servlet.DispatcherType.FORWARD;
import static jakarta.servlet.DispatcherType.INCLUDE;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The default named RequestDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultNamedRequestDispatcher implements RequestDispatcher {

    /**
     * Stores the Servlet environment.
     */
    private final ServletEnvironment environment;

    /**
     * Constructor.
     *
     * @param environment the Servlet environment.
     */
    public DefaultNamedRequestDispatcher(ServletEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void forward(ServletRequest servletRequest, ServletResponse servletResponse) 
            throws ServletException, IOException {
        WebApplicationRequest request = (WebApplicationRequest) servletRequest;
        WebApplicationResponse response = (WebApplicationResponse) servletResponse;
        
        /* - JAVADOC
         *
         * If the response is already committed throw an IllegalStateException.
         */
        if (response.isCommitted()) {
            throw new IllegalStateException("Response has already been committed");
        }

        /* - JAVADOC
         *
         * Clear uncommitted output in the response buffer before forwarding
         */
        response.resetBuffer();
        
        /* - JAVADOC
         *
         * Sets the dispatcher type of the given request to DispatcherType.FORWARD.
         */
        request.setDispatcherType(FORWARD);
        environment.getServlet().service(request, response);

        /* - Servlet:SPEC:80
         * 
         * If the request has not entered async mode and the response is not yet
         * committed we need to flush the response and close the outputstream.
         */
        if (!request.isAsyncStarted() && !response.isCommitted()) {
            response.flushBuffer();
            try (OutputStream outputStream = response.getWebApplicationOutputStream()) {
                outputStream.flush();
            }
        }
    }

    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) 
            throws ServletException, IOException {

        WebApplicationRequest request = (WebApplicationRequest) servletRequest;
        WebApplicationResponse response = (WebApplicationResponse) servletResponse;
        
        /* - JAVADOC
         *
         * Set dispatcher type to DispatcherType.INCLUDE.
         */
        request.setDispatcherType(INCLUDE);
        environment.getServlet().service(request, response);
    }
}

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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.AsyncDispatcher;
import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The default AsyncDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAsyncDispatcher implements AsyncDispatcher {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(DefaultAsyncDispatcher.class.getName());

    /**
     * Stores the web application.
     */
    private final WebApplication webApplication;

    /**
     * Stores the path.
     */
    private final String path;

    /**
     * Stores the async start request.
     */
    private final ServletRequest asyncStartRequest;

    /**
     * Stores the async start response.
     */
    private final ServletResponse asyncStartResponse;

    /**
     * Constructor.
     *
     * @param webApplication the web application.
     * @param path the path.
     * @param asyncStartRequest the request.
     * @param asyncStartResponse the asyncStartResponse.
     */
    public DefaultAsyncDispatcher(WebApplication webApplication, String path, ServletRequest asyncStartRequest, ServletResponse asyncStartResponse) {
        this.webApplication = webApplication;
        this.path = path;
        this.asyncStartRequest = asyncStartRequest;
        this.asyncStartResponse = asyncStartResponse;
    }

    @Override
    public void dispatch() {
        AsyncContext asyncContext = asyncStartRequest.getAsyncContext();
        RequestDispatcher requestDispatcher = webApplication.getRequestDispatcher(path);

        new Thread(() -> {
            Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());

            ServletRequest dispatchedRequest = addAsyncWrapper(asyncStartRequest);
            try {
                requestDispatcher.forward(dispatchedRequest, asyncStartResponse);
            } catch (Throwable t) {
                LOGGER.log(WARNING, "Error occurred during dispatch", t);
            }

            if (!dispatchedRequest.isAsyncStarted()) {
                asyncContext.complete();
            }

        }).start();
    }

    private ServletRequest addAsyncWrapper(ServletRequest request) {
        if (request instanceof HttpServletRequest httpServletRequest) {
            return new AsyncHttpDispatchWrapper(httpServletRequest);
        }

        return new AsyncNonHttpDispatchWrapper(request);
    }
}

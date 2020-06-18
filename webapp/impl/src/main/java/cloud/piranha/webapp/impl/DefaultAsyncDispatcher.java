/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cloud.piranha.webapp.api.AsyncDispatcher;
import cloud.piranha.webapp.api.WebApplication;

/**
 * The default AsyncDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAsyncDispatcher implements AsyncDispatcher {

    private final WebApplication webApplication;
    private final String path;
    private final ServletRequest asyncStartRequest;
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

    /**
     * @see AsyncDispatcher#dispatch()
     */
    @Override
    public void dispatch() {
        AsyncContext asyncContext = asyncStartRequest.getAsyncContext();
        RequestDispatcher requestDispatcher = webApplication.getRequestDispatcher(path);

        new Thread(() -> {
            Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());

            try {
                requestDispatcher.forward(addAsyncWrapper(asyncStartRequest), asyncStartResponse);
            } catch (Throwable t) {
                // TODO: Notify listeners
            }

            // TODO: check complete not already called
            asyncContext.complete();

        }).start();
    }

    private ServletRequest addAsyncWrapper(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return new AsyncHttpDispatchWrapper((HttpServletRequest) request);
        }

        return new AsyncNonHtttpDispatchWrapper(request);
    }

}

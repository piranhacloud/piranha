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

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;

import cloud.piranha.webapp.api.AsyncManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;
import cloud.piranha.webapp.api.WebApplicationResponse;

/**
 * The default AsyncContext.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAsyncContext implements AsyncContext {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultAsyncContext.class.getName());

    /**
     * Stores the listeners.
     */
    private final List<AsyncListener> listeners = new ArrayList<>();

    /**
     * Stores the request.
     */
    private final ServletRequest request;

    /**
     * Stores the response.
     */
    private final ServletResponse response;

    /**
     * Stores the underlying request.
     */
    private final WebApplicationRequest underlyingRequest;
    
    /**
     * Stores the underlying response.
     */
    private final WebApplicationResponse underlyingResponse;

    /**
     * Stores the timeout.
     */
    private long timeout = 30000; // 30 seconds, as mandated by spec

    /**
     * Constructor.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     */
    public DefaultAsyncContext(ServletRequest request, ServletResponse response) {
        this.request = request;
        this.response = response;

        ServletRequest currentRequest = request;
        while (currentRequest instanceof ServletRequestWrapper) {
            ServletRequestWrapper wrapper = (ServletRequestWrapper) currentRequest;
            currentRequest = wrapper.getRequest();
        }
        underlyingRequest = (WebApplicationRequest) currentRequest;
        
        ServletResponse currentResponse = response;
        while (currentResponse instanceof ServletResponseWrapper) {
            ServletResponseWrapper wrapper = (ServletResponseWrapper) currentResponse;
            currentResponse = wrapper.getResponse();
        }
        underlyingResponse = (WebApplicationResponse) currentResponse;

        
    }

    /**
     * Add the listener.
     *
     * @param listener the listener.
     * @see AsyncContext#addListener(javax.servlet.AsyncListener)
     */
    @Override
    public void addListener(AsyncListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Add the listener.
     *
     * @param listener the listener.
     * @param request the servlet request.
     * @param response the servlet response.
     */
    @Override
    public void addListener(AsyncListener listener, ServletRequest request, ServletResponse response) {
        this.listeners.add(listener);
    }

    /**
     * Create the listener.
     *
     * @param <T> the class.
     * @param type the type.
     * @return the listener.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends AsyncListener> T createListener(Class<T> type) throws ServletException {
        try {
            return type.getConstructor().newInstance();
        } catch (Throwable t) {
            LOGGER.log(WARNING, t, () -> "Unable to create AsyncListener: " + type.getName());
            throw new ServletException("Unable to create listener", t);
        }
    }

    /**
     * @see AsyncContext#dispatch()
     */
    @Override
    public void dispatch() {
        String path;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            path = httpServletRequest.getRequestURI();
        } else {
            path = underlyingRequest.getRequestURI();
        }
        dispatch(path);
    }

    /**
     * @see AsyncContext#dispatch(java.lang.String)
     */
    @Override
    public void dispatch(String path) {
        dispatch(request.getServletContext(), path);
    }

    /**
     * Dispatch.
     *
     * @param servletContext the servlet context.
     * @param path the path.
     */
    @Override
    public void dispatch(ServletContext servletContext, String path) {
        WebApplication webApplication = (WebApplication) servletContext;
        AsyncManager asyncManager = webApplication.getAsyncManager();
        asyncManager.getDispatcher(webApplication, path, request, response)
                    .dispatch();
    }
    
    /**
     * Complete the async context.
     */
    @Override
    public void complete() {
        LOGGER.log(FINE, () -> "Completing async processing");
        
        if (!listeners.isEmpty()) {
            listeners.forEach((listener) -> {
                try {
                    listener.onComplete(new AsyncEvent(this));
                } catch (IOException ioe) {
                    LOGGER.log(WARNING,ioe, () -> "IOException when calling onComplete on AsyncListener");
                    // nothing can be done at this point.
                }
            });
        }
        
        LOGGER.log(FINE, () -> "Flushing async response buffer");
        
        if (!response.isCommitted()) {
            try {
                response.flushBuffer();
            } catch (IOException ioe) {
                    LOGGER.log(WARNING, ioe, () -> "IOException when flushing async response buffer");
                // nothing can be done at this point.
            }
        }
        
        /*
         * TODO - review this as it exposes implementation detail and we should not have to do so.
         */
        underlyingResponse.closeAsyncResponse();
    }

    /**
     * Get the request.
     *
     * @return the request.
     * @see AsyncContext#getRequest()
     */
    @Override
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * Get the response.
     *
     * @return the response.
     * @see AsyncContext#getResponse()
     */
    @Override
    public ServletResponse getResponse() {
        return response;
    }

    /**
     * Get the timeout.
     *
     * @return the timeout.
     * @see AsyncContext#getTimeout()
     */
    @Override
    public long getTimeout() {
        return timeout;
    }

    /**
     * Do we have the original request and response?
     *
     * @return true if we do, false otherwise.
     */
    @Override
    public boolean hasOriginalRequestAndResponse() {
        return true;
    }

    /**
     * Set the timeout.
     *
     * @param timeout the timeout.
     */
    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Start the thread.
     *
     * @param runnable the runnable.
     */
    @Override
    public void start(Runnable runnable) {
        LOGGER.log(FINE,  "Starting async context with: {0}", runnable);
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

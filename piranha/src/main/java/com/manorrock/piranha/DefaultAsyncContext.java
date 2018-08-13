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
package com.manorrock.piranha;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The default AsyncContext.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAsyncContext implements AsyncContext {

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
     * Stores the timeout.
     */
    private long timeout;

    /**
     * Constructor.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     */
    public DefaultAsyncContext(ServletRequest request, ServletResponse response) {
        this.request = request;
        this.response = response;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Complete the async context.
     */
    @Override
    public void complete() {
    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> type) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispatch() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispatch(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispatch(ServletContext sc, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public boolean hasOriginalRequestAndResponse() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

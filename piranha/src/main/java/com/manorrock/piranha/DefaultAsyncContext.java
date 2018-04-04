/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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

    @Override
    public void complete() {
        throw new UnsupportedOperationException("Not supported yet.");
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
     * @return @see AsyncContext#getRequest()
     */
    @Override
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * @return @see AsyncContext#getResponse()
     */
    @Override
    public ServletResponse getResponse() {
        return response;
    }

    /**
     * @return @see AsyncContext#getTimeout()
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

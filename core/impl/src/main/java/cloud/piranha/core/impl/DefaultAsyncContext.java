/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.core.api.AsyncManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The default AsyncContext.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAsyncContext implements AsyncContext {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(DefaultAsyncContext.class.getName());

    /**
     * Stores the listeners.
     */
    private final List<AsyncListener> listeners = new ArrayList<>();

    /**
     * The request that comes from a call to <code>request.startAsync()</code>
     *
     * <p>
     * This is either the request the caller passed in when using
     * <code>request.startAsync(someRequest, someResponse)</code> or it's the
     * request object on which <code>startAsync</code> was called when using the
     * zero argument version <code>request.startAsync()</code>.
     *
     * <p>
     * In the latter case, the request is guaranteed to be the "original
     * request", which is the request passed to the servlet that started the
     * async cycle. In the former case it can either be the "original request",
     * or it can be that request but wrapped by user code.
     */
    private final ServletRequest asyncStartRequest;

    /**
     * The response that comes from a call to <code>request.startAsync()</code>
     *
     * <p>
     * This is either the response the caller passed in when using
     * <code>request.startAsync(someRequest, someResponse)</code> or it's the
     * response object associated with the request object on which
     * <code>startAsync</code> was called when using the zero argument version
     * <code>request.startAsync()</code>.
     *
     * <p>
     * In the latter case, the response is guaranteed to be the "original
     * response", which is the response passed to the servlet that started the
     * async cycle. In the former case it can either be the "original response",
     * or it can be that response but wrapped by user code.
     */
    private final ServletResponse asyncStartResponse;

    /**
     * "the request object passed to the first servlet object in the call chain
     * that received the request from the client."
     */
    private final WebApplicationRequest originalRequest;

    /**
     * The response object passed to the first servlet object in the call chain
     * that received the request from the client."
     */
    private final WebApplicationResponse originalResponse;

    /**
     * Stores the timeout.
     */
    private long timeout = Long.parseLong(System.getProperty("piranha.async.timeout", "30000")); // 30 seconds, as mandated by spec

    /**
     * Tracks whether dispatch() has already been called on this context or not.
     */
    private boolean dispatched;

    /**
     * Stores the scheduled thread pool executor.
     */
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    /**
     * Constructor.
     *
     * @param asyncStartRequest the servlet asyncStartRequest.
     * @param asyncStartResponse the servlet asyncStartResponse.
     */
    public DefaultAsyncContext(ServletRequest asyncStartRequest, ServletResponse asyncStartResponse) {
        this.asyncStartRequest = requireNonNull(asyncStartRequest);
        this.asyncStartResponse = requireNonNull(asyncStartResponse);

        originalRequest = unwrapFully(asyncStartRequest);
        originalResponse = unwrapFully(asyncStartResponse);

        scheduledThreadPoolExecutor.schedule(this::onTimeOut, timeout, MILLISECONDS);
    }

    @Override
    public void addListener(AsyncListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void addListener(AsyncListener listener, ServletRequest request, ServletResponse response) {
        this.listeners.add(listener);
    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> type) throws ServletException {
        try {
            return type.getConstructor().newInstance();
        } catch (Throwable t) {
            LOGGER.log(WARNING, () -> "Unable to create AsyncListener: " + type.getName(), t);
            throw new ServletException("Unable to create listener", t);
        }
    }

    /**
     * @see AsyncContext#dispatch()
     */
    @Override
    public void dispatch() {
        String path;
        if (asyncStartRequest instanceof HttpServletRequest httpServletRequest) {
            path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        } else {
            path = originalRequest.getRequestURI().substring(originalRequest.getContextPath().length());
        }
        dispatch(path);
    }

    /**
     * @see AsyncContext#dispatch(java.lang.String)
     */
    @Override
    public void dispatch(String path) {
        dispatch(asyncStartRequest.getServletContext(), path);
    }

    /**
     * Dispatch.
     *
     * @param servletContext the servlet context.
     * @param path the path.
     */
    @Override
    public void dispatch(ServletContext servletContext, String path) {
        if (dispatched) {
            throw new IllegalStateException("Dispatch already called on this async contexct");
        }
        dispatched = true;

        WebApplication webApplication = (WebApplication) servletContext;
        AsyncManager asyncManager = webApplication.getManager().getAsyncManager();
        asyncManager.getDispatcher(webApplication, path, asyncStartRequest, asyncStartResponse)
                .dispatch();
    }

    @Override
    public void complete() {

        scheduledThreadPoolExecutor.shutdownNow();

        LOGGER.log(DEBUG, () -> "Completing async processing");

        if (!listeners.isEmpty()) {
            listeners.forEach(listener -> {
                try {
                    listener.onComplete(new AsyncEvent(this));
                } catch (IOException ioe) {
                    LOGGER.log(WARNING, () -> "IOException when calling onComplete on AsyncListener", ioe);
                }
            });
        }

        LOGGER.log(DEBUG, () -> "Flushing async asyncStartResponse buffer");

        try {
            asyncStartResponse.flushBuffer();
        } catch (IOException ioe) {
            LOGGER.log(WARNING, () -> "IOException when flushing async asyncStartResponse buffer", ioe);
        }

        originalResponse.closeAsyncResponse();
    }

    /**
     * Process on timeout
     */
    public void onTimeOut() {
        scheduledThreadPoolExecutor.shutdownNow();

        if (!listeners.isEmpty()) {
            listeners.forEach(listener -> {
                try {
                    listener.onTimeout(new AsyncEvent(this));
                } catch (IOException ioe) {
                    LOGGER.log(WARNING, () -> "IOException when calling onTimeout on AsyncListener", ioe);
                }
            });
        }

        LOGGER.log(DEBUG, () -> "Flushing async asyncStartResponse buffer");

        if (!asyncStartResponse.isCommitted()) {
            try {
                asyncStartResponse.flushBuffer();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, () -> "IOException when flushing async asyncStartResponse buffer", ioe);
            }
        }

        originalResponse.closeAsyncResponse();
    }

    @Override
    public ServletRequest getRequest() {
        return asyncStartRequest;
    }

    @Override
    public ServletResponse getResponse() {
        return asyncStartResponse;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public boolean hasOriginalRequestAndResponse() {
        return originalRequest == asyncStartRequest && originalResponse == asyncStartResponse;
    }

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
        LOGGER.log(DEBUG, "Starting async context with: {0}", runnable);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Unwrap the servlet request.
     *
     * @param <T> the type.
     * @param request the request to unwrap.
     * @return the unwrapped request.
     */
    @SuppressWarnings("unchecked")
    private <T extends ServletRequest> T unwrapFully(ServletRequest request) {
        ServletRequest currentRequest = request;
        while (currentRequest instanceof ServletRequestWrapper wrapper) {
            currentRequest = wrapper.getRequest();
        }
        return (T) currentRequest;
    }

    /**
     * Unwrap the servlet response.
     *
     * @param <T> the type.
     * @param response the response to unwrap.
     * @return the unwrapped response.
     */
    @SuppressWarnings("unchecked")
    private <T extends ServletResponse> T unwrapFully(ServletResponse response) {
        ServletResponse currentResponse = response;
        while (currentResponse instanceof ServletResponseWrapper wrapper) {
            currentResponse = wrapper.getResponse();
        }
        return (T) currentResponse;
    }
}

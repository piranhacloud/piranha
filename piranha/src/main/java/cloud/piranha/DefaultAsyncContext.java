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
package cloud.piranha;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

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
        this.listeners.add(listener);
    }

    /**
     * Complete the async context.
     */
    @Override
    public void complete() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Completing async processing");
        }
        if (!listeners.isEmpty()) {
            listeners.forEach((listener) -> {
                try {
                    listener.onComplete(new AsyncEvent(this));
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "IOException when calling onComplete on AsyncListener", ioe);
                    }
                    // nothing can be done at this point.
                }
            });
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Flushing async response buffer");
        }
        if (!response.isCommitted()) {
            try {
                response.flushBuffer();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing async response buffer", ioe);
                }
                // nothing can be done at this point.
            }
        }
        /*
         * TODO - review this as it exposes implementation detail and we should not have to do so.
         */
        if (response instanceof DefaultWebApplicationResponse) {
            try {
                DefaultWebApplicationResponse underlyingResponse = (DefaultWebApplicationResponse) response;
                underlyingResponse.getUnderlyingOutputStream().flush();
                underlyingResponse.getUnderlyingOutputStream().close();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing the underlying async output stream", ioe);
                }
            }
        }
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
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to create AsyncListener: " + type.getName(), t);
            }
            throw new ServletException("Unable to create listener", t);
        }
    }

    /**
     * Dispatch.
     */
    @Override
    public void dispatch() {
        if (!response.isCommitted()) {
            try {
                response.flushBuffer();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing async response buffer", ioe);
                }
                // nothing can be done at this point.
            }
        }
        ServletResponse servletResponse = response;
        while (servletResponse instanceof ServletResponseWrapper) {
            ServletResponseWrapper wrapper = (ServletResponseWrapper) servletResponse;
            servletResponse = wrapper.getResponse();
        }
        if (servletResponse instanceof WebApplicationResponse) {
            try {
                WebApplicationResponse underlyingResponse = (WebApplicationResponse) servletResponse;
                underlyingResponse.getUnderlyingOutputStream().flush();
                underlyingResponse.getUnderlyingOutputStream().close();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing the underlying async output stream", ioe);
                }
            }
        }
    }

    /**
     * Dispatch.
     *
     * @param path the path.
     */
    @Override
    public void dispatch(String path) {
        if (!response.isCommitted()) {
            try {
                response.flushBuffer();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing async response buffer", ioe);
                }
                // nothing can be done at this point.
            }
        }
        ServletResponse servletResponse = response;
        while (servletResponse instanceof ServletResponseWrapper) {
            ServletResponseWrapper wrapper = (ServletResponseWrapper) servletResponse;
            servletResponse = wrapper.getResponse();
        }
        if (servletResponse instanceof WebApplicationResponse) {
            try {
                WebApplicationResponse underlyingResponse = (WebApplicationResponse) servletResponse;
                underlyingResponse.getUnderlyingOutputStream().flush();
                underlyingResponse.getUnderlyingOutputStream().close();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing the underlying async output stream", ioe);
                }
            }
        }
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
        RequestDispatcher dispatcher = webApplication.getRequestDispatcher(path);
        try {
            ServletRequest servletRequest = request;
            while (servletRequest instanceof ServletRequestWrapper) {
                ServletRequestWrapper wrapper = (ServletRequestWrapper) servletRequest;
                servletRequest = wrapper.getRequest();
            }
            HttpServletRequestWrapper wrappedRequest
                    = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {

                private boolean asyncStarted = false;

                @Override
                public boolean isAsyncStarted() {
                    return asyncStarted;
                }

                @Override
                public DispatcherType getDispatcherType() {
                    return DispatcherType.ASYNC;
                }

                public void setAsyncStarted(boolean asyncStarted) {
                    this.asyncStarted = asyncStarted;
                }
            };
            ServletResponse servletResponse = response;
            while (servletResponse instanceof ServletResponseWrapper) {
                ServletResponseWrapper wrapper = (ServletResponseWrapper) servletResponse;
                servletResponse = wrapper.getResponse();
            }
            dispatcher.forward(wrappedRequest, servletResponse);
        } catch (IOException | ServletException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "An error occurred during async dispatch", e);
            }
        }
        if (!response.isCommitted()) {
            try {
                response.flushBuffer();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing async response buffer", ioe);
                }
                // nothing can be done at this point.
            }
        }
        ServletResponse servletResponse = response;
        while (servletResponse instanceof ServletResponseWrapper) {
            ServletResponseWrapper wrapper = (ServletResponseWrapper) servletResponse;
            servletResponse = wrapper.getResponse();
        }
        if (servletResponse instanceof WebApplicationResponse) {
            try {
                WebApplicationResponse underlyingResponse = (WebApplicationResponse) servletResponse;
                underlyingResponse.getUnderlyingOutputStream().flush();
                underlyingResponse.getUnderlyingOutputStream().close();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "IOException when flushing the underlying async output stream", ioe);
                }
            }
        }
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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Starting async context with: {0}", runnable);
        }
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

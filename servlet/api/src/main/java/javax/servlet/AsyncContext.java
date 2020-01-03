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
package javax.servlet;

/**
 * The AsyncContext API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface AsyncContext {

    /**
     * Defines the ASYNC_CONTEXT_PATH constant.
     */
    static final String ASYNC_CONTEXT_PATH = "javax.servlet.async.context_path";
    
    /**
     * Defines the ASYNC_MAPPING constant.
     */
    static final String ASYNC_MAPPING = "javax.servlet.async.mapping";

    /**
     * Defines the ASYNC_PATH_INFO constant.
     */
    static final String ASYNC_PATH_INFO = "javax.servlet.async.path_info";

    /**
     * Defines the ASYNC_QUERY_STRING constant.
     */
    static final String ASYNC_QUERY_STRING = "javax.servlet.async.query_string";

    /**
     * Defines the ASYNC_REQUEST_URI constant.
     */
    static final String ASYNC_REQUEST_URI = "javax.servlet.async.request_uri";

    /**
     * Define the ASYNC_SERVLET_PATH constant.
     */
    static final String ASYNC_SERVLET_PATH = "javax.servlet.async.servlet_path";

    /**
     * Add the async listener.
     *
     * @param listener the async listener.
     */
    public void addListener(AsyncListener listener);

    /**
     * Add the async listener.
     *
     * @param listener the async listener.
     * @param servletRequest the servlet request.
     * @param servletResponse the servlet response.
     */
    public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse);

    /**
     * Complete the request.
     */
    public void complete();

    /**
     * Create the listener.
     *
     * @param <T> the listener type.
     * @param clazz the class.
     * @return the listener.
     * @throws ServletException when a serv
     */
    public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException;

    /**
     * Dispatch the request.
     */
    public void dispatch();

    /**
     * Dispatch the request.
     *
     * @param context the servlet context.
     * @param path the path.
     */
    public void dispatch(ServletContext context, String path);

    /**
     * Dispatch the request.
     *
     * @param path the path.
     */
    public void dispatch(String path);

    /**
     * Get the request.
     *
     * @return the request.
     */
    public ServletRequest getRequest();

    /**
     * Get the response.
     *
     * @return the response.
     */
    public ServletResponse getResponse();

    /**
     * Get the timeout.
     *
     * @return the timeout.
     */
    public long getTimeout();

    /**
     * Has the original request and response.
     *
     * @return true if it does, false otherwise.
     */
    public boolean hasOriginalRequestAndResponse();

    /**
     * Set the timeout.
     *
     * @param timeout the timeout.
     */
    public void setTimeout(long timeout);

    /**
     * Start the thread.
     *
     * @param runnable the runnable.
     */
    public void start(Runnable runnable);
}

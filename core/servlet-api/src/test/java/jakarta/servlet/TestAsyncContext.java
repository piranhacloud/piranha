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

/**
 * A Test AsyncContext.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestAsyncContext implements AsyncContext {
    
    /**
     * Stores the request.
     */
    private ServletRequest request;
    
    /**
     * Stores the response.
     */
    private ServletResponse response;

    /**
     * Constructor.
     * 
     * @param request the request.
     * @param response the response.
     */
    public TestAsyncContext(ServletRequest request, ServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void addListener(AsyncListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void complete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispatch() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispatch(ServletContext context, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispatch(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@return the request}
     */
    @Override
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * {@return the response}
     */
    @Override
    public ServletResponse getResponse() {
        return response;
    }

    @Override
    public long getTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasOriginalRequestAndResponse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTimeout(long timeout) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void start(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

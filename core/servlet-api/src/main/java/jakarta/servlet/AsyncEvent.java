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
package jakarta.servlet;

/**
 * The AsyncEvent API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AsyncEvent {

    /**
     * Stores the async context.
     */
    private AsyncContext context;

    /**
     * Stores the request.
     */
    private ServletRequest suppliedRequest;

    /**
     * Stores the response.
     */
    private ServletResponse suppliedResponse;

    /**
     * Stores the throwable.
     */
    private Throwable throwable;

    /**
     * Constructor.
     *
     * @param context the async context.
     */
    public AsyncEvent(AsyncContext context) {
        this(context, context.getRequest(), context.getResponse(), null);
    }

    /**
     * Constructor.
     *
     * @param context the async context.
     * @param suppliedRequest the request.
     * @param suppliedResponse the response.
     */
    public AsyncEvent(AsyncContext context, ServletRequest suppliedRequest, ServletResponse suppliedResponse) {
        this(context, suppliedRequest, suppliedResponse, null);
    }

    /**
     * Constructor.
     *
     * @param context the async context.
     * @param throwable the throwable.
     */
    public AsyncEvent(AsyncContext context, Throwable throwable) {
        this(context, context.getRequest(), context.getResponse(), throwable);
    }

    /**
     * Constructor
     *
     * @param context the context.
     * @param suppliedRequest the request.
     * @param suppliedResponse the response.
     * @param throwable the throwable.
     */
    public AsyncEvent(AsyncContext context, ServletRequest suppliedRequest, ServletResponse suppliedResponse, Throwable throwable) {
        this.context = context;
        this.suppliedRequest = suppliedRequest;
        this.suppliedResponse = suppliedResponse;
        this.throwable = throwable;
    }

    /**
     * {@return the async context}
     */
    public AsyncContext getAsyncContext() {
        return context;
    }

    /**
     * {@return the supplied request}
     */
    public ServletRequest getSuppliedRequest() {
        return suppliedRequest;
    }

    /**
     * {@return the supplied response}
     */
    public ServletResponse getSuppliedResponse() {
        return suppliedResponse;
    }

    /**
     * {@return the throwable}
     */
    public Throwable getThrowable() {
        return throwable;
    }
}

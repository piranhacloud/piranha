/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

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
     * Get the async context.
     *
     * @return the async context.
     */
    public AsyncContext getAsyncContext() {
        return context;
    }

    /**
     * Get the supplied request.
     *
     * @return the supplied request.
     */
    public ServletRequest getSuppliedRequest() {
        return suppliedRequest;
    }

    /**
     * Get the supplied response.
     *
     * @return the supplied response.
     */
    public ServletResponse getSuppliedResponse() {
        return suppliedResponse;
    }

    /**
     * Get the throwable.
     *
     * @return the throwable.
     */
    public Throwable getThrowable() {
        return throwable;
    }
}

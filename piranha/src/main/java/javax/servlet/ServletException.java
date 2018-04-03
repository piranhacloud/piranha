/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The ServletException API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletException extends Exception {

    /**
     * Stores the cause.
     */
    private Throwable rootCause;

    /**
     * Constructor.
     */
    public ServletException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message the message.
     */
    public ServletException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message the message.
     * @param rootCause the root cause.
     */
    public ServletException(String message, Throwable rootCause) {
        super(message, rootCause);
        this.rootCause = rootCause;
    }

    /**
     * Constructor.
     *
     * @param rootCause the root cause.
     */
    public ServletException(Throwable rootCause) {
        super(rootCause);
        this.rootCause = rootCause;
    }

    /**
     * Get the root cause.
     *
     * @return the root cause.
     */
    public Throwable getRootCause() {
        return rootCause;
    }
}

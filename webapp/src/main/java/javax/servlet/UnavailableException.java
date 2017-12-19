/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The UnavailableException API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class UnavailableException extends ServletException {

    /**
     * Stores the permanent flag.
     */
    private final boolean permanent;

    /**
     * Stores the number of seconds.
     */
    private final int unavailableSeconds;

    /**
     * Constructor.
     *
     * @param message the message.
     */
    public UnavailableException(String message) {
        super(message);
        this.permanent = true;
        this.unavailableSeconds = -1;
    }

    /**
     * Constructor.
     *
     * @param message the message.
     * @param unavailableSeconds the unavailable seconds.
     */
    public UnavailableException(String message, int unavailableSeconds) {
        super(message);
        this.permanent = false;
        this.unavailableSeconds = unavailableSeconds;
    }

    /**
     * Constructor.
     *
     * @param servlet the servlet.
     * @param message the message.
     * @deprecated
     */
    public UnavailableException(Servlet servlet, String message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructor.
     *
     * @param unavailableSeconds the unavailable seconds.
     * @param servlet the servlet.
     * @param message the message.
     * @deprecated
     */
    public UnavailableException(int unavailableSeconds, Servlet servlet, String message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the servlet.
     *
     * @return the servlet
     * @deprecated
     */
    public Servlet getServlet() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the unavailableSeconds.
     *
     * @return the unavailable seconds.
     */
    public int getUnavailableSeconds() {
        return unavailableSeconds;
    }

    /**
     * Is unavailability permanent.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isPermanent() {
        return permanent;
    }
}

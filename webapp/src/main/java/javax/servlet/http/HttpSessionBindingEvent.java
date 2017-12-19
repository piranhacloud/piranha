/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

/**
 * The HttpSessionBindingEvent API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpSessionBindingEvent extends HttpSessionEvent {

    /**
     * Stores the name.
     */
    private String name;

    /**
     * Stores the value.
     */
    private Object value;

    /**
     * Constructor.
     *
     * @param session the HTTP session.
     * @param name the name.
     */
    public HttpSessionBindingEvent(HttpSession session, String name) {
        super(session);
        this.name = name;
    }

    /**
     * Constructor.
     *
     * @param session the HTTP session.
     * @param name the name.
     * @param value the value.
     */
    public HttpSessionBindingEvent(HttpSession session, String name, Object value) {
        super(session);
        this.name = name;
        this.value = value;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the HTTP session.
     *
     * @return the HTTP session.
     */
    @Override
    public HttpSession getSession() {
        return super.getSession();
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    public Object getValue() {
        return this.value;
    }
}

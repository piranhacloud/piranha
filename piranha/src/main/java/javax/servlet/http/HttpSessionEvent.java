/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.EventObject;

/**
 * The HttpSessionEvent API.
 */
public class HttpSessionEvent extends EventObject {

    /**
     * Constructor.
     *
     * @param session the HTTP session.
     */
    public HttpSessionEvent(HttpSession session) {
        super(session);
    }

    /**
     * Get the HTTP session.
     *
     * @return the HTTP session.
     */
    public HttpSession getSession() {
        return (HttpSession) super.getSource();
    }
}

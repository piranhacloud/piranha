/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.Enumeration;

/**
 * The HttpSessionContext API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @deprecated
 */
public interface HttpSessionContext {

    /**
     * Get the HTTP session.
     *
     * @param sessionId the HTTP session id.
     * @return the HTTP session.
     * @deprecated
     *
     */
    public HttpSession getSession(String sessionId);

    /**
     * Get the HTTP session ids.
     *
     * @return the HTTP session ids.
     * @deprecated
     */
    public Enumeration<String> getIds();
}

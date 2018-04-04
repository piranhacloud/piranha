/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.EventListener;
import java.util.Set;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The HttpSessionManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSessionManager {

    /**
     * Add a listener.
     *
     * @param <T> the type.
     * @param listener the listener.
     */
    <T extends EventListener> void addListener(T listener);

    /**
     * Attribute added.
     *
     * @param session the HTTP session.
     * @param name the name.
     * @param value the value.
     */
    void attributeAdded(HttpSession session, String name, Object value);

    /**
     * Attribute replaced.
     *
     * @param session the HTTP session.
     * @param name the name.
     * @param value the value.
     */
    void attributeReplaced(HttpSession session, String name, Object value);

    /**
     * Change the session id and return it.
     *
     * @param request the request.
     * @return the session id.
     */
    String changeSessionId(HttpServletRequest request);

    /**
     * Create a session.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @return the session.
     */
    HttpSession createSession(WebApplication webApplication, HttpServletRequest request);

    /**
     * Encode the redirect URL.
     *
     * @param response the HTTP servlet response.
     * @param url the URL.
     * @return the encode URL.
     */
    String encodeRedirectURL(HttpServletResponse response, String url);

    /**
     * Encode the URL.
     *
     * @param response the HTTP servlet response.
     * @param url the URL.
     * @return the encoded URL.
     */
    String encodeURL(HttpServletResponse response, String url);

    /**
     * Get the default session tracking modes.
     *
     * @return the default session tracking modes.
     */
    Set<SessionTrackingMode> getDefaultSessionTrackingModes();

    /**
     * Get the effective session tracking modes.
     *
     * @return the effective session tracking modes.
     */
    Set<SessionTrackingMode> getEffectiveSessionTrackingModes();

    /**
     * Get the session.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @param currentSessionId the current session id.
     * @return the session.
     */
    HttpSession getSession(WebApplication webApplication, 
            HttpServletRequest request, String currentSessionId);

    /**
     * Get the session cookie config.
     *
     * @return the session cookie config.
     */
    SessionCookieConfig getSessionCookieConfig();

    /**
     * Is the session manager handling this session?
     *
     * @param sessionId the session id.
     * @return true if there is a session with the given session id.
     */
    boolean hasSession(String sessionId);

    /**
     * Set the session tracking modes.
     *
     * @param sessionTrackingModes the session tracking modes.
     */
    void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes);
}

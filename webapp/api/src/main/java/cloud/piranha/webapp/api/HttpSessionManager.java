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
package cloud.piranha.webapp.api;

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
     * Attribute removed.
     *
     * @param session the HTTP session.
     * @param name the name.
     */
    void attributeRemoved(HttpSession session, String name);

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
     * @param request the request.
     * @return the session.
     */
    HttpSession createSession(HttpServletRequest request);

    /**
     * Destroys a session.
     *
     * @param session the HTTP session.
     */
    void destroySession(HttpSession session);

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
     * @param request the request.
     * @param currentSessionId the current session id.
     * @return the session.
     */
    HttpSession getSession(HttpServletRequest request, String currentSessionId);

    /**
     * Get the session cookie config.
     *
     * @return the session cookie config.
     */
    SessionCookieConfig getSessionCookieConfig();

    /**
     * Get the session timeout (in minutes).
     *
     * @return the session timeout.
     */
    int getSessionTimeout();

    /**
     * Is the session manager handling this session?
     *
     * @param sessionId the session id.
     * @return true if there is a session with the given session id.
     */
    boolean hasSession(String sessionId);

    /**
     * Set the session timeout (in minutes).
     *
     * @param timeout the timeout.
     */
    void setSessionTimeout(int timeout);

    /**
     * Set the session tracking modes.
     *
     * @param sessionTrackingModes the session tracking modes.
     */
    void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes);

    /**
     * Set the web application.
     * 
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);
}

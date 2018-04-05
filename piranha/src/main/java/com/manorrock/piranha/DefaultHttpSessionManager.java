/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * The default HttpSessionManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpSessionManager implements HttpSessionManager, SessionCookieConfig {

    /**
     * Stores the session listeners.
     */
    protected final ArrayList<HttpSessionAttributeListener> attributeListeners;

    /**
     * Stores the comment.
     */
    protected String comment;

    /**
     * Stores the default session tracking modes.
     */
    protected final Set<SessionTrackingMode> defaultSessionTrackingModes;

    /**
     * Stores the domain.
     */
    protected String domain;

    /**
     * Stores the HTTP only flag.
     */
    protected boolean httpOnly;

    /**
     * Stores the session id listeners.
     */
    protected final ArrayList<HttpSessionIdListener> idListeners;

    /**
     * Stores the max age.
     */
    protected int maxAge;

    /**
     * Stores the name.
     */
    protected String name;

    /**
     * Stores the path.
     */
    protected String path;

    /**
     * Stores the secure flag.
     */
    protected boolean secure;

    /**
     * Stores the session listeners.
     */
    protected final ArrayList<HttpSessionListener> sessionListeners;

    /**
     * Stores the session tracking modes.
     */
    protected Set<SessionTrackingMode> sessionTrackingModes;

    /**
     * Stores the sessions.
     */
    protected final Map<String, DefaultHttpSession> sessions;

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Constructor.
     */
    public DefaultHttpSessionManager() {
        attributeListeners = new ArrayList<>(1);
        defaultSessionTrackingModes = EnumSet.of(SessionTrackingMode.COOKIE);
        idListeners = new ArrayList<>(1);
        name = "JSESSIONID";
        sessionListeners = new ArrayList<>(1);
        sessions = new ConcurrentHashMap<>();
    }

    /**
     * Add a listener.
     *
     * @param <T> the type.
     * @param listener the listener.
     */
    @Override
    public <T extends EventListener> void addListener(T listener) {
        if (listener instanceof HttpSessionAttributeListener) {
            attributeListeners.add((HttpSessionAttributeListener) listener);
        }

        if (listener instanceof HttpSessionIdListener) {
            idListeners.add((HttpSessionIdListener) listener);
        }

        if (listener instanceof HttpSessionListener) {
            sessionListeners.add((HttpSessionListener) listener);
        }
    }

    /**
     * Attribute added.
     *
     * @param session the HTTP session.
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void attributeAdded(HttpSession session, String name, Object value) {
        attributeListeners.stream().forEach((listener) -> {
            listener.attributeAdded(new HttpSessionBindingEvent(session, name, value));
        });
    }

    /**
     * Attribute removed.
     *
     * @param session the HTTP session.
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void attributeReplaced(HttpSession session, String name, Object value) {
        attributeListeners.stream().forEach((listener) -> {
            listener.attributeReplaced(new HttpSessionBindingEvent(session, name, value));
        });
    }

    /**
     * Change the session id.
     *
     * @param request the request.
     * @return the session id.
     */
    @Override
    public String changeSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String key = null;
        if (session != null) {
            String oldSessionId = session.getId();
            sessions.remove(oldSessionId);
            key = UUID.randomUUID().toString();
            DefaultHttpSession newSession = (DefaultHttpSession) session;
            newSession.setId(key);
            sessions.put(key, (DefaultHttpSession) session);

            idListeners.stream().forEach((idListener) -> {
                idListener.sessionIdChanged(new HttpSessionEvent(session), oldSessionId);
            });
        } else {
            throw new IllegalStateException("No session active");
        }
        return key;
    }

    /**
     * Create the session.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @return the session.
     */
    @Override
    public synchronized HttpSession createSession(WebApplication webApplication, HttpServletRequest request) {
        String key = UUID.randomUUID().toString();
        DefaultHttpSession result = new DefaultHttpSession(webApplication, key, true);
        result.setSessionManager(this);
        sessions.put(key, result);
        HttpServletResponse response = (HttpServletResponse) webApplication.getResponse(request);
        Cookie cookie = new Cookie(name, key);
        response.addCookie(cookie);

        sessionListeners.stream().forEach((sessionListener) -> {
            sessionListener.sessionCreated(new HttpSessionEvent(result));
        });

        return result;
    }

    /**
     * Encode the redirect URL.
     *
     * @param response the response.
     * @param url the redirect url.
     * @return the encoded redirect url.
     */
    @Override
    public String encodeRedirectURL(HttpServletResponse response, String url) {
        return url;
    }

    /**
     * Encode the URL.
     *
     * @param response the response.
     * @param url the url.
     * @return the encoded url.
     */
    @Override
    public String encodeURL(HttpServletResponse response, String url) {
        return url;
    }

    /**
     * Get the comment.
     *
     * @return the comment.
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Get the default session tracking modes.
     *
     * @return the default session tracking modes.
     */
    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return defaultSessionTrackingModes;
    }

    /**
     * Get the domain.
     *
     * @return the domain.
     */
    @Override
    public String getDomain() {
        return domain;
    }

    /**
     * Get the effective session tracking modes.
     *
     * @return the effective session tracking modes.
     */
    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return sessionTrackingModes;
    }

    /**
     * Get the max age.
     *
     * @return the max age.
     */
    @Override
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the path.
     *
     * @return the path.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Get the session.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @param currentSessionId the current session id.
     * @return the session.
     */
    @Override
    public HttpSession getSession(WebApplication webApplication, 
            HttpServletRequest request, String currentSessionId) {
        DefaultHttpSession result;
        HttpServletResponse response = (HttpServletResponse) webApplication.getResponse(request);
        result = sessions.get(currentSessionId);
        Cookie cookie = new Cookie(name, currentSessionId);
        response.addCookie(cookie);
        result.setNew(false);
        return result;
    }

    /**
     * Get the session cookie config.
     *
     * @return the session cookie config.
     */
    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return this;
    }

    /**
     * Has a session with the given id.
     *
     * @param sessionId the session id.
     * @return true if there is one, false otherwise.
     */
    @Override
    public boolean hasSession(String sessionId) {
        return sessionId != null ? sessions.containsKey(sessionId) : false;
    }

    /**
     * Is HTTP only?
     *
     * @return true if HTTP only, false otherwise.
     */
    @Override
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * Is secure.
     *
     * @return the secure flag.
     */
    @Override
    public boolean isSecure() {
        return secure;
    }

    /**
     * Set the comment.
     *
     * @param comment the comment.
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Set the domain.
     *
     * @param domain the domain
     */
    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Set the HTTP only flag.
     *
     * @param httpOnly the HTTP only flag.
     */
    @Override
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    /**
     * Set the max age.
     *
     * @param maxAge the max age.
     */
    @Override
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Set the name.
     *
     * @param name the name.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the path.
     *
     * @param path the path.
     */
    @Override
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Set the secure flag.
     *
     * @param secure the secure flag.
     */
    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * Set the session tracking modes.
     *
     * @param sessionTrackingModes the session tracking modes.
     */
    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        this.sessionTrackingModes = sessionTrackingModes;
    }
}

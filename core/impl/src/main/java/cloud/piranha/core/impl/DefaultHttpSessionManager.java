/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.HttpSessionManager;
import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.SessionTrackingMode;
import static jakarta.servlet.SessionTrackingMode.COOKIE;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionIdListener;
import jakarta.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The default HttpSessionManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpSessionManager implements HttpSessionManager, SessionCookieConfig, ServletRequestListener {

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
     * Stores the session counters.
     */
    protected final Map<String, AtomicInteger> sessionCounters = new HashMap<>();

    /**
     * Stores the cookie attributes.
     */
    protected HashMap<String, String> sessionCookieAttributes;

    /**
     * Stores the session listeners.
     */
    protected final ArrayList<HttpSessionListener> sessionListeners;

    /**
     * Stores the session timeout (in minutes).
     */
    protected int sessionTimeout;

    /**
     * Stores the session tracking modes.
     */
    protected Set<SessionTrackingMode> sessionTrackingModes;

    /**
     * Stores the sessions.
     */
    protected Map<String, HttpSession> sessions;

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Constructor.
     */
    public DefaultHttpSessionManager() {
        attributeListeners = new ArrayList<>(1);
        sessionCookieAttributes = new HashMap<>();
        defaultSessionTrackingModes = EnumSet.of(COOKIE);
        sessionTrackingModes = defaultSessionTrackingModes;
        idListeners = new ArrayList<>(1);
        name = "JSESSIONID";
        sessionListeners = new ArrayList<>(1);
        sessionTimeout = 10;
        maxAge = -1;
        sessions = new ConcurrentHashMap<>();
        ThreadFactory threadFactory = (Runnable r) -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, threadFactory);
        scheduler.scheduleWithFixedDelay(this::reapSessions, 0, 1300, MILLISECONDS);
    }

    @Override
    public <T extends EventListener> void addListener(T listener) {
        if (listener instanceof HttpSessionAttributeListener httpSessionAttributeListener) {
            attributeListeners.add(httpSessionAttributeListener);
        }
        if (listener instanceof HttpSessionIdListener httpSessionIdListener) {
            idListeners.add(httpSessionIdListener);
        }
        if (listener instanceof HttpSessionListener httpSessionListener) {
            sessionListeners.add(httpSessionListener);
        }
    }

    @Override
    public void attributeAdded(HttpSession session, String name, Object value) {
        attributeListeners.stream().forEach(listener -> listener.attributeAdded(new HttpSessionBindingEvent(session, name, value)));
        if (value instanceof HttpSessionBindingListener httpSessionBindingListener) {
            httpSessionBindingListener.valueBound(new HttpSessionBindingEvent(session, name));
        }
    }

    @Override
    public void attributeRemoved(HttpSession session, String name, Object value) {
        attributeListeners.stream().forEach(listener -> listener.attributeRemoved(new HttpSessionBindingEvent(session, name, value)));
        if (value instanceof HttpSessionBindingListener httpSessionBindingListener) {
            httpSessionBindingListener.valueUnbound(new HttpSessionBindingEvent(session, name));
        }
    }

    @Override
    public void attributeReplaced(HttpSession session, String name, Object oldValue, Object newValue) {
        attributeListeners.stream().forEach(listener -> listener.attributeReplaced(new HttpSessionBindingEvent(session, name, oldValue)));
        if (oldValue instanceof HttpSessionBindingListener httpSessionBindingListener) {
            httpSessionBindingListener.valueUnbound(new HttpSessionBindingEvent(session, name));
        }
        if (newValue instanceof HttpSessionBindingListener httpSessionBindingListener) {
            httpSessionBindingListener.valueBound(new HttpSessionBindingEvent(session, name));
        }
    }

    @Override
    public String changeSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalStateException("No session active");
        }
        String oldSessionId = session.getId();
        sessions.remove(oldSessionId);
        String sessionId = UUID.randomUUID().toString();
        DefaultHttpSession newSession = (DefaultHttpSession) session;
        newSession.setId(sessionId);
        sessions.put(sessionId, session);
        idListeners.stream().forEach(idListener -> idListener.sessionIdChanged(new HttpSessionEvent(session), oldSessionId));
        return sessionId;
    }

    @Override
    public synchronized HttpSession createSession(HttpServletRequest request) {
        String sessionId = UUID.randomUUID().toString();
        DefaultHttpSession session = new DefaultHttpSession(webApplication, sessionId, true);
        session.setMaxInactiveInterval(getSessionTimeout() * 60);
        session.setSessionManager(this);
        sessions.put(sessionId, session);
        sessionCounters.put(session.getId(), new AtomicInteger(1));
        HttpServletResponse response = (HttpServletResponse) webApplication.getResponse(request);
        Cookie cookie = new Cookie(name, sessionId);
        if (path != null) {
            cookie.setPath(path);
        } else {
            cookie.setPath("".equals(request.getContextPath()) ? "/" : request.getContextPath());
        }
        cookie.setComment(comment);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        response.addCookie(cookie);
        sessionListeners.stream().forEach(sessionListener -> sessionListener.sessionCreated(new HttpSessionEvent(session)));
        return session;
    }

    @Override
    public synchronized void destroySession(HttpSession session) {
        for (HttpSessionListener sessionListener : sessionListeners) {
            sessionListener.sessionDestroyed(new HttpSessionEvent(session));
        }
        sessions.remove(session.getId());
        sessionCounters.remove(session.getId());
        Iterator<String> attributeNames = session.getAttributeNames().asIterator();
        while(attributeNames.hasNext()) {
            String attributeName = attributeNames.next();
            attributeRemoved(session, attributeName, session.getAttribute(attributeName));
        }
    }

    @Override
    public String encodeRedirectURL(HttpServletResponse response, String url) {
        return url;
    }

    @Override
    public String encodeURL(HttpServletResponse response, String url) {
        return url;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return Collections.unmodifiableSet(defaultSessionTrackingModes);
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return Collections.unmodifiableSet(sessionTrackingModes);
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public HttpSession getSession(HttpServletRequest request, String currentSessionId) {
        return sessions.get(currentSessionId);
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return this;
    }

    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public boolean hasSession(String sessionId) {
        boolean result = false;
        if (sessionId != null) {
            result = sessions.containsKey(sessionId);
        }
        return result;
    }

    @Override
    public boolean isHttpOnly() {
        return httpOnly;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    /**
     * Reap any inactive session.
     */
    protected void reapSessions() {
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(sessions.keySet());
        keys.forEach(sessionId -> {
            HttpSession session = sessions.get(sessionId);
            if (session != null) {
                synchronized (session) {
                    if (sessionCounters.getOrDefault(session.getId(), new AtomicInteger(0)).intValue() <= 0) {
                        try {
                            if (session.getLastAccessedTime() + (session.getMaxInactiveInterval() * 1000L) - 1300 < System.currentTimeMillis()) {
                                session.invalidate();
                            }
                        } catch (IllegalStateException ise) {
                            // nothing to do
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setComment(String comment) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setComment once ServletContext is initialized");
        }
    }

    @Override
    public void setDomain(String domain) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setDomain once ServletContext is initialized");
        }
        this.domain = domain;
    }

    @Override
    public void setHttpOnly(boolean httpOnly) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setHttpOnly once ServletContext is initialized");
        }
        this.httpOnly = httpOnly;
    }

    @Override
    public void setMaxAge(int maxAge) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setMaxAge once ServletContext is initialized");
        }
        this.maxAge = maxAge;
    }

    @Override
    public void setName(String name) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setName once ServletContext is initialized");
        }
        this.name = name;
    }

    @Override
    public void setPath(String path) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setPath once ServletContext is initialized");
        }
        this.path = path;
    }

    @Override
    public void setSecure(boolean secure) {
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setSecure once ServletContext is initialized");
        }
        this.secure = secure;
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        if (sessionTrackingModes.size() > 1 && sessionTrackingModes.contains(SessionTrackingMode.SSL)) {
            throw new IllegalArgumentException("SSL cannot be combined with any other method");
        }
        this.sessionTrackingModes = Collections.unmodifiableSet(sessionTrackingModes);
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest httpRequest) {
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                synchronized (session) {
                    AtomicInteger integer = sessionCounters.get(session.getId());
                    if (integer != null) {
                        integer.incrementAndGet();
                    }
                }
            }
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest httpRequest) {
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                synchronized (session) {
                    AtomicInteger integer = sessionCounters.get(session.getId());
                    if (integer != null) {
                        integer.decrementAndGet();
                    }
                }
            }
        }
    }

    /*
       REVIEW FOR SERVLET 6
     */

    @Override
    public void setAttribute(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (webApplication.isInitialized()) {
            throw new IllegalStateException("You cannot call setAttribute once ServletContext is initialized");
        }
        String nameLowerCase = name.toLowerCase(Locale.ROOT);
        switch (nameLowerCase) {
            case "comment" -> setComment(value);
            case "path" -> setPath(value);
            case "name" -> setName(value);
            case "domain" -> setDomain(value);
            case "max-age" -> setMaxAge(Integer.parseInt(value));
            case "secure" -> setSecure(Boolean.parseBoolean(value));
            case "httponly" -> setHttpOnly(Boolean.parseBoolean(value));
            default -> sessionCookieAttributes.put(name, value);
        }
    }

    /*
       REVIEW FOR SERVLET 6
     */

    @Override
    public String getAttribute(String name) {
        String nameLowerCase = name.toLowerCase(Locale.ROOT);
        return switch (nameLowerCase) {
            case "comment" -> getComment();
            case "path" -> getPath();
            case "name" -> getName();
            case "domain" -> getDomain();
            case "max-age" -> String.valueOf(getMaxAge());
            case "secure" -> String.valueOf(isSecure());
            case "httponly" -> String.valueOf(isHttpOnly());
            default -> sessionCookieAttributes.get(name);
        };
    }
    
    @Override
    public Map<String, String> getAttributes() {
        Stream<Entry<String, String>> entriesWithGettersAndSetter = Stream.of(
            Map.entry("Comment", getComment()),
            Map.entry("Path", getPath()),
            Map.entry("Name", getName()),
            Map.entry("Domain", getDomain()),
            Map.entry("Max-Age", String.valueOf(getMaxAge())),
            Map.entry("HttpOnly", String.valueOf(isHttpOnly())),
            Map.entry("Secure", String.valueOf(isSecure()))
        );

        return Stream.concat(entriesWithGettersAndSetter, sessionCookieAttributes.entrySet().stream())
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

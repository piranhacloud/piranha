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
package cloud.piranha.session.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import cloud.piranha.webapp.impl.DefaultHttpSessionManager;
import cloud.piranha.webapp.api.WebApplication;
import java.util.UUID;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;

/**
 * The Hazelcast HTTP session manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HazelcastHttpSessionManager extends DefaultHttpSessionManager {

    /**
     * Stores the hazelcast instance.
     */
    private HazelcastInstance hazelcast;

    /**
     * Constructor.
     */
    public HazelcastHttpSessionManager() {
        this(HazelcastHttpSessionManager.class.getName());
    }

    /**
     * Constructor.
     *
     * @param name the name used for the hazelcast session map.
     */
    public HazelcastHttpSessionManager(String name) {
        super();
        Config config = new Config();
        config.setInstanceName(name);
        hazelcast = Hazelcast.newHazelcastInstance();
        sessions = hazelcast.getMap(name);
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
            HazelcastHttpSession newSession = (HazelcastHttpSession) session;
            newSession.setId(key);
            sessions.put(key, (HazelcastHttpSession) session);
            idListeners.stream().forEach((idListener) -> {
                idListener.sessionIdChanged(new HttpSessionEvent(session), oldSessionId);
            });
        } else {
            throw new IllegalStateException("No session active");
        }
        return key;
    }

    /**
     * Create the HTTP session.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @return the HTTP session.
     */
    @Override
    public synchronized HttpSession createSession(WebApplication webApplication, HttpServletRequest request) {
        String key = UUID.randomUUID().toString();
        while(sessions.containsKey(key)) {
            key = UUID.randomUUID().toString();
        }
        HazelcastHttpSession result = new HazelcastHttpSession(webApplication, key, true);
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
     * Get the session.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @param currentSessionId the current session id.
     * @return the HTTP session.
     */
    @Override
    public HttpSession getSession(WebApplication webApplication, HttpServletRequest request, String currentSessionId) {
        HazelcastHttpSession result;
        HttpServletResponse response = (HttpServletResponse) webApplication.getResponse(request);
        result = (HazelcastHttpSession) sessions.get(currentSessionId);
        result.setSessionManager(this);
        result.setServletContext(webApplication);
        Cookie cookie = new Cookie(name, currentSessionId);
        response.addCookie(cookie);
        result.setNew(false);
        return result;
    }
}

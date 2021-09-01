/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;

import cloud.piranha.webapp.api.HttpSessionManager;

/**
 * The default HttpSession.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@SuppressWarnings("deprecation")
public class DefaultHttpSession implements HttpSession {

    /**
     * Stores the attributes.
     */
    private HashMap<String, Object> attributes = new HashMap<>();

    /**
     * Stores the creation time.
     */
    private long creationTime;

    /**
     * Stores the session id.
     */
    private String id;

    /**
     * Stores the last accessed time.
     */
    private long lastAccessedTime;

    /**
     * Stores the max inactive interval.
     */
    private int maxInactiveInterval;

    /**
     * Stores if the session is new.
     */
    private boolean newFlag;

    /**
     * Stores the servlet context.
     */
    private ServletContext servletContext;

    /**
     * Stores the HTTP session manager.
     */
    private HttpSessionManager sessionManager;

    /**
     * Stores the valid flag.
     */
    private boolean valid;

    /**
     * Constructor.
     *
     * @param servletContext the servlet context.
     */
    public DefaultHttpSession(ServletContext servletContext) {
        this.id = UUID.randomUUID().toString();
        this.servletContext = servletContext;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = System.currentTimeMillis();
        this.valid = true;
    }

    /**
     * Constructor.
     *
     * @param servletContext the servlet context.
     * @param id the id.
     * @param newFlag the new flag.
     */
    public DefaultHttpSession(ServletContext servletContext, String id, boolean newFlag) {
        this.id = id;
        this.servletContext = servletContext;
        this.newFlag = newFlag;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = System.currentTimeMillis();
        this.valid = true;
    }

    @Override
    public Object getAttribute(String name) {
        verifyValid("getAttribute");
        return this.attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        verifyValid("getAttributeNames");
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public long getCreationTime() {
        verifyValid("getCreationTime");
        return this.creationTime;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public long getLastAccessedTime() {
        verifyValid("getLastAccessedTime");
        return this.lastAccessedTime;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public String[] getValueNames() {
        verifyValid("getValueNames");
        return this.attributes.keySet().toArray(new String[0]);
    }

    @Override
    public void invalidate() {
        verifyValid("invalidate");
        sessionManager.destroySession(this);
        this.valid = false;
    }

    @Override
    public boolean isNew() {
        verifyValid("isNew");
        return this.newFlag;
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        verifyValid("removeAttribute");
        sessionManager.attributeRemoved(this, name, attributes.remove(name));
    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        verifyValid("setAttribute");
        if (value != null) {
            boolean added = true;
            if (attributes.containsKey(name)) {
                added = false;
            }
            Object oldValue = attributes.put(name, value);
            if (added) {
                sessionManager.attributeAdded(this, name, value);
            } else {
                sessionManager.attributeReplaced(this, name, oldValue, value);
            }
        } else {
            removeAttribute(name);
        }
    }

    /**
     * Set the id.
     *
     * @param id the id.
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /**
     * Set the new flag.
     *
     * @param newFlag the new flag.
     */
    public void setNew(boolean newFlag) {
        verifyValid("setNew");
        this.newFlag = newFlag;
    }

    /**
     * Set the HTTP session manager.
     *
     * @param sessionManager the HTTP session manager.
     */
    public void setSessionManager(HttpSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Verify if the session is valid.
     *
     * @param methodName the method name.
     */
    private void verifyValid(String methodName) {
        if (!valid) {
            throw new IllegalStateException("Session is invalid, called by: " + methodName);
        }
    }

    /**
     * Set the last accessed time.
     *
     * @param lastAccessedTime the last accessed time.
     */
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }
}

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
package jakarta.servlet.http;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;
import jakarta.servlet.ServletContext;

/**
 * A Test HttpSession.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpSession implements HttpSession {

    /**
     * @see HttpSession#getAttribute
     */
    @Override
    public Object getAttribute(String name) {
        return null;
    }

    /**
     * @see HttpSession#getAttributeNames() 
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.emptyEnumeration();
    }

    /**
     * @see HttpSession#getCreationTime()
     */
    @Override
    public long getCreationTime() {
        return 0;
    }

    /**
     * @see HttpSession#getId() 
     */
    @Override
    public String getId() {
        return "SESSION_ID";
    }

    /**
     * @see HttpSession#getLastAccessedTime() 
     */
    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    /**
     * @see HttpSession#getMaxInactiveInterval() 
     */
    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    /**
     * @see HttpSession#getServletContext()  
     */
    @Override
    public ServletContext getServletContext() {
        return null;
    }

    /**
     * @see HttpSession#getSessionContext() 
     */
    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    /**
     * @see HttpSession#getValue(java.lang.String) 
     */
    @Override
    public Object getValue(String name) {
        return null;
    }

    /**
     * @see HttpSession#getValueNames() 
     */
    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    /**
     * @see HttpSession#invalidate() 
     */
    @Override
    public void invalidate() {
    }

    /**
     * @see HttpSession#isNew() 
     */
    @Override
    public boolean isNew() {
        return false;
    }

    /**
     * @see HttpSession#putValue(java.lang.String, java.lang.Object) 
     */
    @Override
    public void putValue(String name, Object value) {
    }

    /**
     * @see HttpSession#removeAttribute(java.lang.String) 
     */
    @Override
    public void removeAttribute(String name) {
    }

    /**
     * @see HttpSession#removeValue(java.lang.String) 
     */
    @Override
    public void removeValue(String name) {
    }

    /**
     * @see HttpSession#setAttribute(java.lang.String, java.lang.Object) 
     */
    @Override
    public void setAttribute(String name, Object value) {
    }

    /**
     * @see HttpSession#setMaxInactiveInterval(int) 
     */
    @Override
    public void setMaxInactiveInterval(int interval) {
    }
}

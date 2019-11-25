/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;

/**
 * The HttpSession API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSession {

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    public Object getAttribute(String name);

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    public Enumeration<String> getAttributeNames();

    /**
     * Get the creation time.
     *
     * @return the creation time.
     */
    public long getCreationTime();

    /**
     * Get the id.
     *
     * @return the id.
     */
    public String getId();

    /**
     * Get the last accessed time.
     *
     * @return the last accessed time.
     */
    public long getLastAccessedTime();

    /**
     * Get the max inactive interval.
     *
     * @return the max inactive interval.
     */
    public int getMaxInactiveInterval();

    /**
     * Get the Servlet context.
     *
     * @return the Servlet context.
     */
    public ServletContext getServletContext();

    /**
     * Get the HTTP session context.
     *
     * @return the HTTP session context.
     */
    @Deprecated
    public HttpSessionContext getSessionContext();

    /**
     * Get the value.
     *
     * @param name the name.
     * @return the value.
     * @deprecated
     */
    @Deprecated
    public Object getValue(String name);

    /**
     * Get the value names.
     *
     * @return the value names.
     * @deprecated
     */
    @Deprecated
    public String[] getValueNames();

    /**
     * Invalidate the HTTP session.
     */
    public void invalidate();

    /**
     * Is the HTTP session new.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isNew();

    /**
     * Put the value.
     *
     * @param name the name.
     * @param value the value.
     * @deprecated
     */
    @Deprecated
    public void putValue(String name, Object value);

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    public void removeAttribute(String name);

    /**
     * Remove the value.
     *
     * @param name the name.
     * @deprecated
     */
    @Deprecated
    public void removeValue(String name);

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param value the value.
     */
    public void setAttribute(String name, Object value);

    /**
     * Set the max inactive interval.
     *
     * @param interval the max inactive interval.
     */
    public void setMaxInactiveInterval(int interval);
}

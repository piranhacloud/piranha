/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
    public HttpSessionContext getSessionContext();

    /**
     * Get the value.
     *
     * @param name the name.
     * @return the value.
     */
    public Object getValue(String name);

    /**
     * Get the value names.
     *
     * @return the value names.
     * @deprecated
     */
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

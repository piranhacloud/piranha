/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The SessionCookieConfig API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface SessionCookieConfig {

    /**
     * Get the comment.
     *
     * @return the comment.
     */
    public String getComment();

    /**
     * Get the domain.
     *
     * @return the domain.
     */
    public String getDomain();

    /**
     * Get the max age.
     *
     * @return the max age.
     */
    public int getMaxAge();

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName();

    /**
     * Get the path.
     *
     * @return the path.
     */
    public String getPath();

    /**
     * Is HTTP-only.
     *
     * @return true if it is HTTP-only, false otherwise.
     */
    public boolean isHttpOnly();

    /**
     * Is secure.
     *
     * @return true if it is secure, false otherwise.
     */
    public boolean isSecure();

    /**
     * Set the comment.
     *
     * @param comment the comment.
     */
    public void setComment(String comment);

    /**
     * Set the domain.
     *
     * @param domain the domain.
     */
    public void setDomain(String domain);

    /**
     * Set the HTTP-only flag.
     *
     * @param httpOnly the HTTP-only flag.
     */
    public void setHttpOnly(boolean httpOnly);

    /**
     * Set the max age.
     *
     * @param maxAge the max age.
     */
    public void setMaxAge(int maxAge);

    /**
     * Set the name.
     *
     * @param name the name.
     */
    public void setName(String name);

    /**
     * Set the path.
     *
     * @param path the path.
     */
    public void setPath(String path);

    /**
     * Set the secure flag.
     *
     * @param secure the secure flag.
     */
    public void setSecure(boolean secure);
}

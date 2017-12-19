/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.io.Serializable;

/**
 * The Cookie API.
 */
public class Cookie implements Cloneable, Serializable {

    /**
     * Stores the comment.
     */
    private String comment;

    /**
     * Stores the domain.
     */
    private String domain;

    /**
     * Stores the HTTP only flag.
     */
    private boolean httpOnly;

    /**
     * Stores the max age.
     */
    private int maxAge;

    /**
     * Stores the name.
     */
    private String name;

    /**
     * Stores the path.
     */
    private String path;

    /**
     * Stores the secure flag.
     */
    private boolean secure;

    /**
     * Stores the value.
     */
    private String value;

    /**
     * Stores the version.
     */
    private int version;

    /**
     * Constructor.
     *
     * @param name the name.
     * @param value the value.
     */
    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Clones the cookie.
     *
     * @return the clone.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new RuntimeException(cnse);
        }
    }

    /**
     * Get the comment.
     *
     * @return the comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Get the domain.
     *
     * @return the domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Get the max age.
     *
     * @return the max age.
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the path.
     *
     * @return the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the secure flag.
     *
     * @return the secure flag.
     */
    public boolean getSecure() {
        return secure;
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the version.
     *
     * @return the version.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Is HTTP only.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * Set the comment.
     *
     * @param comment the comment.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Set the domain.
     *
     * @param domain the domain.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Set the HTTP only flag.
     *
     * @param httpOnly the HTTP only flag.
     */
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    /**
     * Set the max age.
     *
     * @param maxAge the max age.
     */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Set the path.
     *
     * @param path the path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Set the secure flag.
     *
     * @param secure the secure flag.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Set the version.
     *
     * @param version the version.
     */
    public void setVersion(int version) {
        this.version = version;
    }
}

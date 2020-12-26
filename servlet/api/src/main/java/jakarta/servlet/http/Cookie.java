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

import java.io.Serializable;

/**
 * The Cookie API.
 */
public class Cookie implements Cloneable, Serializable {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = -4779842497728237253L;

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
     * Stores the illegal characters for a name.
     */
    private char[] illegalCharactersForName = {',', ';', ' ', '$', '\t', '\n'};

    /**
     * Stores the max age.
     */
    private int maxAge = -1;

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
        checkValidName(name);
        this.name = name;
        this.value = value;
    }
    
    /**
     * Check if the name is valid.
     * 
     * @param name the name.
     */
    private void checkValidName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        for(int i=0; i<illegalCharactersForName.length; i++) {
            if (name.startsWith(Character.toString(illegalCharactersForName[i]))) {
                throw new IllegalArgumentException("Name cannot contain '" + illegalCharactersForName[i] + "'");
            }
        }
    }

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

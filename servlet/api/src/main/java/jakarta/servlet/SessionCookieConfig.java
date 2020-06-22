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
package jakarta.servlet;

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

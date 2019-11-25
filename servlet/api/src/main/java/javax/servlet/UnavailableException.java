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
package javax.servlet;

/**
 * The UnavailableException API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class UnavailableException extends ServletException {

    /**
     * Stores the permanent flag.
     */
    private final boolean permanent;

    /**
     * Stores the number of seconds.
     */
    private final int unavailableSeconds;

    /**
     * Constructor.
     *
     * @param message the message.
     */
    public UnavailableException(String message) {
        super(message);
        this.permanent = true;
        this.unavailableSeconds = -1;
    }

    /**
     * Constructor.
     *
     * @param message the message.
     * @param unavailableSeconds the unavailable seconds.
     */
    public UnavailableException(String message, int unavailableSeconds) {
        super(message);
        this.permanent = false;
        this.unavailableSeconds = unavailableSeconds;
    }

    /**
     * Constructor.
     *
     * @param servlet the servlet.
     * @param message the message.
     * @deprecated
     */
    @Deprecated
    public UnavailableException(Servlet servlet, String message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructor.
     *
     * @param unavailableSeconds the unavailable seconds.
     * @param servlet the servlet.
     * @param message the message.
     * @deprecated
     */
    @Deprecated
    public UnavailableException(int unavailableSeconds, Servlet servlet, String message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the servlet.
     *
     * @return the servlet
     * @deprecated
     */
    @Deprecated
    public Servlet getServlet() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the unavailableSeconds.
     *
     * @return the unavailable seconds.
     */
    public int getUnavailableSeconds() {
        return unavailableSeconds;
    }

    /**
     * Is unavailability permanent.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isPermanent() {
        return permanent;
    }
}

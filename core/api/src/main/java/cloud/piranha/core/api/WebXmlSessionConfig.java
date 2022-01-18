/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

/**
 * A session-config inside of web.xml/web-fragment.xml.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlSessionConfig {

    /**
     * Stores the cookie config.
     */
    private WebXmlSessionConfigCookieConfig cookieConfig;

    /**
     * Stores the timeout.
     */
    private int sessionTimeout;

    /**
     * Get the cookie config.
     *
     * @return the cookie config.
     */
    public WebXmlSessionConfigCookieConfig getCookieConfig() {
        return cookieConfig;
    }

    /**
     * Get the session timeout.
     *
     * @return the session timeout.
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Set the cookie config.
     * 
     * @param cookieConfig the cookie config.
     */
    public void setCookieConfig(WebXmlSessionConfigCookieConfig cookieConfig) {
        this.cookieConfig = cookieConfig;
    }

    /**
     * Set the session timeout.
     *
     * @param sessionTimeout the session timeout.
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}

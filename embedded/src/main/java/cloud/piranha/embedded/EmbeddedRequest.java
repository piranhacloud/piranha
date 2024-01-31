/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.embedded;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;

/**
 * The WebApplicationRequest class used by
 * {@link cloud.piranha.embedded.EmbeddedPiranha}
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 * @see cloud.piranha.embedded.EmbeddedPiranha
 */
public class EmbeddedRequest extends DefaultWebApplicationRequest {

    /**
     * Another Constructor.
     *
     * @param servletPath the servletPath
     *
     */
    public EmbeddedRequest(String servletPath) {
        this();
        setServletPath(servletPath);
        setContextPath("");
    }

    /**
     * Constructor.
     */
    public EmbeddedRequest() {
        super();
        this.localAddress = "127.0.0.1";
        this.localName = "localhost";
        this.localPort = 80;
        this.remoteAddr = "127.0.0.1";
        this.remoteHost = "localhost";
        this.remotePort = 18080;
    }
    
    /**
     * Get the web application.
     * 
     * @return the web application.
     */
    public WebApplication getWebApplication() {
        return webApplication;
    }
}

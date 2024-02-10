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
package cloud.piranha.http.api;

import java.io.InputStream;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Iterator;

/**
 * The HttpServerRequest API.
 *
 * <p>
 * This API delivers an abstraction over the HTTP request line, the HTTP request
 * headers and the HTTP request body.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpServerRequest {

    /**
     * Get the header.
     *
     * @param name the name of the header.
     * @return the value, or null.
     */
    String getHeader(String name);
    
    /**
     * Get the headers.
     * 
     * @param name the name of the header.
     * @return the potentially empty collection.
     */
    Iterator<String> getHeaders(String name);

    /**
     * {@return the header names}
     */
    Iterator<String> getHeaderNames();

    /**
     * {@return the input stream}
     */
    InputStream getInputStream();

    /**
     * {@return the local address}
     */
    String getLocalAddress();

    /**
     * {@return the local hostname}
     */
    String getLocalHostname();

    /**
     * {@return the local port}
     */
    int getLocalPort();

    /**
     * {@return the method}
     */
    String getMethod();

    /**
     * {@return the remote address}
     */
    String getRemoteAddress();

    /**
     * {@return the remote hostname}
     */
    String getRemoteHostname();

    /**
     * {@return the remote port}
     */
    int getRemotePort();

    /**
     * {@return the request target}
     */
    String getRequestTarget();

    /**
     * {@return the protocol}
     */
    default String getProtocol() {
        return "HTTP/1.1";
    }
    
    /**
     * Get the SSL certificates.
     * 
     * @return the SSL certificates.
     */
    default X509Certificate[] getSslCertificates() {
        return null;
    }
    
    /**
     * Get the SSL cipher suite.
     * 
     * @return the SSL cipher suite.
     */
    default String getSslCipherSuite() {
        return null;
    }
    
    /**
     * Get the SSL key size.
     * 
     * @return the SSL key size.
     */
    default int getSslKeySize() {
        return -1;
    }
    
    /**
     * Get the SSL principal.
     * 
     * @return the SSL principal.
     */
    default Principal getSslPrincipal() {
        return null;
    }

    /**
     * {@return if we are secure}
     */
    boolean isSecure();
}

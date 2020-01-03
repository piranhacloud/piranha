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
package cloud.piranha;

import cloud.piranha.api.HttpServerRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A Test HttpServerRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServerRequest implements HttpServerRequest {

    @Override
    public String getHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getQueryParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Stores the headers.
     */
    private HashMap<String, String[]> headers;

    /**
     * Stores the input stream.
     */
    private InputStream inputStream;

    /**
     * Stores the local address.
     */
    private String localAddress;

    /**
     * Stores the local hostname.
     */
    private String localHostname;

    /**
     * Stores the local port.
     */
    private int localPort;

    /**
     * Stores the method.
     */
    private String method;

    /**
     * Stores the query string.
     */
    private String queryString;

    /**
     * Stores the remote address.
     */
    private String remoteAddress;

    /**
     * Stores the remote hostname.
     */
    private String remoteHostname;

    /**
     * Stores the remote port.
     */
    private int remotePort;

    /**
     * Stores the request target.
     */
    private String requestTarget;

    /**
     * Constructor.
     */
    public TestHttpServerRequest() {
        this.headers = new HashMap<>();
        this.inputStream = new ByteArrayInputStream(new byte[0]);
        this.localAddress = "127.0.0.1";
        this.localHostname = "localhost";
        this.localPort = 8080;
        this.remoteAddress = "127.0.0.2";
        this.remoteHostname = "localhost";
        this.remotePort = 18080;
        this.requestTarget = "";
        this.method = "GET";
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Iterator<String> getHeaderNames() {
        return headers.keySet().iterator();
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     */
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Get the local address.
     *
     * @return the local address.
     */
    @Override
    public String getLocalAddress() {
        return localAddress;
    }

    /**
     * Get the local hostname.
     *
     * @return the local hostname.
     */
    @Override
    public String getLocalHostname() {
        return localHostname;
    }

    /**
     * Get the local port.
     *
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return localPort;
    }

    /**
     * Get the method.
     *
     * @return the method.
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     * Get the query string.
     *
     * @return the query string.
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * Get the remote address.
     *
     * @return the remote address.
     */
    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Get the remote hostname.
     *
     * @return the remote hostname.
     */
    @Override
    public String getRemoteHostname() {
        return remoteHostname;
    }

    /**
     * Get the remote port.
     *
     * @return the remote port.
     */
    @Override
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Get the request target.
     *
     * @return the request target.
     */
    @Override
    public String getRequestTarget() {
        return requestTarget;
    }

    /**
     * Set the method.
     *
     * @param method the method.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Set the input stream.
     *
     * @param inputStream the input stream.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Set the local address.
     *
     * @param localAddress the local address.
     */
    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    /**
     * Set the local hostname.
     *
     * @param localHostname the local hostname.
     */
    public void setLocalHostname(String localHostname) {
        this.localHostname = localHostname;
    }

    /**
     * Set the local port.
     *
     * @param localPort the local port.
     */
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * Set the query string.
     *
     * @param queryString the query string.
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Set the remote address.
     *
     * @param remoteAddress the remote address.
     */
    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /**
     * Set the remote hostname.
     *
     * @param remoteHostname the remote hostname.
     */
    public void setRemoteHostname(String remoteHostname) {
        this.remoteHostname = remoteHostname;
    }

    /**
     * Set the remote port.
     *
     * @param remotePort the remote port.
     */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    /**
     * Set the request target.
     *
     * @param requestTarget the request target.
     */
    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }
}

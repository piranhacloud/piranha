/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.servlet.server;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;
import cloud.piranha.webapp.api.WebApplicationResponse;
import cloud.piranha.webapp.api.WebApplicationServer;
import cloud.piranha.webapp.api.WebApplicationServerRequestMapper;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * A Server Servlet.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServerServlet extends HttpServlet implements WebApplicationServer, WebApplicationServerRequestMapper {
    
    /**
     * Stores the servlet configuration.
     */
    private static ServletConfig config;

    /**
     * Initialize the servlet.
     * 
     * @param config the servlet configuration.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        initialize();
        start();
    }

    /**
     * Destroy the servlet.
     */
    @Override
    public void destroy() {
        stop();
    }

    @Override
    public void addWebApplication(WebApplication webApplication) {
    }

    @Override
    public WebApplicationServerRequestMapper getRequestMapper() {
        return this;
    }

    @Override
    public void service(WebApplicationRequest request, WebApplicationResponse response) throws IOException, ServletException {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void setRequestMapper(WebApplicationServerRequestMapper requestMapper) {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public Set<String> addMapping(WebApplication webApplication, String... urlPatterns) {
        return null;
    }

    @Override
    public WebApplication findMapping(String path) {
        return null;
    }
}

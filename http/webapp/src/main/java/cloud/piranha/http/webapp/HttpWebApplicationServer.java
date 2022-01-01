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
package cloud.piranha.http.webapp;

import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerProcessorEndState;
import static cloud.piranha.http.api.HttpServerProcessorEndState.ASYNCED;
import static cloud.piranha.http.api.HttpServerProcessorEndState.COMPLETED;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.api.WebApplicationServer;
import cloud.piranha.core.api.WebApplicationServerRequestMapper;
import cloud.piranha.core.impl.CookieParser;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The default WebApplicationServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationServer implements HttpServerProcessor, WebApplicationServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(HttpWebApplicationServer.class.getName());

    /**
     * Stores the request mapper.
     */
    protected WebApplicationServerRequestMapper requestMapper;

    /**
     * Stores the web applications.
     */
    protected final Map<String, WebApplication> webApplications;

    /**
     * Constructor.
     */
    public HttpWebApplicationServer() {
        this.requestMapper = new HttpWebApplicationServerRequestMapper();
        this.webApplications = new ConcurrentHashMap<>();
    }

    /**
     * Add a context path mapping.
     *
     * @param servletContextName the servlet context name.
     * @param contextPath the context path.
     */
    public void addMapping(String servletContextName, String contextPath) {
        for (WebApplication webApp : webApplications.values()) {
            if (webApp.getServletContextName().equals(servletContextName)) {
                requestMapper.addMapping(webApp, contextPath);
                break;
            }
        }
    }

    @Override
    public void addWebApplication(WebApplication webApplication) {
        LOGGER.log(DEBUG, () -> "Adding web application with context path: " + webApplication.getContextPath());
        webApplications.put(webApplication.getContextPath(), webApplication);
        requestMapper.addMapping(webApplication, webApplication.getContextPath());
    }

    /**
     * Create the web application server request.
     *
     * @param request the HTTP server request.
     * @return the web application server request.
     */
    private WebApplicationRequest createRequest(HttpServerRequest request) {
        DefaultWebApplicationRequest applicationServerRequest = new DefaultWebApplicationRequest();
        copyHttpRequestToApplicationRequest(request, applicationServerRequest);
        applicationServerRequest.setServletPath("");

        Iterator<String> headerNames = request.getHeaderNames();
        while (headerNames.hasNext()) {
            String name = headerNames.next();
            String value = request.getHeader(name);
            request.getHeaders(name).forEachRemaining(x -> applicationServerRequest.addHeader(name, x));
            if (name.equalsIgnoreCase("Content-Type")) {
                applicationServerRequest.setContentType(value);
            }
            if (name.equalsIgnoreCase("Content-Length")) {
                applicationServerRequest.setContentLength(Integer.parseInt(value));
            }
            if (name.equalsIgnoreCase("COOKIE")) {
                applicationServerRequest.setCookies(processCookies(applicationServerRequest, value));
            }
        }

        String contextPath = applicationServerRequest.getContextPath();
        if (contextPath != null) {
            String jsessionid = ";jsessionid=";
            int indexJsessionid = contextPath.indexOf(jsessionid);
            if (indexJsessionid > -1) {
                applicationServerRequest.setContextPath(contextPath.substring(0, indexJsessionid));
                if (!applicationServerRequest.isRequestedSessionIdFromCookie()) {
                    applicationServerRequest.setRequestedSessionIdFromURL(true);
                    applicationServerRequest.setRequestedSessionId(contextPath.substring(indexJsessionid + jsessionid.length()));
                }
            }
        }
        
        if (request.isSecure()) {
            applicationServerRequest.setScheme("https");
        }

        return applicationServerRequest;
    }

    private Cookie[] processCookies(DefaultWebApplicationRequest result, String cookiesValue) {
        Cookie[] cookies = CookieParser.parse(cookiesValue);

        Stream.of(cookies)
                .filter(x -> "JSESSIONID".equals(x.getName()))
                .findAny()
                .ifPresent(cookie -> {
                    result.setRequestedSessionIdFromCookie(true);
                    result.setRequestedSessionId(cookie.getValue());
                });

        return cookies;
    }

    private void copyHttpRequestToApplicationRequest(HttpServerRequest httpRequest, DefaultWebApplicationRequest applicationRequest) {
        applicationRequest.setLocalAddr(httpRequest.getLocalAddress());
        applicationRequest.setLocalName(httpRequest.getLocalHostname());
        applicationRequest.setLocalPort(httpRequest.getLocalPort());
        applicationRequest.setRemoteAddr(httpRequest.getRemoteAddress());
        applicationRequest.setRemoteHost(httpRequest.getRemoteHostname());
        applicationRequest.setRemotePort(httpRequest.getRemotePort());
        applicationRequest.setServerName(httpRequest.getLocalHostname());
        applicationRequest.setServerPort(httpRequest.getLocalPort());
        applicationRequest.setMethod(httpRequest.getMethod());
        applicationRequest.setContextPath(httpRequest.getRequestTarget());
        applicationRequest.setQueryString(httpRequest.getQueryString());
        applicationRequest.setInputStream(httpRequest.getInputStream());
        applicationRequest.setProtocol(httpRequest.getProtocol());
    }

    /**
     * Create the web application server response.
     *
     * @param httpResponse the HTTP server response.
     * @return the web application server response.
     */
    public DefaultWebApplicationResponse createResponse(HttpServerResponse httpResponse) {
        DefaultWebApplicationResponse applicationResponse = new DefaultWebApplicationResponse();
        applicationResponse.setUnderlyingOutputStream(httpResponse.getOutputStream());

        applicationResponse.setResponseCloser(() -> {
            try {
                httpResponse.closeResponse();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, () -> "IOException when flushing the underlying async output stream", ioe);
            }
        });

        return applicationResponse;
    }

    /**
     * {@return the request mapper}
     */
    @Override
    public WebApplicationServerRequestMapper getRequestMapper() {
        return requestMapper;
    }

    @Override
    public void initialize() {
        LOGGER.log(DEBUG, "Starting initialization of {0} web application(s)", webApplications.size());
        for(WebApplication webApp : webApplications.values()) {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.initialize();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
        LOGGER.log(DEBUG, "Finished initialization of {0} web application(s)", webApplications.size());
    }

    @Override
    public HttpServerProcessorEndState process(HttpServerRequest request, HttpServerResponse response) {
        HttpServerProcessorEndState state = COMPLETED;
        try {
            DefaultWebApplicationRequest serverRequest = (DefaultWebApplicationRequest) createRequest(request);
            DefaultWebApplicationResponse serverResponse = createResponse(response);
            service(serverRequest, serverResponse);
            if (serverRequest.isAsyncStarted()) {
                state = ASYNCED;
            }
        } catch (Throwable t) {
            LOGGER.log(ERROR, "An error occurred while processing the request", t);
        }
        return state;
    }

    /**
     * Service method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void service(WebApplicationRequest request, WebApplicationResponse response) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (requestUri == null) {
            response.sendError(500);
            return;
        }

        WebApplication webApplication = requestMapper.findMapping(requestUri);
        if (webApplication == null) {
            response.sendError(404);
            return;
        }

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());
            String contextPath = webApplication.getContextPath();
            request.setContextPath(contextPath);
            request.setServletPath(requestUri.substring(contextPath.length()));
            request.setWebApplication(webApplication);
            response.setWebApplication(webApplication);

            webApplication.service(request, response);

            // Make sure the request is fully read wrt parameters (if any still)
            request.getParameterMap();
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    /**
     * Set the request mapper.
     *
     * @param requestMapper the request mapper.
     */
    @Override
    public void setRequestMapper(WebApplicationServerRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    /**
     * Start the server.
     */
    @Override
    public void start() {
        LOGGER.log(DEBUG, "Starting HTTP web application server");
        webApplications.values().forEach(webApp -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.start();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
        LOGGER.log(DEBUG, "Started HTTP web application server");
    }

    /**
     * Stop the server.
     */
    @Override
    public void stop() {
        LOGGER.log(DEBUG, "Stopping HTTP web application server");
        webApplications.values().forEach(webApp -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.stop();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
        LOGGER.log(DEBUG, "Stopped HTTP web application server");
    }
}

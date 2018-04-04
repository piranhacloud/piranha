/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * The WebApplicationServer API.
 *
 * @param <R> the request type.
 * @param <S> the response type.
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationServer<R, S> {

    /**
     * Add a web application.
     *
     * @param webApplication the web application to add.
     */
    void addWebApplication(WebApplication webApplication);

    /**
     * Get the request mapper.
     *
     * @return the request mapper.
     */
    WebApplicationServerRequestMapper getRequestMapper();

    /**
     * Service the request and response.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    void service(R request, S response) throws IOException, ServletException;

    /**
     * Initialize the server.
     */
    void initialize();

    /**
     * Set the request mapper.
     *
     * @param requestMapper the request mapper.
     */
    void setRequestMapper(WebApplicationServerRequestMapper requestMapper);

    /**
     * Start the server.
     */
    void start();

    /**
     * Stop the server.
     */
    void stop();
}

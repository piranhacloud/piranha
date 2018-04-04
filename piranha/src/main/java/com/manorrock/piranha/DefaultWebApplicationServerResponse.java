/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import com.manorrock.httpserver.HttpServerResponse;

/**
 * The default WebApplicationServerResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerResponse extends DefaultHttpServletResponse {

    /**
     * Stores the response.
     */
    private final HttpServerResponse response;

    /**
     * Constructor.
     *
     * @param response the response.
     */
    public DefaultWebApplicationServerResponse(HttpServerResponse response) {
        this.response = response;
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return -1;
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
    }

    /**
     * Set the buffer size.
     *
     * @param bufferSize the buffer size.
     */
    @Override
    public void setBufferSize(int bufferSize) {
    }
}

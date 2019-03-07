/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import com.manorrock.piranha.api.MultiPartManager;
import com.manorrock.piranha.api.WebApplicationRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;

/**
 * The default multi-part manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultMultiPartManager implements MultiPartManager {

    /**
     * Defines the BOUNDARY constant.
     */
    private static final String BOUNDARY = "boundary=";

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultMultiPartManager.class.getName());

    /**
     * Stores the boundary string.
     */
    private String boundary;

    /**
     * Stores the parsed flag.
     */
    private boolean parsed = false;

    /**
     * Stores the parts.
     */
    private final Map<String, Part> parts = new HashMap<>();

    /**
     * Get the parts.
     *
     * @param request the request.
     * @return the parts.
     */
    @Override
    public Collection<Part> getParts(WebApplicationRequest request) {
        if (!parsed) {
            parse(request);
        }
        return Collections.unmodifiableCollection(parts.values());
    }

    /**
     * Get the part.
     *
     * @param request the request.
     * @param name the name of the part.
     * @return the part, or null if not found.
     */
    @Override
    public Part getPart(WebApplicationRequest request, String name) {
        if (!parsed) {
            parse(request);
        }
        return parts.get(name);
    }

    /**
     * Parse the request.
     *
     * @param request the web application request.
     */
    private void parse(WebApplicationRequest request) {
        synchronized (request) {
            if (!parsed) {
                parsed = true;
                parseBoundary(request);
                parseParts(request);
            }
        }
    }

    /**
     * Parse the boundary.
     *
     * @param request the web application request.
     */
    private void parseBoundary(WebApplicationRequest request) {
        String contentType = request.getContentType();
        int index = contentType.lastIndexOf(BOUNDARY);
        boundary = contentType.substring(index + BOUNDARY.length());
        if (boundary.charAt(0) == '"') {
            index = boundary.lastIndexOf('"');
            boundary = boundary.substring(1, index);
        }
        boundary = "--" + boundary;
    }

    /**
     * Parse a line.
     *
     * @param inputStream the input stream.
     */
    private String parseLine(InputStream inputStream) {
        return null;
    }

    /**
     * Parse a part.
     *
     * @param request the web application request.
     * @param inputStream the input stream.
     */
    private Part parsePart(WebApplicationRequest request, InputStream inputStream) {
        HashMap<String, String> headers = parsePartHeaders(request, inputStream);
        return null;
    }

    /**
     * Parse the parts.
     *
     * @param request the web application request.
     */
    private void parseParts(WebApplicationRequest request) {
        try {
            InputStream inputStream = request.getInputStream();
            parsePreamble(request, inputStream);
            Part part = parsePart(request, inputStream);
            while (part != null) {
                parts.put(part.getName(), part);
                part = parsePart(request, inputStream);
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "An I/O error occurred while parsing parts", ioe);
        }
    }

    /**
     * Parse a preamble.
     *
     * @param request the web application request.
     * @param inputStream the input stream.
     */
    private void parsePreamble(WebApplicationRequest request, InputStream inputStream) {
        String line = parseLine(inputStream);
        while (line != null) {
            if (line.startsWith(boundary)) {
                break;
            }
            line = parseLine(inputStream);
        }
    }

    /**
     * Parse the part headers.
     *
     * @param request the web application request.
     * @param inputStream the input stream.
     */
    private HashMap<String, String> parsePartHeaders(WebApplicationRequest request, InputStream inputStream) {
        return new HashMap<>();
    }
}

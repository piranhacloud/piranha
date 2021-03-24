/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import java.lang.System.Logger.Level;
import java.util.Collection;
import java.util.Collections;
import java.lang.System.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import cloud.piranha.webapp.api.MultiPartManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;

/**
 * The default MultiPartManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultMultiPartManager implements MultiPartManager {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(DefaultMultiPartManager.class.getName());

    /**
     * Get the parts.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @return the parts.
     * @throws ServletException when the request is not a multipart/form-data
     * request.
     */
    @Override
    public Collection<Part> getParts(WebApplication webApplication,
            WebApplicationRequest request) throws ServletException {
        LOGGER.log(Level.DEBUG, "Getting parts for request: {0}", request);
        return Collections.emptyList();
    }

    /**
     * Get the part.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @param name the name of the part.
     * @return the part, or null if not found.
     * @throws ServletException when the request is not a multipart/form-data
     * request.
     */
    @Override
    public Part getPart(WebApplication webApplication,
            WebApplicationRequest request, String name) throws ServletException {
        LOGGER.log(Level.DEBUG, "Getting part: {0} for request: {1}", new Object[]{name, request});
        return null;
    }
}

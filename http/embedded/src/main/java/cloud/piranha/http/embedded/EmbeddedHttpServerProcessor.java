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
package cloud.piranha.http.embedded;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import cloud.piranha.http.webapp.HttpWebApplicationRequest;
import cloud.piranha.http.webapp.HttpWebApplicationResponse;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;

/**
 * The Piranha Embedded HttpServerProcessor.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedHttpServerProcessor implements HttpServerProcessor {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(EmbeddedHttpServerProcessor.class.getPackageName());

    /**
     * Stores the Piranha Embedded instance.
     */
    private final EmbeddedPiranha piranha;

    /**
     * Constructor.
     *
     * @param piranha the Piranha Embedded instance.
     */
    public EmbeddedHttpServerProcessor(EmbeddedPiranha piranha) {
        this.piranha = piranha;
    }

    @Override
    public boolean process(HttpServerRequest request, HttpServerResponse response) {
        try ( HttpWebApplicationRequest servletRequest = new HttpWebApplicationRequest(request)) {
            HttpWebApplicationResponse servletResponse = new HttpWebApplicationResponse(response);
            piranha.service(servletRequest, servletResponse);
            servletResponse.flush();
        } catch (Throwable t) {
            LOGGER.log(ERROR, "An error occurred while processing the request", t);
        }
        return false;
    }
}

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
package cloud.piranha.http.webapp;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerProcessorEndState;
import static cloud.piranha.http.api.HttpServerProcessorEndState.COMPLETED;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;

/**
 * The Web Application HttpServerProcessor.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationServerProcessor implements HttpServerProcessor {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(HttpWebApplicationServerProcessor.class.getPackageName());

    /**
     * Stores the Piranha instance.
     */
    private final Piranha piranha;

    /**
     * Constructor.
     *
     * @param piranha the Piranha instance.
     */
    public HttpWebApplicationServerProcessor(Piranha piranha) {
        this.piranha = piranha;
    }

    @Override
    public HttpServerProcessorEndState process(HttpServerRequest request, HttpServerResponse response) {
        try (HttpWebApplicationRequest servletRequest = new HttpWebApplicationRequest(request)) {
            HttpWebApplicationResponse servletResponse = new HttpWebApplicationResponse(response);
            piranha.service(servletRequest, servletResponse);
            servletResponse.flush();
        } catch (Throwable t) {
            LOGGER.log(ERROR, "An error occurred while processing the request", t);
        }
        return COMPLETED;
    }
}

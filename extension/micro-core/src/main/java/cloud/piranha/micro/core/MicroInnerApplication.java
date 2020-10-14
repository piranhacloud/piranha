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
package cloud.piranha.micro.core;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.ServletException;

import cloud.piranha.appserver.impl.DefaultWebApplicationServerRequest;
import cloud.piranha.appserver.impl.DefaultWebApplicationServerResponse;
import cloud.piranha.webapp.api.WebApplication;

public class MicroInnerApplication implements Consumer<Map<String, Object>> {

    /**
     * Stores the web application.
     */
    private final WebApplication webApplication;

    /**
     * Constructor.
     * 
     * @param webApplication the web application.
     */
    public MicroInnerApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    @Override
    public void accept(Map<String, Object> requestMap) {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());

            webApplication.service(copyMapToApplicationRequest(requestMap), copyMapToApplicationResponse(requestMap));

        } catch (ServletException | IOException e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    @SuppressWarnings("unchecked")
    private DefaultWebApplicationServerRequest copyMapToApplicationRequest(Map<String, Object> requestMap) {
        DefaultWebApplicationServerRequest applicationRequest = DefaultWebApplicationServerRequest.fromMap(requestMap);
        applicationRequest.setWebApplication(webApplication);

        return applicationRequest;
    }

    private DefaultWebApplicationServerResponse copyMapToApplicationResponse(Map<String, Object> requestMap) {
        return DefaultWebApplicationServerResponse.fromMap(requestMap);
    }

}

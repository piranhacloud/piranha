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
package cloud.piranha.appserver.impl;

import java.io.OutputStream;
import java.util.Map;

import cloud.piranha.appserver.api.WebApplicationServerResponse;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;

/**
 * The default WebApplicationServerResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerResponse extends DefaultWebApplicationResponse implements WebApplicationServerResponse {

    private Runnable responseCloser;

    @Override
    public Runnable getResponseCloser() {
        return responseCloser;
    }

    public void setResponseCloser(Runnable responseCloser) {
        this.responseCloser = responseCloser;
    }

    @Override
    public void closeAsyncResponse() {
        responseCloser.run();
    }

    public static DefaultWebApplicationServerResponse fromMap(Map<String, Object> requestMap) {
        DefaultWebApplicationServerResponse applicationResponse = new DefaultWebApplicationServerResponse();
        applicationResponse.setUnderlyingOutputStream((OutputStream) requestMap.get("UnderlyingOutputStream"));
        applicationResponse.setResponseCloser((Runnable) requestMap.get("ResponseCloser"));

        return applicationResponse;
    }


}

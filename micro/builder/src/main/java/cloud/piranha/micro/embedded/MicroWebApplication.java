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
package cloud.piranha.micro.embedded;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import cloud.piranha.naming.thread.ThreadInitialContextFactory;
import cloud.piranha.policy.thread.ThreadPolicy;
import cloud.piranha.webapp.api.WebApplicationRequest;
import cloud.piranha.webapp.api.WebApplicationResponse;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * A Piranha Micro web application.
 * 
 * @author Arjan Tijms
 */
public class MicroWebApplication extends DefaultWebApplication {

    /**
     * Stores the deployed application.
     */
    private Consumer<Map<String, Object>> deployedApplication;

    /**
     * Get the deployed application.
     * 
     * @return the deployed application.
     */
    public Consumer<Map<String, Object>> getDeployedApplication() {
        return deployedApplication;
    }

    /**
     * Set the deployed application.
     * 
     * @param deployedApplication the deployed application.
     */
    public void setDeployedApplication(Consumer<Map<String, Object>> deployedApplication) {
        this.deployedApplication = deployedApplication;
    }

    /**
     * Service the request.
     * 
     * @param request the request.
     * @param response the resposne.
     */
    @Override
    public void service(ServletRequest request, ServletResponse response) {
        try {
            ThreadPolicy.setPolicy(getPolicyManager().getPolicy());
            ThreadInitialContextFactory.setInitialContext(getNamingManager().getContext());
            deployedApplication.accept(copyApplicationRequestToMap((WebApplicationRequest) request, (WebApplicationResponse) response));
        } finally {
            ThreadPolicy.removePolicy();
            ThreadInitialContextFactory.removeInitialContext();
        }
    }

    /**
     * Copy the request and response to a map.
     * 
     * @param applicationRequest the web application request.
     * @param applicationResponse the web application response.
     * @return the map.
     */
    private Map<String, Object> copyApplicationRequestToMap(WebApplicationRequest applicationRequest, WebApplicationResponse applicationResponse) {
        Map<String, Object> requestValues = new HashMap<>();
        requestValues.put("request", applicationRequest);
        requestValues.put("response", applicationResponse);
        return requestValues;
    }
}

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
package cloud.piranha.extension.standard.policy;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import java.security.NoSuchAlgorithmException;
import java.security.Policy;

/**
 * The WebApplicationExtension that is responsible for setting up the proper
 * Policy instance so it can be made available during web application
 * initialization and subsequently during request processing as well as
 * delivering listeners to set/remove the Policy from the current thread.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class StandardPolicyExtension implements WebApplicationExtension {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(StandardPolicyExtension.class.getName());

    /**
     * Configure the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        try {
            LOGGER.log(DEBUG, "Configuring webapplication");
            Policy policy = Policy.getInstance("JavaPolicy", null);
            webApplication.setAttribute(Policy.class.getName(), policy);
            StandardPolicyThreadLocal.setPolicy(policy);
            webApplication.addListener(StandardPolicyServletContextListener.class.getName());
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(WARNING, "Error setting up Policy", ex);
        }
    }
}

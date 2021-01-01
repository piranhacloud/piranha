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
package cloud.piranha.extension.micro;

import static java.util.Arrays.asList;

import cloud.piranha.security.jakarta.JakartaSecurityAllInitializer;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationExtension;
import cloud.piranha.webapp.scinitializer.ServletContainerInitializerExtension;
import cloud.piranha.webapp.webannotation.WebAnnotationInitializer;
import cloud.piranha.webapp.webxml.WebXmlInitializer;

/**
 * The default {@link WebApplicationExtension} used to configure a web
 * application for Piranha Micro.
 *
 * @see WebApplicationExtension
 */
public class MicroExtension implements WebApplicationExtension {

    /**
     * Configure the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.addInitializer(new WebAnnotationInitializer());
        webApplication.addInitializer(new JakartaSecurityAllInitializer());
        new ServletContainerInitializerExtension(
            true,
            asList("org.glassfish.soteria.servlet.SamRegistrationInstaller")).
        configure(webApplication);
    }
}

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
package cloud.piranha.extension.micro;

import static java.util.Arrays.asList;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationExtensionContext;
import cloud.piranha.extension.apache.fileupload.ApacheMultiPartExtension;
import cloud.piranha.extension.async.AsyncExtension;
import cloud.piranha.extension.eclipselink.EclipseLinkExtension;
import cloud.piranha.extension.exousia.AuthorizationPostInitializer;
import cloud.piranha.extension.localeencoding.LocaleEncodingExtension;
import cloud.piranha.extension.mimetype.MimeTypeExtension;
import cloud.piranha.extension.naming.NamingExtension;
import cloud.piranha.extension.policy.PolicyExtension;
import cloud.piranha.extension.scinitializer.ServletContainerInitializerExtension;
import cloud.piranha.extension.security.jakarta.JakartaSecurityExtension;
import cloud.piranha.extension.security.servlet.ServletSecurityManagerExtension;
import cloud.piranha.extension.servletannotations.ServletAnnotationsExtension;
import cloud.piranha.extension.tempdir.TempDirExtension;
import cloud.piranha.extension.transact.TransactExtension;
import cloud.piranha.extension.wasp.WaspExtension;
import cloud.piranha.extension.webxml.WebXmlExtension;
import cloud.piranha.extension.welcomefile.WelcomeFileExtension;

/**
 * The {@link WebApplicationExtension} used to configure a web application for
 * Piranha Micro.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MicroExtension implements WebApplicationExtension {

    @Override
    public void extend(WebApplicationExtensionContext context) {
        // Servlet
        context.add(AsyncExtension.class);                      // Async
        context.add(LocaleEncodingExtension.class);             // locale-encoding
        context.add(MimeTypeExtension.class);                   // mimetype
        context.add(PolicyExtension.class);                     // Policy
        context.add(TempDirExtension.class);                    // TEMPDIR
        context.add(WelcomeFileExtension.class);                // welcome-file
        context.add(ServletSecurityManagerExtension.class);     // Servlet security
        context.add(ApacheMultiPartExtension.class);            // Upload
        context.add(WebXmlExtension.class);                     // web.xml
        context.add(ServletAnnotationsExtension.class);         // Servlet annotations
        context.add(NamingExtension.class);                     // Naming (JNDI)
        context.add(TransactExtension.class);                   // Jakarta Transaction
        context.add(JakartaSecurityExtension.class);            // Jakarta Security (includes Jakarta CDI)
        context.add(WaspExtension.class);                       // Jakarta Pages
        context.add(EclipseLinkExtension.class);                // Jakarta Persistence
    }

    @Override
    public void configure(WebApplication webApplication) {
        new ServletContainerInitializerExtension(
            true, asList("org.glassfish.soteria.servlet.SamRegistrationInstaller"))
            .configure(webApplication);
        webApplication.addInitializer(new AuthorizationPostInitializer());
    }
}

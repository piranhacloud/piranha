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

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationExtensionContext;
import cloud.piranha.extension.apache.fileupload.ApacheMultiPartExtension;
import cloud.piranha.extension.herring.HerringExtension;
import cloud.piranha.extension.policy.PolicyExtension;
import cloud.piranha.extension.security.jakarta.JakartaSecurityAllInitializer;
import cloud.piranha.extension.security.servlet.ServletSecurityManagerExtension;
import cloud.piranha.extension.standard.async.StandardAsyncExtension;
import cloud.piranha.extension.standard.localeencoding.StandardLocaleEncodingExtension;
import cloud.piranha.extension.standard.mimetype.StandardMimeTypeExtension;
import cloud.piranha.extension.standard.scinitializer.StandardServletContainerInitializerExtension;
import cloud.piranha.extension.standard.servletannotations.StandardServletAnnotationsExtension;
import cloud.piranha.extension.standard.tempdir.StandardTempDirExtension;
import cloud.piranha.extension.standard.webxml.StandardWebXmlExtension;
import cloud.piranha.extension.standard.welcomefile.StandardWelcomeFileExtension;
import cloud.piranha.extension.wasp.WaspInitializer;
import static java.util.Arrays.asList;

/**
 * The default {@link WebApplicationExtension} used to configure a web application for Piranha Micro.
 *
 * @see WebApplicationExtension
 */
public class MicroExtension implements WebApplicationExtension {

    @Override
    public void extend(WebApplicationExtensionContext context) {
        context.add(StandardAsyncExtension.class);
        context.add(StandardLocaleEncodingExtension.class);
        context.add(StandardMimeTypeExtension.class);
        context.add(StandardTempDirExtension.class);
        context.add(StandardWelcomeFileExtension.class);
        context.add(ServletSecurityManagerExtension.class);
        context.add(ApacheMultiPartExtension.class);
        context.add(StandardWebXmlExtension.class);
        context.add(StandardServletAnnotationsExtension.class);
        context.add(HerringExtension.class);
        context.add(PolicyExtension.class);
    }

    @Override
    public void configure(WebApplication webApplication) {
        webApplication.addInitializer(new JakartaSecurityAllInitializer());
        webApplication.addInitializer(new WaspInitializer());
        new StandardServletContainerInitializerExtension(
            true, asList("org.glassfish.soteria.servlet.SamRegistrationInstaller"))
            .configure(webApplication);
    }
}

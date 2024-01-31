/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.webprofile;

import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationExtensionContext;
import cloud.piranha.extension.annotationscan.AnnotationScanExtension;
import cloud.piranha.extension.annotationscan.classfile.ClassfileAnnotationScanExtension;
import cloud.piranha.extension.apache.fileupload.ApacheMultiPartExtension;
import cloud.piranha.extension.datasource.DefaultDatasourceExtension;
import cloud.piranha.extension.eclipselink.EclipseLinkExtension;
import cloud.piranha.extension.naming.NamingExtension;
import cloud.piranha.extension.policy.PolicyExtension;
import cloud.piranha.extension.scinitializer.ServletContainerInitializerExtension;
import cloud.piranha.extension.security.servlet.ServletSecurityExtension;
import cloud.piranha.extension.security.servlet.ServletSecurityManagerExtension;
import cloud.piranha.extension.servletannotations.ServletAnnotationsExtension;
import cloud.piranha.extension.tempdir.TempDirExtension;
import cloud.piranha.extension.transact.TransactExtension;
import cloud.piranha.extension.wasp.WaspExtension;
import cloud.piranha.extension.wasp.WaspJspManagerExtension;
import cloud.piranha.extension.webxml.WebXmlExtension;
import cloud.piranha.extension.welcomefile.WelcomeFileExtension;
import cloud.piranha.extension.weld.WeldExtension;

/**
 * The extension that delivers the extensions for Piranha Micro/Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebProfileExtension implements WebApplicationExtension {

    @Override
    public void extend(WebApplicationExtensionContext context) {
        context.add(PolicyExtension.class);                         // JavaPolicy
        context.add(TempDirExtension.class);                        // TEMPDIR
        context.add(WelcomeFileExtension.class);                    // welcome-file
        context.add(ServletSecurityManagerExtension.class);         // SecurityManager
        context.add(ApacheMultiPartExtension.class);                // Servlet Part API support
        context.add(WaspJspManagerExtension.class);                 // addJspFile
        context.add(NamingExtension.class);                         // Naming (JNDI)
        context.add(WebXmlExtension.class);                         // web.xml
        context.add(getAnnotationScanExtensionClass());                 // Annotation scanning
        context.add(ServletAnnotationsExtension.class);             // Servlet annotations
        context.add(WeldExtension.class);
        context.add(DefaultDatasourceExtension.class);              // Default data source
        context.add(TransactExtension.class);                       // OmniFish Transaction
        context.add(ServletSecurityExtension.class);                // Security implementation
        context.add(WaspExtension.class);                           // WaSP
        context.add(EclipseLinkExtension.class);                    // Jakarta Persistence
        context.add(ServletContainerInitializerExtension.class);    // ServletContainerInitializer
    }

    private static Class<? extends WebApplicationExtension> getAnnotationScanExtensionClass() {
        if (System.getProperty(ClassfileAnnotationScanExtension.EXPERIMENTAL_PROPERTY) != null) {
            return ClassfileAnnotationScanExtension.class;   // Annotation scanning using the new Classfile API
        }
        return AnnotationScanExtension.class;  // Annotation scanning
    }

}

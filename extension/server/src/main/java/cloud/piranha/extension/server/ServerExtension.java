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
package cloud.piranha.extension.server;

import cloud.piranha.extension.annotationscan.AnnotationScanExtension;
import cloud.piranha.extension.herring.HerringExtension;
import cloud.piranha.extension.scinitializer.ServletContainerInitializerExtension;
import cloud.piranha.extension.tempdir.TempDirExtension;
import cloud.piranha.extension.wasp.WaspExtension;
import cloud.piranha.extension.webannotations.WebAnnotationsExtension;
import cloud.piranha.extension.webxml.WebXmlExtension;
import cloud.piranha.webapp.api.WebApplicationExtension;
import cloud.piranha.webapp.api.WebApplicationExtensionContext;

/**
 * The ServerExtension delivers the default extensions for Piranha Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServerExtension implements WebApplicationExtension {

    @Override
    public void extend(WebApplicationExtensionContext context) {
        context.add(HerringExtension.class);
        context.add(AnnotationScanExtension.class);
        context.add(WaspExtension.class);
        context.add(WebXmlExtension.class);
        context.add(WebAnnotationsExtension.class);
        context.add(TempDirExtension.class);
        context.add(ServletContainerInitializerExtension.class);
    }
}

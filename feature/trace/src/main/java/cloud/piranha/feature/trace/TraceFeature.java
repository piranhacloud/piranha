/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.feature.trace;

import cloud.piranha.core.impl.DefaultFilterChain;
import cloud.piranha.core.impl.DefaultInvocationFinder;
import cloud.piranha.core.impl.DefaultServletRequestManager;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationInputStream;
import cloud.piranha.core.impl.DefaultWebApplicationOutputStream;
import cloud.piranha.extension.fileupload.FileUploadMultiPartManager;
import cloud.piranha.feature.impl.DefaultFeature;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.http.webapp.HttpWebApplicationServerRequestMapper;
import static java.util.logging.Level.FINEST;
import java.util.logging.Logger;

/**
 * The Trace feature.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TraceFeature extends DefaultFeature {

    /**
     * Constructor.
     */
    public TraceFeature() {
    }

    @Override
    public void init() {
        boolean enabled = Boolean.parseBoolean("CLOUD_PIRANHA_FEATURE_TRACE_ENABLED");
        if (!enabled) {
            enabled = Boolean.parseBoolean(
                    System.getProperty("cloud.piranha.feature.trace.enabled", "false"));
        }
        if (enabled) {
            Logger logger = Logger.getLogger(DefaultFilterChain.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(DefaultInvocationFinder.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(DefaultServletRequestManager.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(DefaultWebApplication.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(DefaultWebApplication.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(DefaultWebApplicationInputStream.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(DefaultWebApplicationOutputStream.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(HttpWebApplicationServer.class.getName());
            logger.setLevel(FINEST);
            logger = Logger.getLogger(HttpWebApplicationServerRequestMapper.class.getName());
            logger.setLevel(FINEST);
            
            /*
             * The following uses a try / catch as the extension is optionally
             * available depending on the runtime.
             */
            try {
                logger = Logger.getLogger(FileUploadMultiPartManager.class.getName());
                logger.setLevel(FINEST);
            } catch(Throwable t) {
                // swallow up
            }
        }
    }
}

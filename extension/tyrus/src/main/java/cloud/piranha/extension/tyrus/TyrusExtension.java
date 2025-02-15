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
package cloud.piranha.extension.tyrus;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.TRACE;
import org.glassfish.tyrus.servlet.TyrusServletContainerInitializer;

/**
 * The extension that delivers Tyrus to Piranha.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TyrusExtension implements WebApplicationExtension {

    /**
     * Stores the property used for enabling/disabling Tyrus.
     */
    public static final String TYRUS_ENABLED_PROPERTY
            = "cloud.piranha.extension.tyrus.TyrusExtension.enabled";

    /**
     * Stores the property used for ignore initializers.
     */
    private static final String IGNORE_INITIALIZER_PROPERTY
            = "cloud.piranha.extension.scinitializer.ServletContainerInitializerExtension.ignoreInitializers";

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(TyrusExtension.class.getName());

    /**
     * Constructor.
     */
    public TyrusExtension() {
    }

    /**
     * Configure the extension.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        LOGGER.log(DEBUG, "Configuring Tyrus extension");

        if (!Boolean.parseBoolean(System.getProperty(TYRUS_ENABLED_PROPERTY, "true"))) {
            LOGGER.log(TRACE, "Disabling Tyrus extension");

            String ignoredInitializers = System.getProperty(IGNORE_INITIALIZER_PROPERTY, "");
            if (ignoredInitializers.equals("")) {
                ignoredInitializers = TyrusServletContainerInitializer.class.getName();
            }

            System.setProperty(IGNORE_INITIALIZER_PROPERTY, ignoredInitializers);
        }
    }
}

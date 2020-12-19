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
package cloud.piranha.servlet4.scinitializer;

import static java.util.Collections.emptyList;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;

import cloud.piranha.servlet4.webapp.WebApplication;
import cloud.piranha.servlet4.webapp.WebApplicationExtension;

/**
 * The WebApplication extension that enables ServletContainerInitializer
 * processing.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletContainerInitializerExtension implements WebApplicationExtension {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(
            ServletContainerInitializerExtension.class.getPackage().getName());

    /**
     * Stores the exclude existing initializers flag.
     */
    private final boolean excludeExistingInitializers;
    
    /**
     * Stores the initializers to be ignored.
     */
    private final List<String> ignoreInitializers;

    /**
     * Constructor.
     */
    public ServletContainerInitializerExtension() {
        this(false, emptyList());
    }

    /**
     * Constructor.
     * 
     * @param excludeExistingInitializers the exclude existing initializers flag.
     * @param ignoreInitializers ignore the given initializers.
     */
    public ServletContainerInitializerExtension(boolean excludeExistingInitializers, List<String> ignoreInitializers) {
        this.excludeExistingInitializers = excludeExistingInitializers;
        this.ignoreInitializers = new ArrayList<>(ignoreInitializers);
    }

    /**
     * Configure the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        LOGGER.log(FINER, "Starting ServletContainerInitializer processing");
        ServiceLoader<ServletContainerInitializer> serviceLoader = ServiceLoader.load(
                ServletContainerInitializer.class, webApplication.getClassLoader());

        for (ServletContainerInitializer initializer : serviceLoader) {
            LOGGER.log(FINE, () -> "Adding initializer: " + initializer.getClass().getName());

            if (shouldAdd(webApplication, initializer)) {
                webApplication.addInitializer(initializer);
            }
        }

        if (this.getClass().getModule().isNamed()) {
            // We are running in a modular environment,
            // the providers from modules aren't available in the webApplication classloader
            serviceLoader = ServiceLoader.load(ServletContainerInitializer.class);
            for (ServletContainerInitializer initializer : serviceLoader) {
                LOGGER.log(FINE, () -> "Adding initializer: " + initializer.getClass().getName());

                if (shouldAdd(webApplication, initializer)) {
                    webApplication.addInitializer(initializer);
                }
            }
        }

        LOGGER.log(FINER, "Finished ServletContainerInitializer processing");
    }

    private boolean shouldAdd(WebApplication webApplication, ServletContainerInitializer initializer) {
        if (isIgnored(initializer)) {
            return false;
        }

        if (!excludeExistingInitializers) {
            return true;
        }

        return !containsInstance(webApplication, initializer);
    }

    private boolean containsInstance(WebApplication webApplication, ServletContainerInitializer initializer) {
        return webApplication.getInitializers()
                             .stream()
                             .anyMatch(e -> e.getClass().equals(initializer.getClass()));
    }

    private boolean isIgnored(ServletContainerInitializer initializer) {
        return ignoreInitializers
                             .stream()
                             .anyMatch(e -> e.equals(initializer.getClass().getName()));
    }


}

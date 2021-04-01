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

import org.jboss.shrinkwrap.api.Archive;

import cloud.piranha.micro.loader.MicroConfiguration;
import cloud.piranha.micro.loader.MicroOuterDeployer;
import cloud.piranha.naming.thread.ThreadInitialContextFactory;
import cloud.piranha.policy.thread.ThreadPolicy;

/**
 * Builder for an embedded Piranha instance based on Piranha Micro
 *
 * @author Arjan Tijms
 */
public class MicroEmbeddedPiranhaBuilder {

    /**
     * Object containing all configuration settings for Piranha Micro
     */
    private MicroConfiguration configuration;

    /**
     * Application archive that will be executed by Piranha Micro
     */
    private Archive<?> archive;

    /**
     * Sets the configuration for Piranha Micro
     *
     * @param configuration the configuration
     * @return instance of this builder
     */
    public MicroEmbeddedPiranhaBuilder configuration(MicroConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    /**
     * Set the application archive that will be loaded and executed by Piranha
     * Micro
     *
     * @param archive the archive to be executed
     * @return instance of this builder
     */
    public MicroEmbeddedPiranhaBuilder archive(Archive<?> archive) {
        this.archive = archive;
        return this;
    }

    /**
     * Builds an embedded Piranha Micro instance and deploys the archive set by
     * this builder to it.
     *
     * @return the newly created Piranha Micro instance
     */
    public MicroEmbeddedPiranha buildAndStart() {
        MicroEmbeddedPiranha microEmbeddedPiranha = new MicroEmbeddedPiranha();
        MicroWebApplication microWebApplication = microEmbeddedPiranha.getWebApplication();
        if (configuration == null) {
            // Use default config is nothing set
            configuration = new MicroConfiguration();
            configuration.setHttpStart(false);
        }

        if (configuration.getRoot() != null) {
            // If an explicit root is set, use it. Otherwise use the default.
            microWebApplication.setContextPath(configuration.getRoot());
        }

        try {
            ThreadPolicy.setPolicy(microWebApplication.getPolicyManager().getPolicy());
            ThreadInitialContextFactory.setInitialContext(microWebApplication.getNamingManager().getContext());

            microWebApplication.setDeployedApplication(
                    new MicroOuterDeployer(configuration.postConstruct()).deploy(archive).getDeployedApplication());

            if (!configuration.isHttpStart()) {
                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(microWebApplication.getClassLoader());
                    microWebApplication.initialize();
                    microWebApplication.start();
                } finally {
                    Thread.currentThread().setContextClassLoader(oldClassLoader);
                }
            }

            return microEmbeddedPiranha;
        } finally {
            ThreadPolicy.removePolicy();
            ThreadInitialContextFactory.removeInitialContext();
        }
    }

}

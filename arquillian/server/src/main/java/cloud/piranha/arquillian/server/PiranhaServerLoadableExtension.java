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
package cloud.piranha.arquillian.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.core.spi.LoadableExtension;

import cloud.piranha.micro.shrinkwrap.loader.MicroConfiguration;

/**
 * The extension sets up the Arquillian Server Connector
 *
 * @author Arjan Tijms
 *
 */
public class PiranhaServerLoadableExtension implements LoadableExtension {

    // Defines the deployable container used; PiranhaServerDeployableContainer.class
    // This is the actual "connector" that controls Piranha.

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(DeployableContainer.class, PiranhaServerDeployableContainer.class);
    }

    // Defines the configuration class used: PiranhaServerContainerConfiguration.class
    // Defines the protocol used: "Servlet 5.0"

    public abstract static class PiranhaServerContainerBase implements DeployableContainer<PiranhaServerContainerConfiguration> {

        @Override
        public Class<PiranhaServerContainerConfiguration> getConfigurationClass() {
            return PiranhaServerContainerConfiguration.class;
        }

        @Override
        public ProtocolDescription getDefaultProtocol() {
            return new ProtocolDescription("Servlet 5.0");
        }
    }

    // Defines the configuration class to be essentially the same as MicroConfiguration.class

    public static class PiranhaServerContainerConfiguration extends MicroConfiguration implements ContainerConfiguration {

        /**
         * Highest port number that we'll try
         */
        private static final int MAX_PORT_NUMBER = 65535;

        /**
         * Lowest port number that we'll try
         */
        private static final int MIN_PORT_NUMBER = 1;

        /**
         * Stores whether to automatically find an available port.
         */
        private boolean autoPort;

        /**
         * Helper class for finding a free port
         */
        private PortFinder portFinder = new PortFinder();

        /**
         * Initializes values from system properties or to default values
         */
        public PiranhaServerContainerConfiguration() {
            super();
            this.autoPort = Boolean.valueOf(System.getProperty("piranha.autoPort", "true"));
        }

        /**
         * Just calls {@link #postConstruct()}
         * @throws ConfigurationException - never thrown
         */
        @Override
        public void validate() throws ConfigurationException {
            postConstruct();
        }

        /**
         * Initializes configuration after all configured values were loaded.
         * Computes generated configuration, e.g. a free port for autoPort
         *
         * @return
         */
        @Override
        public PiranhaServerContainerConfiguration postConstruct() {
            if (isAutoPort()) {
                setPort(portFinder.findFreePort(getPort()));
            }
            super.postConstruct();
            return this;
        }

        /**
         * @return whether automatically assign port
         */
        public boolean isAutoPort() {
            return autoPort;
        }

        /**
         * @param autoPort Whether automatically assign port
         */
        public void setAutoPort(boolean autoPort) {
            this.autoPort = autoPort;
        }

        private class PortFinder {

            /**
             * Find a random free local port. It's guaranteed that the returned port will be always random.
             * The initialPort parameter is only used as a base for the randomization and isn't returned
             * even if it's free. This is to prevent returning the same port if this method is called in parallel.
             *
             * @param initialPort The initial port to start searching from.
             *                    If 0, the default port 8080 will be used.
             * @return free port tha
             */
            public int findFreePort(int initialPort) {
                int portCandidate = 8080;
                int numberOfAttempts = 100;
                boolean foundFreePort = false;
                final Random random = new Random();

                if (initialPort > 0) {
                    portCandidate = initialPort;
                }

                do {
                    portCandidate += random.nextInt(100);
                    foundFreePort = isFreePort(portCandidate);
                    numberOfAttempts--;
                    if (portCandidate > MAX_PORT_NUMBER) {
                        portCandidate = portCandidate - MAX_PORT_NUMBER - 1 + MIN_PORT_NUMBER;
                    }
                } while (!foundFreePort || numberOfAttempts <= 0);

                if (foundFreePort) {
                    return portCandidate;
                } else {
                    throw new RuntimeException("No free port found!");
                }
            }

            private boolean isFreePort(int portCandidate) {
                try ( Socket clientSocket = new Socket("localhost", portCandidate)) {
                } catch (IOException clientEx) {
                    // we cannot connect, the port isn't occupied
                    try {
                        // check whether this process can bind to the port - listen to it and then close it
                        try (ServerSocket listeningSocket = new ServerSocket(portCandidate)) {}
                        return true;
                    } catch (IOException listenEx) {
                    }
                }
                return false;
            }

        }

    }
}

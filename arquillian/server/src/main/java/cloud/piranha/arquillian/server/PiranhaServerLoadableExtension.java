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

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import cloud.piranha.micro.shrinkwrap.loader.MicroConfiguration;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

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

        @Override
        public void start() throws LifecycleException {
            // We don't start Piranha separately. Start and Deploy is one step.
        }

        @Override
        public void deploy(Descriptor descriptor) throws DeploymentException {
            // We don't deploy by descriptor (and neither does Arquillian it seems)

        }

        @Override
        public void undeploy(Descriptor descriptor) throws DeploymentException {
            // We don't undeploy by descriptor (and neither does Arquillian it seems)
        }

        @Override
        public void stop() throws LifecycleException {
            // We don't stop Piranha separately. Stop and Undeploy is one step.
        }

    }

    // Defines the configuration class to be essentially the same as MicroConfiguration.class
    public static class PiranhaServerContainerConfiguration extends MicroConfiguration implements ContainerConfiguration {

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

        @Override
        public void validate() throws ConfigurationException {
            postConstruct();
        }

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

            private int findFreePort(int initialPort) {
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
                } while (!foundFreePort || numberOfAttempts <= 0 || portCandidate > 65000);

                if (foundFreePort) {
                    return portCandidate;
                } else {
                    throw new RuntimeException("No free port found!");
                }
            }

            private boolean isFreePort(int portCandidate) {
                try ( Socket s = new Socket("localhost", portCandidate)) {
                } catch (IOException ex) {
                    return true;
                }
                return false;
            }

        }

    }
}

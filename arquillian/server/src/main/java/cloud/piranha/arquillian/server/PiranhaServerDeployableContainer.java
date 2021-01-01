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
package cloud.piranha.arquillian.server;

import java.util.logging.Logger;

import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.shrinkwrap.api.Archive;

import cloud.piranha.arquillian.server.PiranhaServerLoadableExtension.PiranhaServerContainerConfiguration;
import cloud.piranha.micro.MicroDeployOutcome;
import cloud.piranha.micro.MicroOuterDeployer;

/**
 * The Piranha Micro Arquillian connector.
 *
 * <p>
 * This connector will start up an embedded Piranha Micro runtime in an isolated class loader for every application that is
 * deployed.
 *
 * @author Arjan Tijms
 *
 */
public class PiranhaServerDeployableContainer extends PiranhaServerLoadableExtension.PiranhaServerContainerBase {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PiranhaServerDeployableContainer.class.getName());

    /**
     * Stores the configuration.
     */
    private PiranhaServerContainerConfiguration configuration;
    
    /**
     * Stores the outer deployer.
     */
    private MicroOuterDeployer microOuterDeployer;


    @Override
    public void setup(PiranhaServerContainerConfiguration configuration) {
        this.configuration = configuration;
        configuration.validate();
    }

    @Override
    public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {
        LOGGER.info("Starting Piranha Micro");

        microOuterDeployer = new MicroOuterDeployer(configuration);

        MicroDeployOutcome deployOutcome = microOuterDeployer.deploy(archive);

        HTTPContext httpContext = new HTTPContext("localhost", configuration.getPort());

        String contextRoot = configuration.getRoot();
        if (contextRoot == null) {
            contextRoot = "/";
        }

        for (String servletName : deployOutcome.getDeployedServlets()) {
            httpContext.add(new Servlet(servletName, contextRoot));
        }

        ProtocolMetaData protocolMetaData = new ProtocolMetaData();
        protocolMetaData.addContext(httpContext);

        return protocolMetaData;
    }

    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {
        if (microOuterDeployer != null) {
            LOGGER.info("Stopping Piranha Micro");
            microOuterDeployer.stop();
        }
    }



}

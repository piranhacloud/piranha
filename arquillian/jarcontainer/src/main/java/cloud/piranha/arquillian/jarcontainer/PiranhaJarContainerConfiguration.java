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
package cloud.piranha.arquillian.jarcontainer;

import static java.lang.System.Logger.Level.INFO;
import me.alexpanov.net.FreePortFinder;
import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;

/**
 * The Piranha JAR container configuration.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class PiranhaJarContainerConfiguration implements ContainerConfiguration {
    
    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(PiranhaJarContainerConfiguration.class.getName());
    
    /**
     * Stores the HTTP port.
     */
    private Integer httpPort;
    
    /**
     * Stores the JVM arguments.
     */
    private String jvmArguments = "";

    /**
     * Stores the protocol.
     */
    private String protocol = "Servlet 6.0";
    
    /**
     * Get the HTTP port.
     * 
     * @return the HTTP port.
     */
    public int getHttpPort() {
        if (httpPort == null) {
            httpPort = FreePortFinder.findFreeLocalPort();
        }
        return httpPort;
    }
    
    /**
     * Get the JVM arguments.
     * 
     * @return the JVM arguments.
     */
    public String getJvmArguments() {
        return jvmArguments;
    }

    /**
     * Get the protocol.
     * 
     * @return the protocol.
     */
    public String getProtocol() {
        return protocol;
    }
    
    /**
     * Set the HTTP port.
     * 
     * @param httpPort the HTTP port.
     */
    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }
    
    /**
     * Set the JVM arguments.
     * 
     * @param jvmArguments the JVM arguments.
     */
    public void setJvmArguments(String jvmArguments) {
        this.jvmArguments = jvmArguments;
    }

    /**
     * Set the protocol.
     * 
     * @param protocol the protocol.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void validate() throws ConfigurationException {
        LOGGER.log(INFO, "Using HTTP Port: {0}", getHttpPort());
        LOGGER.log(INFO, "Using JVM arguments: {0}", getJvmArguments());
        LOGGER.log(INFO, "Using protocol: {0}", getProtocol());
    }
}

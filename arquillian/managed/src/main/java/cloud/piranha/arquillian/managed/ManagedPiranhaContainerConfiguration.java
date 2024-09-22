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
package cloud.piranha.arquillian.managed;

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;

import me.alexpanov.net.FreePortFinder;

import static java.lang.System.Logger.Level.INFO;

/**
 * The managed Piranha container configuration.
 *
 * The following system properties can be used to configure the Piranha
 * container from the command-line.
 * <table>
 * <caption>System properties</caption>
 * <tr>
 * <th>name</th>
 * <th>value</th>
 * <th>notes</th>
 * </tr>
 * <tr>
 * <td>piranha.debug</td>
 * <td>The boolean to start the Piranha process in debugging mode</td>
 * <td>not enabled by default</td>
 * </tr>
 * <tr>
 * <td>piranha.httpPort</td>
 * <td>The integer to select the HTTP port to use for the Piranha process</td>
 * <td>if not set an unused port will be automatically chosen</td>
 * </tr>
 * <tr>
 * <td>piranha.jvmArguments</td>
 * <td>The string with JVM arguments to pass to the Piranha process</td>
 * <td>no additional JVM arguments are passed by default</td>
 * </tr>
 * <tr>
 * <td>piranha.protocol</td>
 * <td>The string with the Arquillian protocol to use when talking to the
 * Piranha process</td>
 * <td>set to 'Servlet 6.0' by default</td>
 * </tr>
 * <tr>
 * <td>piranha.suspend</td>
 * <td>the boolean to start the Piranha process in suspend mode</td>
 * <td>not enabled by default</td>
 * </tr>
 * </table>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ManagedPiranhaContainerConfiguration implements ContainerConfiguration {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(ManagedPiranhaContainerConfiguration.class.getName());

    /**
     * Stores the debug flag.
     */
    private boolean debug = Boolean.parseBoolean(System.getProperty("piranha.debug", "false"));

    /**
     * Stores the HTTP port.
     */
    private Integer httpPort = System.getProperty("piranha.httpPort") != null ? Integer.valueOf(System.getProperty("piranha.httpPort")) : null;

    /**
     * Stores the JVM arguments.
     */
    private String jvmArguments = System.getProperty("piranha.jvmArguments", "");

    /**
     * Stores the Arquillian protocol.
     */
    private String protocol = System.getProperty("piranha.protocol", "Servlet 6.0");

    /**
     * Stores the suspend flag.
     */
    private boolean suspend = Boolean.parseBoolean(System.getProperty("piranha.suspend", "false"));

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

    /**
     * Is the debug flag set?
     *
     * @return true if the debug flag is set, false otherwise.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set the debug flag.
     *
     * @param debug the debug flag.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Is the suspend flag set?
     *
     * @return true if the suspend flag is set, false otherwise.
     */
    public boolean isSuspend() {
        return suspend;
    }

    /**
     * Set the suspend flag.
     *
     * @param suspend the suspend flag.
     */
    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    @Override
    public void validate() throws ConfigurationException {
        LOGGER.log(INFO, """

            Using HTTP Port:     {0}
            Using JVM arguments: {1}
            Using protocol:      {2}
            Using debug:         {3}
            Using suspend:       {4}

            """,
                getHttpPort() + "",
                getJvmArguments(),
                getProtocol(),
                isDebug(),
                isSuspend());
    }
}

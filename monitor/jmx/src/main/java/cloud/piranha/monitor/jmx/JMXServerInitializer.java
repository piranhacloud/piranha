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
package cloud.piranha.monitor.jmx;

import cloud.piranha.api.Piranha;
import java.lang.management.ManagementFactory;
import java.util.Set;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * Bootstraps the JMX server.
 *
 * <p>
 * This class initializes the JMX server and registers the PiranhaInfo MBean if
 * and only if it finds the appropriate attribute in the ServletContext.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class JMXServerInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(JMXServerInitializer.class.getName());

    /**
     * Defines the attribute name for the MicroPiranha reference.
     */
    private static final String MICRO_PIRANHA = "cloud.piranha.micro.MicroPiranha";

    /**
     * Defines the attribute name for the ServerPiranha reference.
     */
    private static final String SERVER_PIRANHA = "cloud.piranha.server.ServerPiranha";

    /**
     * On startup.
     *
     * @param annotatedClasses the annotated class.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> annotatedClasses,
            ServletContext servletContext) throws ServletException {
        LOGGER.log(FINER, "Initializing JMX server");

        Piranha piranha = null;

        if (servletContext.getAttribute(SERVER_PIRANHA) != null) {
            piranha = (Piranha) servletContext.getAttribute(SERVER_PIRANHA);
        }

        if (servletContext.getAttribute(MICRO_PIRANHA) != null) {
            piranha = (Piranha) servletContext.getAttribute(MICRO_PIRANHA);
        }

        if (piranha != null) {
            try {
                MBeanServer server = ManagementFactory.getPlatformMBeanServer();
                ObjectName name = new ObjectName("cloud.piranha:type=PiranhaInfo");
                PiranhaInfo bean = new PiranhaInfo(piranha);
                server.registerMBean(bean, name);
            } catch (InstanceAlreadyExistsException | MBeanRegistrationException
                    | MalformedObjectNameException | NotCompliantMBeanException ex) {
                LOGGER.log(WARNING, "A problem registering PiranhaInfo MBean", ex);
            }
        } else {
            LOGGER.log(WARNING, "Unable to determine Piranha version, "
                    + "not registering PiranhaInfo MBean");
        }

        LOGGER.log(FINER, "Initialized JMX server");
    }
}

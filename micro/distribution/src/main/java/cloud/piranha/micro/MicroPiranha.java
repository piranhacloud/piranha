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
package cloud.piranha.micro;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import cloud.piranha.micro.loader.MicroConfiguration;
import cloud.piranha.micro.loader.MicroOuterDeployer;

/**
 * The micro version of Piranha.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 */
public class MicroPiranha implements Runnable {

    /**
     * The HTTP port on which Piranha accepts requests
     */
    private int port = 8080;

    /**
     * Stores the HTTP server implementation.
     */
    private String httpServer = "impl";

    /**
     * Stores the WAR file.
     */
    private Archive<?> archive;

    /**
     * Stores the outer deployer.
     */
    private MicroOuterDeployer outerDeployer;

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        MicroPiranha runner = new MicroPiranha();
        runner.configure(arguments);
        runner.run();
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Configure.
     *
     * @param arguments the arguments.
     */
    public void configure(String[] arguments) {
        File warFile = null;
        File explodedDir = null;

        if (arguments.length > 0) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].equals("--war")) {
                    warFile = new File(arguments[i + 1]);
                }
                
                if (arguments[i].equals("--webapp")) {
                    explodedDir = new File(arguments[i + 1]);
                }
                
                if (arguments[i].equals("--port")) {
                    port = Integer.parseInt(arguments[i + 1]);
                }

                if (arguments[i].equals("--http")) {
                    httpServer = arguments[i + 1];
                }

                if (arguments[i].equals("--ssl")) {
                    System.setProperty("piranha.http.ssl", "true");
                }
            }
        }

        if (warFile != null) {
            archive = ShrinkWrap.create(ZipImporter.class, warFile.getName()).importFrom(warFile).as(WebArchive.class);
        } else if (explodedDir != null) {
            archive = ShrinkWrap.create(ExplodedImporter.class, explodedDir.getName()).importDirectory(explodedDir).as(WebArchive.class);
        } else {
            archive = ShrinkWrap.create(WebArchive.class);
        }
    }

    /**
     * Start method.
     */
    @Override
    public void run() {
        MicroConfiguration configuration = new MicroConfiguration();
        configuration.setPort(port);
        configuration.setHttpServer(httpServer);

        outerDeployer = new MicroOuterDeployer(configuration.postConstruct());
        outerDeployer.deploy(archive);
    }

    /**
     * Stop method.
     */
    public void stop() {
        if (outerDeployer != null) {
            outerDeployer.stop();
        }
    }
}

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger.Level;
import static java.lang.System.Logger.Level.WARNING;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;

/**
 * The Piranha JAR container.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class PiranhaJarContainer implements DeployableContainer<PiranhaJarContainerConfiguration> {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(PiranhaJarContainer.class.getName());

    /**
     * Stores the PID filename.
     */
    private static final String PID_FILENAME = "tmp/piranha.pid";

    /**
     * Stores the java.io.tmpdir constant.
     */
    private static final String TMP_DIR = "java.io.tmpdir";

    /**
     * Stores the 'Unable to create directories' message.
     */
    private static final String UNABLE_TO_CREATE_DIRECTORIES = "Unable to create directories";

    /**
     * Stores the configuration.
     */
    private PiranhaJarContainerConfiguration configuration;

    /**
     * Stores the local repository directory.
     */
    private File localRepositoryDir = new File(System.getProperty("user.home"), ".m2/repository");

    /**
     * Stores the process.
     */
    private Process process;

    /**
     * Default constructor.
     */
    public PiranhaJarContainer() {
    }

    @Override
    public Class<PiranhaJarContainerConfiguration> getConfigurationClass() {
        return PiranhaJarContainerConfiguration.class;
    }

    @Override
    public void setup(PiranhaJarContainerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start() throws LifecycleException {
        LOGGER.log(Level.INFO, "Starting server");
    }

    @Override
    public void stop() throws LifecycleException {
        LOGGER.log(Level.INFO, "Stopping server");

        /*
         * Delete the PID file.
         */
        File runtimeDirectory = new File(System.getProperty(TMP_DIR));
        File pidFile = new File(runtimeDirectory, PID_FILENAME);
        if (pidFile.exists()) {
            try {
                Files.delete(pidFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Unable to delete PID file", ioe);
            }
        }
    }

    @Override
    public ProtocolDescription getDefaultProtocol() {
        return new ProtocolDescription(configuration.getProtocol());
    }

    @Override
    public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {
        LOGGER.log(Level.INFO, "Deploying - " + archive.getName());

        ProtocolMetaData metadata = new ProtocolMetaData();

        try {
            /*
             * Export the Archive into a WAR file.
             */
            File runtimeDirectory = new File(System.getProperty(TMP_DIR));
            File warFile = new File(runtimeDirectory, toWarFilename(archive));
            archive.as(ZipExporter.class).exportTo(warFile, true);

            /*
             * Copy runtime JAR into the runtime diectory.
             */
            String version = determineVersionToUse();
            File piranhaJarFile = getPiranhaJarFile(version);
            copyPiranhaCoreProfileJarFile(runtimeDirectory, piranhaJarFile);

            /*
             * Start Piranha.
             */
            startPiranhaCoreProfile(runtimeDirectory, warFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        HTTPContext httpContext = new HTTPContext("localhost", configuration.getHttpPort());
        httpContext.add(new Servlet(
                "ArquillianServletRunnerEE9",
                archive.getName().substring(0, archive.getName().lastIndexOf("."))));
        metadata.addContext(httpContext);
        return metadata;
    }

    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {
        LOGGER.log(Level.INFO, "Undeploying - " + archive.getName());

        /*
         * Delete the PID file.
         */
        File runtimeDirectory = new File(System.getProperty(TMP_DIR));
        File pidFile = new File(runtimeDirectory, PID_FILENAME);
        if (pidFile.exists()) {
            try {
                Files.delete(pidFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Error deleting PID file", ioe);
            }
        }

        /*
         * Wait for 5 seconds.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
        }

        /*
         * Destroy the process forcibly if it is still running.
         */
        try {
            if (process.isAlive()) {
                LOGGER.log(Level.WARNING, 
                        "Process for {0} still alive, destroying forcibly", 
                        archive.getName());
                process.destroyForcibly().waitFor();
            }
        } catch (InterruptedException ie) {
        }

        /*
         * Delete the WAR file.
         */
        File warFile = new File(runtimeDirectory, toWarFilename(archive));
        warFile.delete();
    }

    /**
     * Creates a WAR filename for the archive.
     *
     * @param archive the archive.
     * @return the WAR filename.
     */
    private String toWarFilename(Archive<?> archive) {
        String warFilename = archive.getName();

        if (archive.getName() == null || archive.getName().equals("")) {
            warFilename = "ROOT.war";
        }

        return warFilename;
    }

    /**
     * Get the Piranha JAR file.
     *
     * @param version the version.
     * @return the zip file.
     * @throws IOException when an I/O error occurs.
     */
    private File getPiranhaJarFile(String version) throws IOException {
        URL downloadUrl = createMavenCentralArtifactUrl(
                "cloud.piranha.dist",
                "piranha-dist-coreprofile",
                version,
                "jar"
        );

        String artifactPath = createArtifactPath(
                "cloud.piranha.dist",
                "piranha-dist-coreprofile",
                version,
                "jar"
        );

        File zipFile = new File(localRepositoryDir, artifactPath);
        if (!zipFile.exists() && !zipFile.getParentFile().mkdirs()) {
            LOGGER.log(Level.WARNING, UNABLE_TO_CREATE_DIRECTORIES);
        }

        try (InputStream inputStream = downloadUrl.openStream()) {
            Files.copy(inputStream,
                    zipFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (FileNotFoundException fnfe) {
            LOGGER.log(Level.WARNING, "Could not download JAR file, defaulting back to local Maven repository");
        }

        return new File(localRepositoryDir, artifactPath);
    }

    /**
     * Create artifact path.
     *
     * @param groupId the groupId.
     * @param artifactId the artifactId.
     * @param version the version
     * @param type the type.
     */
    private String createArtifactPath(String groupId, String artifactId,
            String version, String type) {
        String artifactPathFormat = "%s/%s/%s/%s-%s.%s";
        return String.format(artifactPathFormat,
                convertGroupIdToPath(groupId),
                artifactId,
                version,
                artifactId,
                version,
                type.toLowerCase());
    }

    /**
     * Convert the groupId to path.
     *
     * @param groupId the groupId.
     * @return the path.
     */
    private String convertGroupIdToPath(String groupId) {
        return groupId.replace('.', '/');
    }

    /**
     * Create the Maven central artifact URL
     *
     * @param groupId the groupId.
     * @param artifactId the artifactId.
     * @param version the version
     * @param type the type.
     * @return the URL.
     * @throws IOException when an I/O error occurs.
     */
    private URL createMavenCentralArtifactUrl(String groupId, String artifactId,
            String version, String type) throws IOException {
        return new URL("https://repo1.maven.org/maven2/" + createArtifactPath(groupId, artifactId, version, type));
    }

    /**
     * Determine what version of Piranha to use.
     *
     * @return the version.
     */
    private String determineVersionToUse() {
        return getClass().getPackage().getImplementationVersion();
    }

    /**
     * Start and wait for Piranha Core Profile.
     *
     * @param runtimeDirectory the runtime directory.
     * @param warFilename the WAR filename.
     */
    private void startPiranhaCoreProfile(
            File runtimeDirectory,
            File warFilename)
            throws IOException {

        List<String> commands = new ArrayList<>();
        commands.add("java");
        List<String> jvmARgs = Arrays.asList(configuration.getJvmArguments().split("\\s+"));
        if (!jvmARgs.isEmpty()) {
            jvmARgs.forEach(s -> {
                if (s != null && !s.trim().equals("")) {
                    commands.add(s);
                }
            });
        }
        commands.add("-jar");
        commands.add("piranha-dist-coreprofile.jar");
        commands.add("--http-port");
        commands.add(Integer.toString(configuration.getHttpPort()));
        commands.add("--war-file");
        commands.add(warFilename.getName());
        commands.add("--write-pid");

        process = new ProcessBuilder()
                .directory(runtimeDirectory)
                .command(commands)
                .start();

        File pidFile = new File(runtimeDirectory, PID_FILENAME);
        int count = 0;
        LOGGER.log(Level.INFO, "Waiting for Piranha to be ready ");
        while (!pidFile.exists()) {
            try {
                Thread.sleep(500);
                count++;
                LOGGER.log(Level.INFO, ".");
            } catch (InterruptedException ie) {
            }
            if (count == 80) {
                LOGGER.log(Level.WARNING, "Warning, PID file not seen!");
                break;
            }
        }
        LOGGER.log(Level.INFO, "");

        LOGGER.log(Level.INFO, "Running application from directory: " + runtimeDirectory);
        LOGGER.log(Level.INFO, "Application is available at: http://localhost:"
                + Integer.toString(configuration.getHttpPort())
                + "/" + warFilename.getName().substring(0, warFilename.getName().lastIndexOf(".")));
    }

    /**
     * Copy the Piranha Core Profile JAR file.
     *
     * @param runtimeDirectory the runtime directory.
     * @param zipFile the zip file.
     */
    private void copyPiranhaCoreProfileJarFile(File runtimeDirectory, File zipFile) throws IOException {
        if (!runtimeDirectory.exists() && !runtimeDirectory.mkdirs()) {
            System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
        }
        Files.copy(zipFile.toPath(),
                Path.of(runtimeDirectory + "/piranha-dist-coreprofile.jar"),
                REPLACE_EXISTING);
    }
}

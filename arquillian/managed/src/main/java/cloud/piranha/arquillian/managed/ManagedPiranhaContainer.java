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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;

import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * The Managed Piranha container.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ManagedPiranhaContainer implements DeployableContainer<ManagedPiranhaContainerConfiguration> {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(ManagedPiranhaContainer.class.getName());

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
    private ManagedPiranhaContainerConfiguration configuration;

    /**
     * Stores the local repository directory.
     */
    private File localRepositoryDir = new File(System.getProperty("user.home"), ".m2/repository");

    /**
     * Stores the Piranha process.
     */
    private Process process;

    /**
     * Default constructor.
     */
    public ManagedPiranhaContainer() {
    }

    @Override
    public Class<ManagedPiranhaContainerConfiguration> getConfigurationClass() {
        return ManagedPiranhaContainerConfiguration.class;
    }

    @Override
    public void setup(ManagedPiranhaContainerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ProtocolDescription getDefaultProtocol() {
        return new ProtocolDescription(configuration.getProtocol());
    }

    @Override
    public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {
        LOGGER.log(INFO, "Deploying " + archive.getName());

        ProtocolMetaData metadata = new ProtocolMetaData();

        try {
            /*
             * Export the Archive into a WAR file.
             */
            String warFileName = toWarFilename(archive);

            File runtimeDirectory = new File(System.getProperty(TMP_DIR), toAppName(warFileName));
            runtimeDirectory.mkdirs();

            File warFile = new File(runtimeDirectory, warFileName);

            archive.as(ZipExporter.class).exportTo(warFile, true);

            /*
             * Copy runtime JAR into the runtime diectory.
             */
            String version = determineVersionToUse();
            File piranhaJarFile = getPiranhaJarFile(version);
            copyPiranhaJarFile(runtimeDirectory, piranhaJarFile);

            /*
             * Start Piranha.
             */
            startPiranha(runtimeDirectory, warFile);
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
        LOGGER.log(INFO, "Undeploying " + archive.getName());

        /*
         * Delete the PID file.
         */
        File runtimeDirectory = new File(System.getProperty(TMP_DIR), toAppName(archive));
        File pidFile = new File(runtimeDirectory, PID_FILENAME);
        if (pidFile.exists()) {
            try {
                Files.delete(pidFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Error deleting PID file", ioe);
            }
        }

        /*
         * Wait for 5 minutes at the most.
         */
        if (process != null && process.isAlive()) {
            try {
                LOGGER.log(INFO, "Waiting for Piranha to be shutdown");

                long startTime = System.currentTimeMillis();
                process.waitFor(30, TimeUnit.SECONDS);
                Long finishTime = System.currentTimeMillis();

                LOGGER.log(INFO, "Piranha has shutdown\n It took {0} milliseconds", finishTime - startTime);
            } catch (InterruptedException ie) {
                LOGGER.log(WARNING, "Piranha did not shutdown within time alloted");
                LOGGER.log(WARNING, "Destroying Piranha process forcibly");
                process.destroyForcibly();
            }
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

        if (isEmpty(archive.getName())) {
            warFilename = "ROOT.war";
        }

        return warFilename;
    }

    private String toAppName(String warFileName) {
        return warFileName.substring(0, warFileName.lastIndexOf("."));
    }

    private String toAppName(Archive<?> archive) {
        return toAppName(toWarFilename(archive));
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
            LOGGER.log(WARNING, UNABLE_TO_CREATE_DIRECTORIES);
        }

        try (InputStream inputStream = downloadUrl.openStream()) {
            Files.copy(inputStream,
                    zipFile.toPath(),
                    REPLACE_EXISTING);
        } catch (IOException fnfe) {
            LOGGER.log(WARNING, "Could not download JAR file, defaulting back to local Maven repository");
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
    private String createArtifactPath(String groupId, String artifactId, String version, String type) {
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
    private URL createMavenCentralArtifactUrl(String groupId, String artifactId, String version, String type) throws IOException {
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
     * Start Piranha.
     *
     * @param runtimeDirectory the runtime directory.
     * @param warFile the WAR filename.
     */
    private void startPiranha(File runtimeDirectory, File warFile) throws IOException, DeploymentException {
        List<String> commands = new ArrayList<>();
        StringBuilder classpath = new StringBuilder();
        commands.add("java");
        String[] jvmArgs = configuration.getJvmArguments().split("\\s+");
        if (jvmArgs.length > 0) {
            for(int i=0; i<jvmArgs.length; i++) {
                if (jvmArgs[i] != null && !jvmArgs[i].trim().equals("")) {
                    commands.add(jvmArgs[i]);
                }
                if (jvmArgs[i] != null && jvmArgs[i].trim().equals("-cp")) {
                    // ignore this one.
                }
                if (i > 0 && jvmArgs[i] != null && jvmArgs[i-1].trim().equals("-cp")) {
                    classpath.append(jvmArgs[i].trim()).append(File.pathSeparatorChar);
                }
            }
        }

        if (configuration.isDebug()) {
            commands.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=localhost:9009");
        }

        if (configuration.isSuspend()) {
            commands.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:9009");
        }

        if (classpath.isEmpty()) {
            commands.add("-jar");
            commands.add("piranha-dist-coreprofile.jar");
        } else {
            commands.add("-cp");
            commands.add(classpath.toString() + "piranha-dist-coreprofile.jar");
            commands.add("cloud.piranha.dist.coreprofile.CoreProfilePiranhaMain");
        }
        commands.add("--http-port");
        commands.add(Integer.toString(configuration.getHttpPort()));
        commands.add("--war-file");
        commands.add(warFile.getName());
        commands.add("--write-pid");

        String appName = toAppName(warFile.getName());
        String appURL = "http://localhost:" + Integer.toString(configuration.getHttpPort()) + "/" + appName;
        File logFile = new File(runtimeDirectory, appName + ".log");

        LOGGER.log(INFO,
            """


            Starting Piranha

            Directory:  {0}
            Log:        {1}
            URL:        {2}


            """,

            runtimeDirectory,
            logFile.getAbsolutePath(),
            appURL);

        process = new ProcessBuilder()
                .directory(runtimeDirectory)
                .command(commands)
                .redirectErrorStream(true)
                .redirectOutput(logFile)
                .start();

        File pidFile = new File(runtimeDirectory, PID_FILENAME);
        int count = 0;
        LOGGER.log(INFO, "Waiting for Piranha to be ready");
        while (!pidFile.exists() && process.isAlive()) {
            try {
                Thread.sleep(100);
                count++;
            } catch (InterruptedException ie) {
            }

            if (configuration.isSuspend()) {
                if (count % 500 == 0) {
                    LOGGER.log(INFO, "Still waiting (infinite, because suspend on port 9009)");
                }
                continue;
            }

            if (count % 20 == 0) {
                LOGGER.log(INFO, "Still waiting... ({0} of {1})", (count / 20), (1200 / 20));
            }

            if (count == 1200) {
                LOGGER.log(WARNING, "Warning, PID file not seen!");
                break;
            }
        }

        if (!process.isAlive()) {
            LOGGER.log(WARNING, "Piranha terminated during startup.");

            String msg = "Cannot start Piranha. \n";
            if (process.getErrorStream() != null) {
                msg += Files.readString(logFile.toPath());
            }

            throw new DeploymentException(msg);
        }

        LOGGER.log(INFO,
            "\n" +
            "Application is available at: " + appURL);
    }

    /**
     * Copy the Piranha JAR file.
     *
     * @param runtimeDirectory the runtime directory.
     * @param zipFile the zip file.
     */
    private void copyPiranhaJarFile(File runtimeDirectory, File zipFile) throws IOException {
        if (!runtimeDirectory.exists() && !runtimeDirectory.mkdirs()) {
            System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
        }

        Files.copy(zipFile.toPath(),
                Path.of(runtimeDirectory + "/piranha-dist-coreprofile.jar"),
                REPLACE_EXISTING);
    }

    /**
     * Is the string null or empty.
     *
     * @param string the string
     * @return true if it is, false otherwise.
     */
    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }
}

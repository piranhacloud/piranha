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
package cloud.piranha.maven.plugins.piranha.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.zip.ZipFile;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/*
 * -----------------------------------------------------------------------------
 * Documentation for this plugin is at src/main/javadoc/resources/server-maven-plugin.html
 * at the top-level project. If you introduce anything new / change anything 
 * that needs documentation please make sure this HTML page is updated.
 * -----------------------------------------------------------------------------
 */

/**
 * This goal will deploy the Maven WAR module and start Piranha Server in a 
 * separate process.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "start", defaultPhase = LifecyclePhase.NONE)
public class StartMojo extends ServerMojo {
    
    /**
     * Stores the 'Unable to create directories' message.
     */
    private static final String UNABLE_TO_CREATE_DIRECTORIES = "Unable to create directories";
    
    /**
     * Stores the debug flag.
     */
    @Parameter(property = "piranha.server.debug", defaultValue = "false", required = false)
    private String debug;
    
    /**
     * Stores the local repository directory.
     */
    private File localRepositoryDir = new File(System.getProperty("user.home"), ".m2/repository");

    /**
     * The version of Piranha Server to use.
     */
    @Parameter(name = "version", property = "piranha.server.version", required = false)
    private String version;
    
    /**
     * Stores the WAR name.
     */
    @Parameter(defaultValue = "${project.build.finalName}", required = true, readonly = true)
    private String warName;

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
     * Create artifact path.
     *
     * @param groupId the groupId.
     * @param artificatId the artifactId.
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
     * Copy the WAR file to Piranha Server.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void copyWarFileToPiranhaServer() throws IOException {
        File warFile = new File(buildDir, warName + ".war");
        File outputFile = new File(buildDir + "/piranha-server/piranha/webapps",
                warName + ".war");
        Files.copy(warFile.toPath(), outputFile.toPath(), REPLACE_EXISTING);
    }

    /**
     * Determine what version of Piranha to use.
     *
     * @return the version.
     */
    private String determineVersionToUse() {
        if (version == null) {
            version = getClass().getPackage().getImplementationVersion();
        }
        return version;
    }

    // 5. start the server and wait for the process.
    @Override
    public void execute() throws MojoExecutionException {
        try {
            String version = determineVersionToUse();
            ZipFile zipFile = getPiranhaZipFile(version);
            unzipPiranhaZipFile(zipFile);
            copyWarFileToPiranhaServer();
            startPiranhaServer();
        } catch (IOException ioe) {
            throw new MojoExecutionException(ioe);
        }
    }

    /**
     * Get the Piranha Server zip bundle.
     *
     * @param version the version.
     * @return the zip file.
     * @throws IOException when an I/O error occurs.
     */
    private ZipFile getPiranhaZipFile(String version) throws IOException {
        URL downloadUrl = createMavenCentralArtifactUrl(
                "cloud.piranha",
                "piranha-server",
                version,
                "zip"
        );

        String artifactPath = createArtifactPath(
                "cloud.piranha",
                "piranha-server",
                version,
                "zip"
        );

        File zipFile = new File(localRepositoryDir, artifactPath);
        if (!zipFile.exists()) {
            if (!zipFile.getParentFile().mkdirs()) {
                System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
            }
        }

        try ( InputStream inputStream = downloadUrl.openStream()) {
            Files.copy(inputStream,
                    zipFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch(FileNotFoundException fnfe) {
            System.err.println("Could not download zip bundle, defaulting back to local Maven repository");
        }

        return new ZipFile(new File(localRepositoryDir, artifactPath));
    }

    /**
     * Start Piranha Server.
     */
    private void startPiranhaServer() throws IOException {
        ProcessBuilder builder = new ProcessBuilder();

        System.out.println("Application is available at: http://localhost:8080/" + warName);
               
        StringBuilder arguments = new StringBuilder();
        
        if (Boolean.parseBoolean(debug)) {
            arguments.append(" --suspend");
            System.out.println("Waiting for debugger to attach to port: 9009");
        }
        
        if (System.getProperty("os.name").equalsIgnoreCase("windows")) {
            builder.directory(new File(buildDir, "piranha-server/piranha/bin"))
                    .command("start.cmd")
                    .start();
        } else {
            builder.directory(new File(buildDir, "piranha-server/piranha/bin"))
                    .command("/bin/bash", "-c", "./start.sh" + arguments.toString())
                    .start();
        }
    }

    /**
     * Unzip the Piranha Server zip bundle.
     *
     * @param zipFile the zip file.
     */
    private void unzipPiranhaZipFile(ZipFile zipFile) throws IOException {
        File targetDir = new File(buildDir, "piranha-server");
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
            }
        }
        zipFile.entries().asIterator().forEachRemaining(zipEntry -> {
                    if (zipEntry.isDirectory()) {
                        File directory = new File(targetDir, zipEntry.getName());
                        if (!directory.exists()) {
                            if (!directory.mkdirs()) {
                                System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
                            }
                        }
                    } else {
                        try {
                            File file = new File(targetDir, zipEntry.getName());
                            Files.copy(zipFile.getInputStream(zipEntry), file.toPath(), REPLACE_EXISTING);
                            if (zipEntry.getName().toLowerCase().endsWith(".sh")) {
                                if (!file.setExecutable(true)) {
                                    System.err.println("Unable to set " + zipEntry.getName() + " to executable");
                                }
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace(System.err);
                        }
                    }
                }
        );
    }
}

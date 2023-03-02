/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.maven.plugins.piranha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.zip.ZipFile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The base Mojo for the start and run goals.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class BaseMojo extends AbstractMojo {

    /**
     * Stores the 'Unable to create directories' message.
     */
    protected static final String UNABLE_TO_CREATE_DIRECTORIES = "Unable to create directories";

    /**
     * Stores the build directory.
     */
    @Parameter(defaultValue = "${project.build.directory}", required = true, readonly = true)
    protected String buildDirectory;

    /**
     * Stores the distribution to use.
     */
    @Parameter(defaultValue = "coreprofile", property = "piranha.distribution", required = false)
    protected String distribution;

    /**
     * Stores the context path.
     */
    @Parameter(required = false, property = "piranha.contextPath")
    protected String contextPath;

    /**
     * Stores the HTTP port.
     */
    @Parameter(defaultValue = "8080", property = "piranha.httpPort", required = false)
    protected Integer httpPort;

    /**
     * Stores the JVM arguments.
     */
    @Parameter(required = false, property = "piranha.jvmArguments")
    protected String jvmArguments;

    /**
     * Stores the local repository directory.
     */
    protected File localRepositoryDirectory = new File(System.getProperty("user.home"), ".m2/repository");

    /**
     * Stores the Piranha JAR/Zip file.
     */
    protected File piranhaFile;

    /**
     * Stores the Piranha type (jar or zip).
     */
    protected String piranhaType = "jar";

    /**
     * Stores the runtime directory.
     */
    @Parameter(defaultValue = "${project.build.directory}/piranha", property = "piranha.runtimeDirectory", required = true)
    protected String runtimeDirectory;

    /**
     * Stores the version of the Piranha runtime to use.
     */
    @Parameter(property = "piranha.version", required = false)
    protected String version;

    /**
     * Stores the WAR name.
     */
    @Parameter(defaultValue = "${project.build.finalName}", property="piranha.warName", required = true, readonly = true)
    protected String warName;

    /**
     * Convert a Maven groupId to a path snippet.
     *
     * @param groupId the groupId.
     * @return the path.
     */
    protected String convertGroupIdToPath(String groupId) {
        return groupId.replace('.', '/');
    }

    /**
     * Create an artifact path from a groupId, artifactId, version and type.
     *
     * @param groupId the groupId.
     * @param artifactId the artifactId.
     * @param version the version
     * @param type the type.
     * @return the artifact path.
     */
    protected String createArtifactPath(
            String groupId, String artifactId, String version, String type) {

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
     * Create the Maven central artifact URL
     *
     * @param groupId the groupId.
     * @param artifactId the artifactId.
     * @param version the version
     * @param type the type.
     * @return the URL.
     * @throws IOException when an I/O error occurs.
     */
    protected URL createMavenCentralArtifactUrl(String groupId, String artifactId,
            String version, String type) throws IOException {
        return new URL("https://repo1.maven.org/maven2/"
                + createArtifactPath(groupId, artifactId, version, type));
    }

    /**
     * Determine what version of Piranha to use.
     */
    protected void determineVersionToUse() {
        if (version == null) {
            version = getClass().getPackage().getImplementationVersion();
        }
    }
    
    /**
     * Copy the WAR file.
     *
     * @throws IOException when an I/O error occurs.
     */
    protected void copyWarFile() throws IOException {
        File warFile = new File(buildDirectory, warName + ".war");
        File outputFile;
        if (piranhaType.equals("jar")) {
            outputFile = new File(runtimeDirectory, warName + ".war");
        } else {
            outputFile = new File(runtimeDirectory + File.separator + "webapps", warName + ".war");
        }
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        Files.copy(warFile.toPath(), outputFile.toPath(), REPLACE_EXISTING);
    }

    /**
     * Download the Piranha distribution.
     *
     * @throws IOException when an I/O error occurs.
     */
    protected void downloadDistribution() throws IOException {
        if (distribution.equals("server")) {
            piranhaType = "zip";
        }

        URL downloadUrl = createMavenCentralArtifactUrl(
                "cloud.piranha.dist",
                "piranha-dist-" + distribution,
                version,
                piranhaType
        );

        String artifactPath = createArtifactPath(
                "cloud.piranha.dist",
                "piranha-dist-" + distribution,
                version,
                piranhaType
        );

        File file = new File(localRepositoryDirectory, artifactPath);
        if (!file.exists() && !file.getParentFile().mkdirs()) {
            System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
        }

        try ( InputStream inputStream = downloadUrl.openStream()) {
            Files.copy(inputStream,
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (FileNotFoundException fnfe) {
            System.err.println("Could not download distribution, defaulting back to local Maven repository");
        }

        piranhaFile = new File(localRepositoryDirectory, artifactPath);
    }

    /**
     * Extract the distribution.
     */
    protected void extractDistribution() {
        if (piranhaType.equals("zip")) {
            try (ZipFile zipFile = new ZipFile(piranhaFile)) {
                File targetDir = new File(runtimeDirectory).getParentFile();
                if (!targetDir.exists() && !targetDir.mkdirs()) {
                    System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
                }
                zipFile.entries().asIterator().forEachRemaining(zipEntry -> {
                    if (zipEntry.isDirectory()) {
                        File directory = new File(targetDir, zipEntry.getName());
                        if (!directory.exists() && !directory.mkdirs()) {
                            System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
                        }
                    } else {
                        try {
                            File file = new File(targetDir, zipEntry.getName());
                            Files.copy(zipFile.getInputStream(zipEntry), file.toPath(), REPLACE_EXISTING);
                            if (zipEntry.getName().toLowerCase().endsWith(".sh") && !file.setExecutable(true)) {
                                System.err.println("Unable to set " + zipEntry.getName() + " to executable");
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace(System.err);
                        }
                    }
                }
                );
            } catch (IOException ioe) {
                System.err.println("I/O error occurred opening zip file: " + piranhaFile.toString());
            }
        }
    }
}

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
package cloud.piranha.maven.plugins.piranha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
     * Stores the context path.
     */
    @Parameter(required = false)
    protected String contextPath;

    /**
     * Stores the HTTP port.
     */
    @Parameter(defaultValue = "8080", required = false)
    protected Integer httpPort;

    /**
     * Stores the local repository directory.
     */
    protected File localRepositoryDirectory = new File(System.getProperty("user.home"), ".m2/repository");

    /**
     * Stores the Piranha JAR file.
     */
    protected File piranhaJarFile;

    /**
     * Stores the runtime directory.
     */
    @Parameter(defaultValue = "${project.build.directory}/piranha", required = true)
    protected String runtimeDirectory;

    /**
     * Stores the version of the Piranha runtime to use.
     */
    @Parameter(required = false)
    protected String version;

    /**
     * Stores the WAR name.
     */
    @Parameter(defaultValue = "${project.build.finalName}", required = true, readonly = true)
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
     * Get the Piranha distribution JAR file.
     *
     * @throws IOException when an I/O error occurs.
     */
    protected void getPiranhaJarFile() throws IOException {
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

        File zipFile = new File(localRepositoryDirectory, artifactPath);
        if (!zipFile.exists()) {
            if (!zipFile.getParentFile().mkdirs()) {
                System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
            }
        }

        try ( InputStream inputStream = downloadUrl.openStream()) {
            Files.copy(inputStream,
                    zipFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (FileNotFoundException fnfe) {
            System.err.println("Could not download JAR file, defaulting back to local Maven repository");
        }

        piranhaJarFile = new File(localRepositoryDirectory, artifactPath);
    }
}

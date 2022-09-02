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
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import static org.apache.maven.plugins.annotations.LifecyclePhase.NONE;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This goal will deploy the Maven WAR module and start Piranha Core Profile in
 * a separate process.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "start", defaultPhase = NONE)
public class StartMojo extends AbstractMojo {
    
    /**
     * Stores the 'Unable to create directories' message.
     */
    private static final String UNABLE_TO_CREATE_DIRECTORIES = "Unable to create directories";
    
    /**
     * Stores the build directory.
     */
    @Parameter(defaultValue = "${project.build.directory}", required = true, readonly = true)
    private String buildDirectory;
    
    /**
     * Stores the runtime directory.
     */
    @Parameter(defaultValue = "${project.build.directory}/piranha", required = true)
    private String runtimeDirectory;

    /**
     * Stores the local repository directory.
     */
    private File localRepositoryDir = new File(System.getProperty("user.home"), ".m2/repository");

    /**
     * The version of Piranha Core Profile to use.
     */
    @Parameter(required = false)
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
     * Copy the WAR file to Piranha Core Profile.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void copyWarFileToPiranhaCoreProfile() throws IOException {
        File warFile = new File(buildDirectory, warName + ".war");
        File outputFile = new File(runtimeDirectory, warName + ".war");
        System.out.println(warFile.toPath());
        System.out.println(outputFile.toPath());
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

    @Override
    public void execute() throws MojoExecutionException {
        try {
            version = determineVersionToUse();
            File zipFile = getPiranhaJarFile(version);
            copyPiranhaCoreProfileJarFile(zipFile);
            copyWarFileToPiranhaCoreProfile();
            startPiranhaCoreProfile();
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

        return new File(localRepositoryDir, artifactPath);
    }

    /**
     * Start and wait for Piranha Core Profile.
     */
    private void startPiranhaCoreProfile() throws IOException {
        new ProcessBuilder()
                .directory(new File(runtimeDirectory))
                .command("java",
                        "-jar",
                        "piranha-dist-coreprofile.jar",
                        "--war-file",
                        warName + ".war",
                        "--write-pid")
                .start();

        System.out.println("Application is available at: http://localhost:8080/" + warName);
    }

    /**
     * Copy the Piranha Core Profile JAR file.
     *
     * @param zipFile the zip file.
     */
    private void copyPiranhaCoreProfileJarFile(File zipFile) throws IOException {
        File targetDir = new File(runtimeDirectory);
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println(UNABLE_TO_CREATE_DIRECTORIES);
            }
        }
        Files.copy(zipFile.toPath(),
                Path.of("target/piranha/piranha-dist-coreprofile.jar"),
                REPLACE_EXISTING);
    }
}

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
package cloud.piranha.cli;

import static cloud.piranha.cli.Util.version;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * The coreprofile download command.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class CoreProfileDownloadCommand implements Runnable {

    /**
     * Stores the Maven central prefix.
     */
    private static final String MAVEN_CENTRAL_PREFIX
            = "https://repo1.maven.org/maven2/cloud/piranha/dist/";

    /**
     * Stores the Sonatype SNAPSHOTs prefix.
     */
    private static final String SONATYPE_SNAPSHOTS_PREFIX
            = "https://oss.sonatype.org/content/repositories/snapshots/cloud/piranha/dist/";

    /**
     * Stores the arguments.
     */
    private final String[] arguments;

    /**
     * Stores the filename we are going to download to.
     */
    private String filename = "piranha-dist-coreprofile-" + version() + ".jar";

    /**
     * Stores the output directory.
     */
    private File outputDirectory = new File(System.getProperty("user.home"),
            ".piranha/coreprofile/download");

    /**
     * Stores the URL we are going to download.
     */
    private URL url;

    /**
     * Stores the version we are downloading.
     */
    private String version = "24.2.0";

    /**
     * Constructor.
     *
     * @param arguments the arguments.
     */
    public CoreProfileDownloadCommand(String[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Download a release version.
     */
    private void downloadRelease() {
        String urlString = MAVEN_CENTRAL_PREFIX + "piranha-dist-coreprofile/"
                + version + "/piranha-dist-coreprofile-" + version + ".jar";

        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue) {
            mue.printStackTrace(System.out);
            System.exit(0);
        }

        downloadUrl();
    }

    /**
     * Download a SNAPSHOT version.
     */
    private void downloadSnapshot() {
        String urlString = SONATYPE_SNAPSHOTS_PREFIX + "piranha-dist-coreprofile/"
                + version + "/piranha-dist-coreprofile-" + version + ".jar";

        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue) {
            mue.printStackTrace(System.out);
            System.exit(0);
        }

        downloadUrl();
    }

    /**
     * Download the URL.
     */
    private void downloadUrl() {
        System.out.println("Downloading Piranha Core Profile - " + version);
        System.out.println("From - " + url);
        System.out.println("To - " + outputDirectory);
        
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(new File(outputDirectory, filename).toURI()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
            System.exit(0);
        }
    }

    @Override
    public void run() {
        if (version.endsWith("-SNAPSHOT")) {
            downloadSnapshot();
        } else {
            downloadRelease();
        }
    }
}

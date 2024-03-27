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
import java.util.ArrayList;

/**
 * The coreprofile run command.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class CoreProfileStartCommand implements Runnable {

    /**
     * Stores the arguments.
     */
    private final String[] arguments;

    /**
     * Stores the distribution filename we are going use.
     */
    private String distributionFilename = "piranha-dist-coreprofile-" + version() + ".jar";

    /**
     * Stores the distribution directory.
     */
    private File distributionDirectory = new File(System.getProperty("user.home"),
            ".piranha/coreprofile/download");

    /**
     * Stores the run arguments (--arguments).
     */
    private String runArguments;

    /**
     * Stores the version we are using.
     */
    private String version = "24.3.0";

    /**
     * Constructor.
     *
     * @param arguments the arguments.
     */
    public CoreProfileStartCommand(String[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public void run() {
        parseArguments();
        distributionFilename = "piranha-dist-coreprofile-" + version + ".jar";
        File distributionJarFile = new File(distributionDirectory, distributionFilename);
        if (distributionJarFile.exists()) {
            startPiranhaCoreProfile();
        } else {
            System.out.println(distributionJarFile.toString() + " not found");
        }
    }

    /**
     * Parse the command-line arguments.
     */
    private void parseArguments() {
        System.out.println("Parse arguments");
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].equals("--arguments")) {
                runArguments = arguments[i + 1];
            }
            if (arguments[i].equals("--version")) {
                version = arguments[i + 1];
            }
        }
    }

    /**
     * Start Piranha Core Profile.
     */
    private void startPiranhaCoreProfile() {
        try {
            distributionFilename = "piranha-dist-coreprofile-" + version + ".jar";
            File distributionJarFile = new File(distributionDirectory, distributionFilename);
            System.out.println("Start Piranha Core Profile");
            ProcessBuilder builder = new ProcessBuilder();
            ArrayList<String> processCommands = new ArrayList<>();
            processCommands.add("java");
            processCommands.add("-jar");
            processCommands.add(distributionJarFile.getAbsolutePath());
            processCommands.add("--write-pid");
            if (runArguments != null) {
                processCommands.add(runArguments);
            }
            Process process = builder.command(processCommands).inheritIO().start();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
}

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
package cloud.piranha.cli;

import java.util.List;

/**
 * The Piranha Nano Generate CLI.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoGenerateCli {

    /**
     * Stores the pattern.
     */
    private static final String PATTERN = "  %-38s: %s\n";

    /**
     * Stores the name.
     */
    private String name = null;

    /**
     * Stores the output directory.
     */
    private String outputDirectory = "";

    /**
     * Execute the Nano CLI.
     *
     * @param arguments the arguments.
     */
    public void execute(List<String> arguments) {
        parse(arguments);

        if (name == null) {
            usage();
            return;
        }

        generatePom();
    }

    /**
     * Parse out the arguments.
     *
     * @param arguments the arguments.
     */
    private void parse(List<String> arguments) {
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).equals("--name")) {
                name = arguments.get(i + 1);
            }
            if (arguments.get(i).equals("--outputDirectory")) {
                outputDirectory = arguments.get(i + 1);
            }
        }
    }

    /**
     * Generate the POM file.
     */
    public void generatePom() {
    }

    /**
     * Shows the usage.
     */
    private void usage() {
        System.out.println("usage: pi nano generate <arguments>");
        System.out.println();
        System.out.println("Required arguments:");
        System.out.printf(PATTERN, "--name <name>", "The name of the application");
        System.out.println();
        System.out.println("Optional arguments:");
        System.out.printf(PATTERN, "--outputDirectory <outputDirectory>", "The output directory");
    }
}

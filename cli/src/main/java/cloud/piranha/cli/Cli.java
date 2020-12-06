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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Piranha CLI.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class Cli {

    /**
     * Stores the pattern.
     */
    private static final String PATTERN = "  %-10s: %s\n";

    /**
     * Execute the arguments.
     *
     * @param arguments the arguments.
     */
    public void execute(List<String> arguments) {
        if (!arguments.isEmpty()) {
            ArrayList<String> list = new ArrayList<>();
            list.addAll(arguments);
            if (!list.isEmpty()) {
                list.remove(0);
            }
            switch (arguments.get(0)) {
                case "help":
                    help();
                    break;
                case "nano":
                    nano(list);
                    break;
                case "version":
                    version();
                    break;
                default:
                    usage();
                    break;
            }
        } else {
            usage();
        }
    }

    /**
     * Shows the link where to find help.
     */
    private void help() {
        System.out.println("Please go to https://piranha.cloud/cli for more help");
    }

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        Cli cli = new Cli();
        cli.execute(Arrays.asList(arguments));
    }

    /**
     * Invoke the Piranha Nano CLI.
     *
     * @param arguments the arguments.
     */
    public void nano(List<String> arguments) {
        NanoCli cli = new NanoCli();
        cli.execute(arguments);
    }

    /**
     * Shows the usage.
     */
    private void usage() {
        System.out.println("usage: pi <command>");
        System.out.println();
        System.out.printf(PATTERN, "help", "Where to go for help?");
        System.out.printf(PATTERN, "nano", "Use Piranha Nano");
        System.out.printf(PATTERN, "version", "Show the version of the Piranha CLI");
    }

    /**
     * Show the version.
     */
    private void version() {
        System.out.println("Version " + getClass().getModule().getDescriptor().version().get().toString());
    }
}

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
package cloud.piranha.fin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * The Fin version of Piranha.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FinPiranha implements Runnable {

    /**
     * Stores the arguments to pass.
     */
    private List<String> arguments;

    /**
     * Stores the temporary directory.
     */
    private File tempDirectory = new File(".piranha/fin");

    /**
     * Main method.
     *
     * @param arguments the command-line arguments.
     */
    public static void main(String[] arguments) {
        FinPiranha piranha = new FinPiranha();
        piranha.parseArguments(arguments);
        piranha.run();
    }

    /**
     * Parse the arguments.
     *
     * @param arguments the arguments.
     */
    private void parseArguments(String[] arguments) {
        ArrayList<String> argumentList = new ArrayList<>();
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].equals("--fin-temp-directory")) {
                tempDirectory = new File(arguments[i + 1]);
                i++;
            } else {
                argumentList.add(arguments[i]);
            }
        }
        this.arguments = argumentList;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        if (!tempDirectory.exists()) {
            if (!tempDirectory.mkdirs()) {
                System.err.println("Unable to create temporary directory, exiting");
                System.exit(1);
            }
        }

        URL url = getClass().getResource("/META-INF/MANIFEST.MF");
        System.out.println(url);
        String filename = url.toExternalForm();
        System.out.println(filename);
        filename = filename.substring("jar:file:".length());
        System.out.println(filename);
        filename = filename.substring(0, filename.lastIndexOf("!"));
        System.out.println(filename);

        try (JarFile jarFile = new JarFile(filename)) {
            Manifest manifest = jarFile.getManifest();
            Attributes mainAttributes = manifest.getMainAttributes();
            for (Map.Entry<Object, Object> entry : mainAttributes.entrySet()) {
                Object key = entry.getKey();
                Object val = entry.getValue();
                System.out.println(key.toString() + ": " + val.toString());
            }
            Attributes piranhaAttributes = manifest.getAttributes("piranha");
            if (piranhaAttributes != null) {
                System.out.println("Piranha Distribution: " + piranhaAttributes.getValue("distribution"));
                System.out.println("Piranha Version: " + piranhaAttributes.getValue("version"));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

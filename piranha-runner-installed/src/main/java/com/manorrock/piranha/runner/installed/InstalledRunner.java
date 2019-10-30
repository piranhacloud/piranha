/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.runner.installed;

import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.DefaultWebApplicationClassLoader;
import com.manorrock.piranha.DefaultWebApplicationServer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The "installed" runner.
 *
 * <p>
 * This runner is the main entry point for an installed version of Piranha. It
 * allows you to run multiple web applications at the same time.
 * </p>
 *
 * <p>
 * This runner has a shutdown mechanism that allows you to shutdown the server
 * by removing the piranha.pid file that should be created by the startup
 * script.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class InstalledRunner implements Runnable {

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        InstalledRunner runner = new InstalledRunner();
        runner.run();
    }

    /**
     * Extract the zip input stream.
     *
     * @param zipInput the zip input stream.
     * @param filePath the file path.
     * @throws IOException when an I/O error occurs.
     */
    private void extractZipInputStream(ZipInputStream zipInput, String filePath) throws IOException {
        try (BufferedOutputStream bufferOutput = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[8192];
            int read;
            while ((read = zipInput.read(bytesIn)) != -1) {
                bufferOutput.write(bytesIn, 0, read);
            }
        }
    }

    /**
     * Extract the WAR file.
     */
    private void extractWarFile(File warFile, File webApplicationDirectory) {
        if (!webApplicationDirectory.exists()) {
            webApplicationDirectory.mkdirs();
        }
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(warFile))) {
            ZipEntry entry = zipInput.getNextEntry();
            while (entry != null) {
                String filePath = webApplicationDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    File file = new File(filePath);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    extractZipInputStream(zipInput, filePath);
                }
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }
        } catch (IOException ioe) {
        }
    }

    /**
     * Start method.
     */
    @Override
    public void run() {
        File pidFile = new File("tmp/piranha.pid");
        DefaultWebApplicationServer webApplicationServer = new DefaultWebApplicationServer();
        DefaultHttpServer httpServer = new DefaultHttpServer(8080, webApplicationServer);
        httpServer.start();
        webApplicationServer.start();

        File webappsDirectory = new File("webapps");
        File[] webapps = webappsDirectory.listFiles();
        if (webapps != null && webapps.length > 0) {
            for (File webapp : webapps) {
                if (webapp.getName().toLowerCase().endsWith(".war")) {
                    String contextPath = webapp.getName().substring(0, webapp.getName().length() - 4);
                    File webAppDirectory = new File(webappsDirectory, contextPath);
                    extractWarFile(webapp, webAppDirectory);
                    DefaultWebApplication webApplication = new DefaultWebApplication();
                    DefaultWebApplicationClassLoader classLoader
                            = new DefaultWebApplicationClassLoader(webAppDirectory);
                    webApplication.setClassLoader(classLoader);
                    if (contextPath.toUpperCase().equals("ROOT")) {
                        contextPath = "";
                    } else if (!contextPath.startsWith("/")) {
                        contextPath = "/" + contextPath;
                    }
                    webApplication.setContextPath(contextPath);
                    webApplicationServer.addWebApplication(webApplication);
                    webApplication.initialize();
                    webApplication.start();
                }
            }
        }

        while (httpServer.isRunning()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            if (!pidFile.exists()) {
                webApplicationServer.stop();
                httpServer.stop();
                System.exit(0);
            }
        }
    }
}

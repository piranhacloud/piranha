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
package cloud.piranha.core.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.System.Logger.Level.WARNING;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A utility class to extract WAR files onto the file system.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WarFileExtractor {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(WarFileExtractor.class.getName());

    /**
     * Extract the WAR file to the given directory.
     *
     * @param warFile the WAR file.
     * @param directory the web application directory.
     */
    public static void extractWarFile(File warFile, File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(warFile))) {
            ZipEntry entry = zipInput.getNextEntry();
            while (entry != null) {
                String filePath = directory + File.separator + entry.getName();
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
            LOGGER.log(WARNING, "I/O error occurred while extracting WAR file", ioe);
        }
    }

    /**
     * Extract the zip input stream.
     *
     * @param zipInput the zip input stream.
     * @param filePath the file path.
     * @throws IOException when an I/O error occurs.
     */
    private static void extractZipInputStream(ZipInputStream zipInput, String filePath) throws IOException {
        try (BufferedOutputStream bufferOutput = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[8192];
            int read;
            while ((read = zipInput.read(bytesIn)) != -1) {
                bufferOutput.write(bytesIn, 0, read);
            }
        }
    }
}

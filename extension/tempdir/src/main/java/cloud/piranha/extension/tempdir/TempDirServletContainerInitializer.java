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
package cloud.piranha.extension.tempdir;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import static jakarta.servlet.ServletContext.TEMPDIR;
import jakarta.servlet.ServletException;
import java.io.File;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import java.util.Set;

/**
 * The ServletContainerInitializer that creates the temporary directory on the
 * file system and sets the context attribute to point to that directory.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TempDirServletContainerInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(TempDirServletContainerInitializer.class.getName());

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        File baseDir = new File("tmp");
        String name = servletContext.getContextPath();
        name = name.replace("/", "_");
        if (name.trim().equals("")) {
            name = "ROOT";
        }
        File tempDir = new File(baseDir, name);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        LOGGER.log(DEBUG, "Setting TEMPDIR for context ''{0}'' to ''{1}''",
                servletContext.getContextPath(), tempDir);
        servletContext.setAttribute(TEMPDIR, tempDir);
    }
}

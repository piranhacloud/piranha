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
package cloud.piranha.upload.apache;

import cloud.piranha.webapp.api.WebApplication;
import java.util.Set;
import java.util.logging.Logger;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The ServletContainerInitializer for the ApacheMultiPartManager.
 *
 * <p>
 * The ServletContainerInitializer performs the following steps:
 * </p>
 *
 * <ol>
 * <li>Sets the MultiPartManager to an instance of ApacheMultiPartManager.</li>
 * <li>Adds the FileCleanerCleanup listener that cleans up the temporary
 * files.</li>
 * </ol>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ApacheMultiPartInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(
            ApacheMultiPartInitializer.class.getPackageName());

    /**
     * @see ServletContainerInitializer#onStartup(java.util.Set,
     * jakarta.servlet.ServletContext)
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {
        LOGGER.info("Initializing ApacheMultiPartManager");

        WebApplication webApplication = (WebApplication) servletContext;
        webApplication.setMultiPartManager(new ApacheMultiPartManager());
        webApplication.addListener("org.apache.commons.fileupload.servlet.FileCleanerCleanup");
    }
}

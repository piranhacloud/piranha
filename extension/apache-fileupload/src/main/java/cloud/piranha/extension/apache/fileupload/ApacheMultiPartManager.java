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
package cloud.piranha.extension.apache.fileupload;

import cloud.piranha.core.api.MultiPartManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import jakarta.servlet.MultipartConfigElement;
import static jakarta.servlet.ServletContext.TEMPDIR;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.File;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.Logger.Level.WARNING;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

/**
 * The ApacheMultiPartManager.
 *
 * <p>
 * The ApacheMultiPartManager implements the MultiPartManager API that delivers
 * file upload functionality to a web application by delegating to Apache
 * Commons File Upload.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ApacheMultiPartManager implements MultiPartManager {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ApacheMultiPartManager.class.getName());

    /**
     * Constructor.
     */
    public ApacheMultiPartManager() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Part> getParts(WebApplication webApplication, WebApplicationRequest request) throws ServletException {
        LOGGER.log(TRACE, "Getting parts for request: {0}", request);

        if (!JakartaServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("Not a multipart/form-data request");
        }

        Collection<Part> parts = (Collection<Part>) request.getAttribute(Part.class.getName());
        if (parts == null) {
            Collection<Part> newParts = new ArrayList<>();
            try {
                setupFileUpload(webApplication, request.getMultipartConfig())
                        .parseRequest((HttpServletRequest) request)
                        .forEach(item -> newParts.add(new ApacheMultiPart((FileItem) item)));
            } catch (FileUploadException fue) {
                LOGGER.log(WARNING, "Error getting part", fue);
            }
            request.setAttribute(Part.class.getName(), newParts);
            parts = newParts;
        }

        return parts;
    }

    @Override
    public Part getPart(WebApplication webApplication, WebApplicationRequest request, String name) throws ServletException {
        LOGGER.log(TRACE, "Getting part: {0} for request: {1}", name, request);
        if (!JakartaServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("Not a multipart/form-data request");
        }
        for (Part part : getParts(webApplication, request)) {
            if (part.getName().equals(name)) {
                return part;
            }
        }
        return null;
    }

    /**
     * Setup the JakartaServletFileUpload instance.
     *
     * @param webApplication the web application.
     * @param multipartConfig the multi-part config element.
     * @return the JakartServletFileUpload instance.
     */
    private synchronized JakartaServletFileUpload setupFileUpload(WebApplication webApplication, MultipartConfigElement multipartConfig) {
        JakartaServletFileUpload upload = (JakartaServletFileUpload) webApplication.getAttribute(ApacheMultiPartManager.class.getName());
        if (upload == null) {
            File outputDirectory;
            if (multipartConfig.getLocation() == null || multipartConfig.getLocation().isEmpty()) {
                outputDirectory = (File) webApplication.getAttribute(TEMPDIR);
            } else {
                File location = new File(multipartConfig.getLocation());
                if (!location.isAbsolute()) {
                    location = ((File) webApplication.getAttribute(TEMPDIR)).toPath().resolve(location.toPath()).toFile();
                }
                outputDirectory = location;
            }
            int sizeThreshold = 10240;
            if (multipartConfig.getFileSizeThreshold() != 0) {
                sizeThreshold = multipartConfig.getFileSizeThreshold();
            }
            DiskFileItemFactory factory = DiskFileItemFactory
                    .builder()
                    .setBufferSize(sizeThreshold)
                    .setFile(outputDirectory)
                    .get();
            upload = new JakartaServletFileUpload(factory);
            webApplication.setAttribute(ApacheMultiPartManager.class.getName(), upload);
        }
        return upload;
    }
}

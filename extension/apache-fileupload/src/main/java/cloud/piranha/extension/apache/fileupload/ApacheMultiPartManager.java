/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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

import static jakarta.servlet.ServletContext.TEMPDIR;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import static org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.System.Logger;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cloud.piranha.webapp.api.MultiPartManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

/**
 * The ApacheMultiPartManager.
 *
 * <p>
 * The ApacheMultiPartManager implements the MultiPartManager API that delivers file upload functionality to a web
 * application by delegating to Apache Commons File Upload.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ApacheMultiPartManager implements MultiPartManager {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ApacheMultiPartManager.class.getPackageName());

    /**
     * @see MultiPartManager#getParts(cloud.piranha.webapp.api.WebApplication,
     * cloud.piranha.webapp.api.WebApplicationRequest)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Part> getParts(WebApplication webApplication, WebApplicationRequest request) throws ServletException {
        LOGGER.log(DEBUG, "Getting parts for request: {0}", request);
        
        if (!isMultipartContent(request)) {
            throw new ServletException("Not a multipart/form-data request");
        }
        
        Collection<Part> parts = (Collection<Part>) request.getAttribute(Part.class.getName());
        if (parts == null) {
            Collection<Part> newParts = new ArrayList<>();
            try {
                setupFileUpload(webApplication, request.getMultipartConfig())
                    .parseRequest(request)
                    .forEach(item -> newParts.add(new ApacheMultiPart(item)));
            } catch (FileUploadException fue) {
                LOGGER.log(WARNING, "Error getting part", fue);
            }
            request.setAttribute(Part.class.getName(), newParts);
            parts = newParts;
        }
        
        return parts;
    }

    /**
     * @see MultiPartManager#getPart(cloud.piranha.webapp.api.WebApplication,
     * cloud.piranha.webapp.api.WebApplicationRequest, java.lang.String)
     */
    @Override
    public Part getPart(WebApplication webApplication, WebApplicationRequest request, String name) throws ServletException {
        LOGGER.log(DEBUG, "Getting part: {0} for request: {1}", new Object[] { name, request });
        
        if (!isMultipartContent(request)) {
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
     * Setup the ServletFileUpload.
     *
     * @param webApplication the web application.
     */
    private synchronized ServletFileUpload setupFileUpload(WebApplication webApplication, MultipartConfigElement multipartConfig) {
        ServletFileUpload upload = (ServletFileUpload) webApplication.getAttribute(ApacheMultiPartManager.class.getName());

        if (upload == null) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            if (multipartConfig.getLocation() == null || multipartConfig.getLocation().isEmpty()) {
                factory.setRepository((File) webApplication.getAttribute(TEMPDIR));
            } else {
                File location = new File(multipartConfig.getLocation());
                if (!location.isAbsolute()) {
                    location = ((File) webApplication.getAttribute(TEMPDIR)).toPath().resolve(location.toPath()).toFile();
                }
                
                factory.setRepository(location);
            }
            if (multipartConfig.getFileSizeThreshold() != 0) { // correct?
                factory.setSizeThreshold(multipartConfig.getFileSizeThreshold());
            }
            factory.setRepository((File) webApplication.getAttribute(TEMPDIR));
            upload = new ServletFileUpload(factory);
            webApplication.setAttribute(ApacheMultiPartManager.class.getName(), upload);
        }
        
        return upload;
    }
}

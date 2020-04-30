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

import cloud.piranha.webapp.api.MultiPartManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * The Apache Commons FileUpload MultiPartManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ApacheMultiPartManager implements MultiPartManager {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ApacheMultiPartManager.class.getName());

    /**
     * Get the parts.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @return the parts.
     */
    @Override
    public Collection<Part> getParts(WebApplication webApplication,
            WebApplicationRequest request) throws ServletException {

        Collection<Part> parts = new ArrayList<>();
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Getting parts for request: {0}", request);
        }
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                ServletFileUpload upload = setupFileUpload(webApplication);
                List<FileItem> items = upload.parseRequest(request);
                items.forEach((item) -> {
                    parts.add(new ApacheMultiPart(item));
                });
            } catch (FileUploadException fue) {
            }
        } else {
            throw new ServletException("Not a multipart/form-data request");
        }
        return parts;
    }

    /**
     * Get the part.
     *
     * @param webApplication the web application.
     * @param request the request.
     * @param name the name of the part.
     * @return the part, or null if not found.
     */
    @Override
    public Part getPart(WebApplication webApplication, 
            WebApplicationRequest request, String name) throws ServletException {
        
        ApacheMultiPart result = null;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(FINE, "Getting part: {0} for request: {0}", new Object[]{name, request});
        }
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                ServletFileUpload upload = setupFileUpload(webApplication);
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (item.getName().equals(name)) {
                        result = new ApacheMultiPart(item);
                        break;
                    }
                }
            } catch (FileUploadException fue) {
                if (LOGGER.isLoggable(WARNING)) {
                    LOGGER.log(WARNING, "Error getting part", fue);
                }
            }
        } else {
            throw new ServletException("Not a multipart/form-data request");
        }
        return result;
    }

    /**
     * Setup servlet file upload.
     * 
     * @param webApplication the web application.
     */
    private synchronized ServletFileUpload setupFileUpload(WebApplication webApplication) {
        ServletFileUpload upload = (ServletFileUpload) 
                webApplication.getAttribute(ApacheMultiPartManager.class.getName());
        
        if (upload == null) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(8192);
            File repository = (File) webApplication.getAttribute(ServletContext.TEMPDIR);
            factory.setRepository(repository);
            upload = new ServletFileUpload(factory);
            webApplication.setAttribute(ApacheMultiPartManager.class.getName(), upload);
        }
        return upload;
    }
}

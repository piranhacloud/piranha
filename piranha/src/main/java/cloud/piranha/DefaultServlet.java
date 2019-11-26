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
package cloud.piranha;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The default Servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServlet extends HttpServlet {

    private static final long serialVersionUID = 1331822806510796938L;

    /**
     * Get the requested resource.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        InputStream resource = getResource(request);
        
        if (resource == null) {
            response.sendError(SC_NOT_FOUND);
            return;
        }
        
        setContentType(request, response);
        
        try (InputStream inputStream = new BufferedInputStream(resource)) {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            int read = inputStream.read();
            while (read != -1) {
                outputStream.write(read);
                read = inputStream.read();
            }
            outputStream.flush();
        }
    }
    
    private InputStream getResource(HttpServletRequest request) {
        return request.getServletContext().getResourceAsStream(getPath(request));
    }
    
    private String getPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
    
    private void setContentType(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        
        String filename = uri.contains("/") ? uri.substring(uri.lastIndexOf("/") + 1) : uri.isEmpty()? "" : uri.substring(1);
        String mimeType = request.getServletContext().getMimeType(filename);
        
        if (mimeType != null) {
            response.setContentType(mimeType);
        } else {
            response.setContentType("application/octet-stream");
        }
    }
}

/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

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

    /**
     * Get the requested resource.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String filename = uri.contains("/") ? uri.substring(uri.lastIndexOf("/") + 1) : uri.substring(1);
        String mimeType = request.getServletContext().getMimeType(filename);
        if (mimeType != null) {
            response.setContentType(mimeType);
        } else {
            response.setContentType("application/octet-stream");
        }
        uri = uri.substring(request.getContextPath().length());
        try (InputStream inputStream = new BufferedInputStream(request.getServletContext().getResourceAsStream(uri))) {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            int read = inputStream.read();
            while (read != -1) {
                outputStream.write(read);
                read = inputStream.read();
            }
            outputStream.flush();
        }
    }
}

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
package jakarta.servlet;

import java.io.IOException;

/**
 * The RequestDispatcher API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface RequestDispatcher {

    /**
     * Defines the ERROR_EXCEPTION constant.
     */
    static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";

    /**
     * Defines the ERROR_EXCEPTION_TYPE constant.
     */
    static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";

    /**
     * Defines the ERROR_MESSAGE constant.
     */
    static final String ERROR_MESSAGE = "jakarta.servlet.error.message";

    /**
     * Defines the ERROR_REQUEST_URI constant.
     */
    static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";

    /**
     * Defines the ERROR_SERVLET_NAME constant.
     */
    static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";

    /**
     * Defines the ERROR_STATUS_CODE constant.
     */
    static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    /**
     * Defines the FORWARD_CONTEXT_PATH constant.
     */
    static final String FORWARD_CONTEXT_PATH = "jakarta.servlet.forward.context_path";
    
    /**
     * Defines the FORWARD_MAPPING constant.
     */
    static final String	FORWARD_MAPPING	= "jakarta.servlet.forward.mapping";

    /**
     * Defines the FORWARD_PATH_INFO constant.
     */
    static final String FORWARD_PATH_INFO = "jakarta.servlet.forward.path_info";

    /**
     * Defines the FORWARD_QUERY_STRING constant.
     */
    static final String FORWARD_QUERY_STRING = "jakarta.servlet.forward.query_string";

    /**
     * Defines the FORWARD_REQUEST_URI constant.
     */
    static final String FORWARD_REQUEST_URI = "jakarta.servlet.forward.request_uri";

    /**
     * Defines the FORWARD_SERVLET_PATH constant.
     */
    static final String FORWARD_SERVLET_PATH = "jakarta.servlet.forward.servlet_path";

    /**
     * Defines the INCLUDE_CONTEXT_PATH constant.
     */
    static final String INCLUDE_CONTEXT_PATH = "jakarta.servlet.include.context_path";
    
    /**
     * Defines the INCLUDE_MAPPING constant.
     */
    static final String INCLUDE_MAPPING = "jakarta.servlet.include.mapping";

    /**
     * Defines the INCLUDE_PATH_INFO constant.
     */
    static final String INCLUDE_PATH_INFO = "jakarta.servlet.include.path_info";

    /**
     * Defines the INCLUDE_QUERY_STRING constant.
     */
    static final String INCLUDE_QUERY_STRING = "jakarta.servlet.include.query_string";

    /**
     * Defines the INCLUDE_REQUEST_URI constant.
     */
    static final String INCLUDE_REQUEST_URI = "jakarta.servlet.include.request_uri";

    /**
     * Defines the INCLUDE_SERVLET_PATH constant.
     */
    static final String INCLUDE_SERVLET_PATH = "jakarta.servlet.include.servlet_path";

    /**
     * Forward the request.
     *
     * @param request the request.
     * @param response the response
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    void forward(ServletRequest request, ServletResponse response) throws IOException, ServletException;

    /**
     * Include into the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    void include(ServletRequest request, ServletResponse response) throws IOException, ServletException;
}

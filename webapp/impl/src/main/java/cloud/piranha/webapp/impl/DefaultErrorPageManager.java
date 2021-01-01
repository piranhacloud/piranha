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
package cloud.piranha.webapp.impl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arjan Tijms
 */
public class DefaultErrorPageManager {

    /**
     * Stores the error pages by code.
     */
    private final Map<Integer, String> errorPagesByCode = new HashMap<>();
    
    /**
     * Stores the error pages by exception.
     */
    private final Map<String, String> errorPagesByException = new HashMap<>();

    /**
     * Get the error pages by code.
     * 
     * @return the error pages by code map.
     */
    public Map<Integer, String> getErrorPagesByCode() {
        return errorPagesByCode;
    }

    /**
     * Get the error pages by exception.
     * 
     * @return the error pages by exception map.
     */
    public Map<String, String> getErrorPagesByException() {
        return errorPagesByException;
    }

    /**
     * Get the error page.
     * 
     * @param exception the exception.
     * @param httpResponse the HTTP servlet response.
     * @return the error page.
     */
    public String getErrorPage(Throwable exception, HttpServletResponse httpResponse) {
        if (exception != null) {
            Class<?> rootException = exception.getClass();
            String page = null;
            while (rootException != null && page == null) {
                page = errorPagesByException.get(rootException.getName());
                rootException = rootException.getSuperclass();
            }

            if (page == null && exception instanceof ServletException) {
                page = getErrorPage(((ServletException) exception).getRootCause(), httpResponse);
            }

            return page;
        }


        if (httpResponse.getStatus() >= 400) {
            return errorPagesByCode.get(httpResponse.getStatus());
        }

        // No error
        return null;
    }

}

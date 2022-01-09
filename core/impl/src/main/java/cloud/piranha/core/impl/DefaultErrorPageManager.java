/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.core.api.ErrorPageManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * The default ErrorPageManager.
 * 
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultErrorPageManager implements ErrorPageManager {

    /**
     * Stores the error pages by code.
     */
    private final Map<Integer, String> errorPagesByCode = new HashMap<>();
    
    /**
     * Stores the error pages by exception.
     */
    private final Map<String, String> errorPagesByException = new HashMap<>();
    
    @Override
    public void addErrorPage(int statusCode, String page) {
        errorPagesByCode.put(statusCode, page);
    }
    
    @Override
    public void addErrorPage(String throwableClassName, String page) {
        errorPagesByException.put(throwableClassName, page);
    }

    @Override
    public String getErrorPage(Throwable exception, HttpServletResponse httpResponse) {
        if (exception != null) {
            Class<?> rootException = exception.getClass();
            String page = null;
            while (rootException != null && page == null) {
                page = errorPagesByException.get(rootException.getName());
                rootException = rootException.getSuperclass();
            }

            if (page == null && exception instanceof ServletException servletException) {
                page = getErrorPage(servletException.getRootCause(), httpResponse);
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

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
package cloud.piranha.core.api;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpUpgradeHandler;
import java.security.Principal;

/**
 * The WebApplicationRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationRequest extends HttpServletRequest {

    /**
     * {@return the multipartConfig}
     */
    default MultipartConfigElement getMultipartConfig() {
        return null;
    }
    
    /**
     * {@return the upgrade handler}
     */
    default HttpUpgradeHandler getUpgradeHandler() {
        return null;
    }
    
    /**
     * Get the web application input stream.
     * 
     * @return the web application input stream.
     */
    default WebApplicationInputStream getWebApplicationInputStream() {
        return null;
    }

    /**
     * {@return true when upgraded, false otherwise}
     */
    default boolean isUpgraded() {
        return false;
    }

    /**
     * Set the async supported flag.
     * 
     * @param asyncSupported the async supported flag.
     */
    void setAsyncSupported(boolean asyncSupported);

    /**
     * Set the auth type.
     *
     * @param authType the auth type.
     */
    void setAuthType(String authType);

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    void setContextPath(String contextPath);

    /**
     * Set the dispatcher type.
     *
     * @param dispatcherType the dispatcher type.
     */
    void setDispatcherType(DispatcherType dispatcherType);

    /**
     * Set the requested session id.
     * 
     * @param requestedSessionId the requested session id. 
     */
    default void setRequestedSessionId(String requestedSessionId) {
    }

    /**
     * Set the servlet path.
     *
     * @param servletPath the servlet path.
     */
    void setServletPath(String servletPath);

    /**
     * Set the user principal.
     *
     * @param userPrincipal the user principal.
     */
    void setUserPrincipal(Principal userPrincipal);

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);

    /**
     * Set the path info.
     * 
     * @param pathInfo the path info.
     */
    default void setPathInfo(String pathInfo) {
    }

    /**
     * Set the query string.
     * 
     * @param queryString the query string.
     */
    default void setQueryString(String queryString) {
    }

    /**
     * Set the web application input stream.
     *
     * @param webApplicationInputStream the web application input stream.
     */
    default void setWebApplicationInputStream(
            WebApplicationInputStream webApplicationInputStream) {
    }
}

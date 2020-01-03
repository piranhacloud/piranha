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
package cloud.piranha.api;

import java.io.Serializable;
import java.util.Collection;

/**
 * The WebXml API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebXml extends Serializable {

    /**
     * Add a context param.
     *
     * @param name the name.
     * @param value the value.
     */
    void addContextParam(String name, String value);
    
    /**
     * Add an error page.
     * 
     * @param errorCode the error code.
     * @param exceptionType the exception type.
     * @param location the location.
     */
    void addErrorPage(String errorCode, String exceptionType, String location);

    /**
     * Add a listener.
     *
     * @param className the class name.
     */
    void addListener(String className);

    /**
     * Add a mime type.
     *
     * @param extension the extension.
     * @param mimeType the mime type.
     */
    void addMimeMapping(String extension, String mimeType);
    
    /**
     * Add a servlet mapping.
     * 
     * @param servletName the servlet name.
     * @param urlPattern the URL pattern.
     */
    void addServletMapping(String servletName, String urlPattern);

    /**
     * Get the context params.
     *
     * @return the context params.
     */
    Collection<WebXmlContextParam> getContextParams();
    
    /**
     * Get the error pages.
     * 
     * @return the error pages.
     */
    Collection<WebXmlErrorPage> getErrorPages();

    /**
     * Get the listeners.
     *
     * @return the listeners.
     */
    Collection<WebXmlListener> getListeners();
    
    /**
     * Get the login config.
     * 
     * @return the login config.
     */
    WebXmlLoginConfig getLoginConfig();

    /**
     * Get the mime mappings.
     *
     * @return the the mime mappings.
     */
    Collection<WebXmlMimeMapping> getMimeMappings();
    
    /**
     * Get the servlet mappings.
     * 
     * @return the servlet mappings.
     */
    Collection<WebXmlServletMapping> getServletMappings();
    
    /**
     * Set the login config.
     * 
     * @param loginConfig the login config.
     */
    void setLoginConfig(WebXmlLoginConfig loginConfig);
}

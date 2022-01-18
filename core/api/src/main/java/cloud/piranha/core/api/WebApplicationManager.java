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
package cloud.piranha.core.api;

import cloud.piranha.resource.api.ResourceManager;

/**
 * The WebApplicationManager API.
 * 
 * <p>
 *  This API makes it possible to access the various managers used to deliver
 *  specific functionality (e.g mime-type handling, welcome-file handling, etc).
 * </p>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationManager {
    
    /**
     * Get the annotation manager.
     * 
     * @return the annotation manager.
     */
    AnnotationManager getAnnotationManager();
    
    /**
     * Get the async manager.
     * 
     * @return the async manager.
     */
    AsyncManager getAsyncManager();
    
    /**
     * Get the error page manager.
     * 
     * @return the error page manager.
     */
    ErrorPageManager getErrorPageManager();
    
    /**
     * Get the HTTP request manager.
     * 
     * @return the HTTP request manager.
     */
    HttpRequestManager getHttpRequestManager();
    
    /**
     * Get the HTTP session manager.
     * 
     * @return the HTTP session manager.
     */
    HttpSessionManager getHttpSessionManager();
    
    /**
     * Get the JSP manager.
     * 
     * @return the JSP manager.
     */
    JspManager getJspManager();
    
    /**
     * Get the locale encoding manager.
     * 
     * @return the locale encoding manager.
     */
    LocaleEncodingManager getLocaleEncodingManager();
    
    /**
     * Get the logging manager.
     * 
     * @return the logging manager.
     */
    LoggingManager getLoggingManager();
    
    /**
     * Get the mime-type manager.
     * 
     * @return the mime-type manager.
     */
    MimeTypeManager getMimeTypeManager();
    
    /**
     * Get the multi-part manager.
     * 
     * @return the multi-part manager.
     */
    MultiPartManager getMultiPartManager();
    
    /**
     * Get the object instance manager.
     * 
     * @return the object instance manager.
     */
    ObjectInstanceManager getObjectInstanceManager();
    
    /**
     * Get the resource manager.
     * 
     * @return the resource manager.
     */
    ResourceManager getResourceManager();
    
    /**
     * Get the security manager.
     * 
     * @return the security manager.
     */
    SecurityManager getSecurityManager();
    
    /**
     * Get the web.xml manager.
     * 
     * @return the web.xml manager.
     */
    WebXmlManager getWebXmlManager();
    
    /**
     * Get the welcome file manager.
     * 
     * @return the welcome file manager.
     */
    WelcomeFileManager getWelcomeFileManager();
    
    /**
     * Set the annotation manager.
     * 
     * @param annotationManager the annotation manager.
     */
    void setAnnotationManager(AnnotationManager annotationManager);
    
    /**
     * Set the async manager.
     * 
     * @param asyncManager the async manager.
     */
    void setAsyncManager(AsyncManager asyncManager);
    
    /**
     * Set the error page manager.
     * 
     * @param errorPageManager the error page manager.
     */
    void setErrorPageManager(ErrorPageManager errorPageManager);
    
    /**
     * Set the HTTP request manager.
     * 
     * @param httpRequestManager the HTTP request manager.
     */
    void setHttpRequestManager(HttpRequestManager httpRequestManager);
    
    /**
     * Set the HTTP session manager.
     * 
     * @param httpSessionManager the HTTP session manager.
     */
    void setHttpSessionManager(HttpSessionManager httpSessionManager);
    
    /**
     * Set the JSP manager.
     * 
     * @param jspManager the JSP manager.
     */
    void setJspManager(JspManager jspManager);
    
    /**
     * Set the locale encoding manager.
     * 
     * @param localeEncodingManager the locale encoding manager.
     */
    void setLocaleEncodingManager(LocaleEncodingManager localeEncodingManager);
    
    /**
     * Set the logging manager.
     * 
     * @param loggingManager the logging manager.
     */
    void setLoggingManager(LoggingManager loggingManager);
    
    /**
     * Set the mime-type manager.
     * 
     * @param mimeTypeManager the mime-type manager.
     */
    void setMimeTypeManager(MimeTypeManager mimeTypeManager);
    
    /**
     * Set the multi-part manager.
     * 
     * @param multiPartManager the multi-part manager.
     */
    void setMultiPartManager(MultiPartManager multiPartManager);
    
    /**
     * Set the object instance manager.
     * 
     * @param objectInstanceManager the object instance manager.
     */
    void setObjectInstanceManager(ObjectInstanceManager objectInstanceManager);
    
    /**
     * Set the resource manager.
     * 
     * @param resourceManager the resource manager.
     */
    void setResourceManager(ResourceManager resourceManager);
    
    /**
     * Set the security manager.
     * 
     * @param securityManager the security manager.
     */
    void setSecurityManager(SecurityManager securityManager);
    
    /**
     * Set the web.xml manager.
     * 
     * @param webXmlManager the web.xml manager.
     */
    void setWebXmlManager(WebXmlManager webXmlManager);
    
    /**
     * Set the welcome file manager.
     * 
     * @param welcomeFileManager the welcome file manager.
     */
    void setWelcomeFileManager(WelcomeFileManager welcomeFileManager);
}

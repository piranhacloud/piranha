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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.AsyncManager;
import cloud.piranha.core.api.DispatcherManager;
import cloud.piranha.core.api.ErrorPageManager;
import cloud.piranha.core.api.HttpSessionManager;
import cloud.piranha.core.api.JspManager;
import cloud.piranha.core.api.LocaleEncodingManager;
import cloud.piranha.core.api.MultiPartManager;
import cloud.piranha.core.api.ObjectInstanceManager;
import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.WelcomeFileManager;
import cloud.piranha.core.api.WebApplicationManager;
import cloud.piranha.core.api.WebXmlManager;
import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.core.api.ServletRequestManager;

/**
 * The default WebApplicationManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationManager implements WebApplicationManager {
    
    /**
     * Stores the annotation manager.
     */
    protected AnnotationManager annotationManager;
    
    /**
     * Stores the async manager.
     */
    protected AsyncManager asyncManager = new DefaultAsyncManager();
    
    /**
     * Stores the dispatcher manager.
     */
    protected DispatcherManager dispatcherManager = new DefaultDispatcherManager();
    
    /**
     * Stores the error page manager.
     */
    protected ErrorPageManager errorPageManager = new DefaultErrorPageManager();
    
    /**
     * Stores the HTTP session manager.
     */
    protected HttpSessionManager httpSessionManager = new DefaultHttpSessionManager();

    /**
     * Stores the JSP manager.
     */
    protected JspManager jspManager = new DefaultJspManager();
    
    /**
     * Stores the locale encoding manager.
     */
    protected LocaleEncodingManager localeEncodingManager = new DefaultLocaleEncodingManager();
    
    /**
     * Stores the multi-part manager.
     */
    protected MultiPartManager multiPartManager = new DefaultMultiPartManager();
    
    /**
     * Stores the object instance manager.
     */
    protected ObjectInstanceManager objectInstanceManager = new DefaultObjectInstanceManager();
    
    /**
     * Stores the resource manager.
     */
    protected ResourceManager resourceManager = new DefaultResourceManager();

    /**
     * Stores the security manager.
     */
    protected SecurityManager securityManager = new DefaultSecurityManager();
    
    /**
     * Stores the servlet request manager.
     */
    protected ServletRequestManager servletRequestManager = new DefaultServletRequestManager();

    /**
     * Stores the web.xml manager.
     */
    protected WebXmlManager webXmlManager;

    /**
     * Stores the welcome file manager.
     */
    protected WelcomeFileManager welcomeFileManager;

    @Override
    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    @Override
    public AsyncManager getAsyncManager() {
        return asyncManager;
    }

    /**
     * Get the dispatcher manager.
     * 
     * @return the dispatcher manager.
     */
    @Override
    public DispatcherManager getDispatcherManager() {
        return dispatcherManager;
    }

    @Override
    public ErrorPageManager getErrorPageManager() {
        return errorPageManager;
    }

    @Override
    public HttpSessionManager getHttpSessionManager() {
        return httpSessionManager;
    }

    @Override
    public JspManager getJspManager() {
        return jspManager;
    }

    @Override
    public LocaleEncodingManager getLocaleEncodingManager() {
        return localeEncodingManager;
    }

    @Override
    public MultiPartManager getMultiPartManager() {
        return multiPartManager;
    }

    @Override
    public ObjectInstanceManager getObjectInstanceManager() {
        return objectInstanceManager;
    }

    @Override
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    @Override
    public ServletRequestManager getServletRequestManager() {
        return servletRequestManager;
    }

    @Override
    public WebXmlManager getWebXmlManager() {
        return webXmlManager;
    }

    @Override
    public WelcomeFileManager getWelcomeFileManager() {
        return welcomeFileManager;
    }

    @Override
    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    @Override
    public void setAsyncManager(AsyncManager asyncManager) {
        this.asyncManager = asyncManager;
    }

    @Override
    public void setErrorPageManager(ErrorPageManager errorPageManager) {
        this.errorPageManager = errorPageManager;
    }

    @Override
    public void setServletRequestManager(ServletRequestManager servletRequestManager) {
        this.servletRequestManager = servletRequestManager;
    }

    @Override
    public void setHttpSessionManager(HttpSessionManager httpSessionManager) {
        this.httpSessionManager = httpSessionManager;
    }

    @Override
    public void setJspManager(JspManager jspManager) {
        this.jspManager = jspManager;
    }

    @Override
    public void setLocaleEncodingManager(LocaleEncodingManager localeEncodingManager) {
        this.localeEncodingManager = localeEncodingManager;
    }

    @Override
    public void setMultiPartManager(MultiPartManager multiPartManager) {
        this.multiPartManager = multiPartManager;
    }

    @Override
    public void setObjectInstanceManager(ObjectInstanceManager objectInstanceManager) {
        this.objectInstanceManager = objectInstanceManager;
    }

    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public void setWebXmlManager(WebXmlManager webXmlManager) {
        this.webXmlManager = webXmlManager;
    }

    @Override
    public void setWelcomeFileManager(WelcomeFileManager welcomeFileManager) {
        this.welcomeFileManager = welcomeFileManager;
    }
}

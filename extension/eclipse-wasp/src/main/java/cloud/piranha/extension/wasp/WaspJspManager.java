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
package cloud.piranha.extension.wasp;

import jakarta.servlet.ServletRegistration;
import jakarta.servlet.descriptor.JspConfigDescriptor;

import cloud.piranha.core.api.JspManager;
import cloud.piranha.core.api.WebApplication;

/**
 * The WaSP manager delivered by the Jasper integration.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WaspJspManager implements JspManager {

    /**
     * Stores the JSP config descriptor.
     */
    protected JspConfigDescriptor jspConfigDescriptor;

    @Override
    public ServletRegistration.Dynamic addJspFile(WebApplication webApplication, String servletName, String jspFile) {
        ServletRegistration.Dynamic registration = webApplication.addServlet(servletName, new WaspServlet(jspFile));
        registration.addMapping(jspFile);
        registration.setInitParameter("classpath", System.getProperty("java.class.path"));
        registration.setInitParameter("compilerSourceVM", "1.8");
        registration.setInitParameter("compilerTargetVM", "1.8");
        return registration;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return jspConfigDescriptor;
    }

    @Override
    public void setJspConfigDescriptor(JspConfigDescriptor jspConfigDescriptor) {
        this.jspConfigDescriptor = jspConfigDescriptor;
    }
}

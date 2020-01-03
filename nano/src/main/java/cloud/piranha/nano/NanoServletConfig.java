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
package cloud.piranha.nano;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * The Nano version of ServletConfig.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class NanoServletConfig implements ServletConfig {
    
    /**
     * Stores the init parameters.
     */
    private final HashMap<String, String> initParameters;
    
    /**
     * Stores the servlet context.
     */
    private final ServletContext servletContext;
    
    /**
     * Stores the servlet name.
     */
    private String servletName;
    
    /**
     * Constructor.
     * 
     * @param servletContext the servlet context.
     */
    public NanoServletConfig(ServletContext servletContext) {
        this.initParameters = new HashMap<>();
        this.servletContext = servletContext;
        this.servletName = "Default";
    }

    /**
     * Get the init parameter.
     * 
     * @param name the name.
     * @return the value, or null if not found.
     */
    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    /**
     * Get the init parameter names.
     * 
     * @return the init parameter names.
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    /**
     * Get the servlet context.
     * 
     * @return the servlet context.
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Get the servlet name.
     * 
     * @return the servlet name.
     */
    @Override
    public String getServletName() {
        return servletName;
    }
    
    /**
     * Set the init parameter.
     * 
     * @param name the name.
     * @param value the value.
     */
    public void setInitParameter(String name, String value) {
        initParameters.put(name, value);
    }

    /**
     * Set the servlet name.
     * 
     * @param servletName the servlet name.
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }
}

/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.nano;

import com.manorrock.piranha.DefaultDirectoryResource;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * The Piranha Nano builder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoPiranhaBuilder {

    /**
     * Stores the configuring servlet flag.
     */
    private boolean configuringServlet = false;

    /**
     * Stores the servlet config.
     */
    private NanoServletConfig servletConfig;

    /**
     * Stores our instance.
     */
    private final NanoPiranha piranha;

    /**
     * Constructor.
     */
    public NanoPiranhaBuilder() {
        piranha = new NanoPiranha();
    }

    /**
     * Build Piranha Nano.
     *
     * @return our instance of Piranha Nano.
     */
    public NanoPiranha build() {
        return piranha;
    }

    /**
     * Add a directory resource.
     *
     * @param directory the directory resource.
     * @return the builder.
     */
    public NanoPiranhaBuilder directoryResource(String directory) {
        piranha.getServletContext().addResource(new DefaultDirectoryResource(directory));
        return this;
    }

    /**
     * Set the servlet.
     *
     * @param servlet the servlet.
     * @return the builder.
     */
    public NanoPiranhaBuilder servlet(Servlet servlet) {
        piranha.setServlet(servlet);
        servletConfig = new NanoServletConfig(piranha.getServletContext());
        this.configuringServlet = true;
        return this;
    }

    /**
     * Set an init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public NanoPiranhaBuilder initParam(String name, String value) {
        if (configuringServlet) {
            servletConfig.setInitParameter(name, value);
        }
        return this;
    }

    /**
     * Initialize the servlet.
     *
     * @return the builder.
     */
    public NanoPiranhaBuilder initServlet() {
        configuringServlet = false;
        try {
            piranha.getServlet().init(servletConfig);
        } catch (ServletException se) {
            throw new RuntimeException(se);
        }
        return this;
    }
}

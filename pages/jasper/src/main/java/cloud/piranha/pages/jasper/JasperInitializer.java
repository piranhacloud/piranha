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
package cloud.piranha.pages.jasper;

import cloud.piranha.api.WebApplication;
import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.jsp.JspFactory;
import org.apache.jasper.runtime.JspFactoryImpl;

/**
 * The Jasper initializer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class JasperInitializer implements ServletContainerInitializer {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(JasperInitializer.class.getName());

    /**
     * Get the WEB-INF/classes directory.
     *
     * @param servletContext the servlet context.
     * @return the classes directory.
     */
    private String getClassesDirectory(ServletContext servletContext) {
        String result = servletContext.getRealPath("/WEB-INF/classes");
        if (result == null) {
            result = "";
        } else {
            result = File.pathSeparator + result;
        }
        return result;
    }

    /**
     * Get the WEB-INF/lib JAR files.
     *
     * @param servletContext the servlet context.
     * @return the location of the JAR files.
     */
    private String getJarFiles(ServletContext servletContext) {
        StringBuilder result = new StringBuilder();
        String realPath = servletContext.getRealPath("/WEB-INF/lib");
        if (realPath != null) {
            File directory = new File(realPath);
            if (directory.isDirectory()) {
                String[] files = directory.list();
                if (files != null) {
                    for (String file : files) {
                        if (file.toLowerCase().endsWith(".jar")) {
                            result.append(File.pathSeparator);
                            result.append(file);
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * Initialize Jasper.
     *
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {
        LOGGER.info("Initializing Jasper integration");
        
        if (JspFactory.getDefaultFactory() == null) {
            JspFactory.setDefaultFactory(new JspFactoryImpl());
        }
        ServletRegistration.Dynamic registration = servletContext.addServlet(
                "JSP Servlet", "org.apache.jasper.servlet.JspServlet");
        registration.addMapping("*.jsp");
        String classpath = System.getProperty("java.class.path")
                + getClassesDirectory(servletContext)
                + getJarFiles(servletContext);
        
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Jasper classpath is: {0}", classpath);
        }
        
        registration.setInitParameter("classpath", classpath);
        registration.setInitParameter("compilerSourceVM", "1.8");
        registration.setInitParameter("compilerTargetVM", "1.8");
        WebApplication webApplication = (WebApplication) servletContext;
        webApplication.setJspManager(new JasperJspManager());
        LOGGER.info("Initializing Jasper integration");
    }
}

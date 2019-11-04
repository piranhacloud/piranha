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
package com.manorrock.piranha.servlet;

import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.manorrock.piranha.DefaultAnnotationManager;
import com.manorrock.piranha.DefaultAnnotationManager.DefaultAnnotationInfo;
import com.manorrock.piranha.api.AnnotationManager;
import com.manorrock.piranha.api.ResourceManagerClassLoader;
import com.manorrock.piranha.api.WebApplication;

/**
 * A naive annotation scanner
 * 
 * <p>
 * This scanner loads all classes scanned, which is normally not-done. It does however make for a 
 * scanner that doesn't need to do byte code tricks and depend on byte code manipulation libs or 
 * any other external libs.
 * 
 * <p>
 * Intended for testing only.
 * 
 * @author Arjan Tijms
 */
public class NaiveAnnotationScannerInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NaiveAnnotationScannerInitializer.class.getName());

    /**
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        WebApplication webApp = (WebApplication) servletContext;
        
        AnnotationManager annotationManager = webApp.getAnnotationManager();
        if (annotationManager instanceof DefaultAnnotationManager == false) {
            LOGGER.warning("DefaultAnnotationManager not installed. This scanner does not work");
            return;
        }
        
        DefaultAnnotationManager defaultAnnotationManager = (DefaultAnnotationManager) annotationManager;
        
        ClassLoader classLoader = webApp.getClassLoader();
        if (classLoader instanceof ResourceManagerClassLoader) {
            ResourceManagerClassLoader resourceManagerClassLoader = (ResourceManagerClassLoader) classLoader;
            
            resourceManagerClassLoader
                .getResourceManager()
                .getAllLocations()
                .filter(e -> e.endsWith(".class"))
                .map(e -> loadClass(classLoader, e))
                .filter(e -> hasWebAnnotation(e))
                .forEach(e -> defaultAnnotationManager.addAnnotation(
                    new DefaultAnnotationInfo<>(
                        e.getAnnotation(WebServlet.class), e)));
                
                
                ;
        }
    }
    
    Class<?> loadClass(ClassLoader classLoader, String name) {
        try {
            return classLoader.loadClass(
                name.replaceAll("/", ".")
                    .substring(1, name.length()-".class".length()));
        } catch (ClassNotFoundException e) {
            // Ignore, for now
        }
        
        return Object.class; // for now, tmp tmp
    }
    
    boolean hasWebAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(WebServlet.class) != null;
    }

}

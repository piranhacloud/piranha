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
package cloud.piranha.webapp.tempdir;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationExtension;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContainerInitializer;

/**
 * The TEMPDIR WebApplicationExtension.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TempDirExtension implements WebApplicationExtension {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(
            TempDirExtension.class.getPackage().getName());

    /**
     * Configure the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        try {
            ClassLoader classLoader = webApplication.getClassLoader();
            
            Class<ServletContainerInitializer> clazz
                    = (Class<ServletContainerInitializer>) classLoader.
                            loadClass(TempDirInitializer.class.getName());
            
            ServletContainerInitializer initializer
                    = clazz.getDeclaredConstructor().newInstance();
            
            webApplication.addInitializer(initializer);
            
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.log(Level.WARNING, "Unable to enable TEMPDIR WebApplicationExtension", ex);
        }
    }
}

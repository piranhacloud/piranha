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
package cloud.piranha;

import java.util.ArrayList;
import java.util.List;
import cloud.piranha.api.WebApplicationExtension;
import cloud.piranha.api.WebApplicationExtensionContext;

/**
 * The default web application extension context.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationExtensionContext implements WebApplicationExtensionContext {

    /**
     * Stores the exteions.
     */
    private final ArrayList<WebApplicationExtension> extensions;
    
    /**
     * Stores the extension clasess.
     */
    private final ArrayList<Class<? extends WebApplicationExtension>> extensionClasses;
   
    /**
     * Constructor.
     */
    public DefaultWebApplicationExtensionContext() {
        extensions = new ArrayList<>();
        extensionClasses = new ArrayList<>();
    }
    
    /**
     * Get the extensions.
     * 
     * @return the extensions.
     */
    public List<WebApplicationExtension> getExtensions() {
        return extensions;
    }

    /**
     * Add the extension.
     * 
     * @param extension the extension.
     */
    @Override
    public void add(Class<? extends WebApplicationExtension> extension) {
        try {
            WebApplicationExtension instance = extension.newInstance();
            instance.extend(this);
            extensions.add(instance);
            extensionClasses.add(extension);
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Remove the extension.
     * 
     * @param extension the extension. 
     */
    @Override
    public void remove(Class<? extends WebApplicationExtension> extension) {
        if (extensionClasses.contains(extension)) {
            int index = extensionClasses.indexOf(extension);
            extensionClasses.remove(index);
            extensions.remove(index);
        }
    }
}

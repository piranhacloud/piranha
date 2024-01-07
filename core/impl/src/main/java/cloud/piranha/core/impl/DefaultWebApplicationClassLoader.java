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

import cloud.piranha.core.api.WebApplicationClassLoader;
import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.impl.AliasedNamedResource;
import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.resource.impl.DefaultResourceManagerClassLoader;
import cloud.piranha.resource.impl.DirectoryResource;
import cloud.piranha.resource.impl.JarResource;
import cloud.piranha.resource.impl.MultiReleaseResource;
import java.io.File;

/**
 * The default WebApplicationClassLoader.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationClassLoader extends DefaultResourceManagerClassLoader implements WebApplicationClassLoader {

    /**
     * Constructor.
     */
    public DefaultWebApplicationClassLoader() {
    }

    /**
     * Constructor.
     *
     * @param baseDirectory the base directory.
     */
    public DefaultWebApplicationClassLoader(File baseDirectory) {
        ResourceManager resourceManager = new DefaultResourceManager();
        File classesDirectory = new File(baseDirectory, "WEB-INF/classes");
        if (classesDirectory.exists()) {
            resourceManager.addResource(new MultiReleaseResource(new AliasedNamedResource(new DirectoryResource(classesDirectory), "cloud.piranha.modular.classes")));
        }

        File libDirectory = new File(baseDirectory, "WEB-INF/lib");
        if (libDirectory.exists()) {
            File[] jarFiles = libDirectory.listFiles();
            if (jarFiles != null) {
                for (File jarFile : jarFiles) {
                    resourceManager.addResource(new MultiReleaseResource(new JarResource(jarFile)));
                }
            }
        }

        setResourceManager(resourceManager);
    }

}

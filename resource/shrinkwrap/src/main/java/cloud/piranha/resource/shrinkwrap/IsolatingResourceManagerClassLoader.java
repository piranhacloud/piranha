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
package cloud.piranha.resource.shrinkwrap;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.impl.DefaultResourceManagerClassLoader;

/**
 * The default WebApplicationClassLoader.
 *
 * @author Arjan Tijms
 */
public class IsolatingResourceManagerClassLoader extends DefaultResourceManagerClassLoader {

    /**
     * Stores the Shrinkwrap package prefix.
     */
    private static final String SHRINKWRAP_PACKAGE_PREFIX = "org.jboss.shrinkwrap";

    /**
     * Stores the system classloader.
     */
    private final ClassLoader systemClassLoader;

    /**
     * Stores the classloader id.
     */
    private final String classLoaderId;

    /**
     * Constructor.
     */
    public IsolatingResourceManagerClassLoader() {
        this("");
    }

    /**
     * Constructor.
     *
     * @param classLoaderId the id for the class loader.
     */
    public IsolatingResourceManagerClassLoader(String classLoaderId) {
        this(getSystemClassLoader().getParent(), classLoaderId);
    }

    /**
     * Constructor.
     *
     * @param classLoader the delegate class loader.
     * @param classLoaderId the id for the class loader.
     */
    public IsolatingResourceManagerClassLoader(ClassLoader classLoader, String classLoaderId) {
        super(classLoader);
        this.systemClassLoader = getSystemClassLoader();
        this.classLoaderId = classLoaderId;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.startsWith(SHRINKWRAP_PACKAGE_PREFIX)) {
            return systemClassLoader.loadClass(name);
        }

        return super.loadClass(name, resolve);
    }

    @Override
    public URL getResource(String name) {
        if (name.startsWith(SHRINKWRAP_PACKAGE_PREFIX)) {
            return systemClassLoader.getResource(name);
        }

        return super.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        if (name.startsWith(SHRINKWRAP_PACKAGE_PREFIX)) {
            return systemClassLoader.getResources(name);
        }

        return super.getResources(name);
    }

    /**
     * {@return the classloader id}
     */
    public String getClassLoaderId() {
        return classLoaderId;
    }

    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        resourceManager.setAlsoTryLoadFromClass(false);
        super.setResourceManager(resourceManager);
    }

}

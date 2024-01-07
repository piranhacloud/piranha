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
package cloud.piranha.resource.impl;

import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.api.ResourceManagerClassLoader;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import static java.util.Collections.enumeration;
import static java.util.Collections.list;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default ResourceManagerClassLoader.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultResourceManagerClassLoader extends ClassLoader implements ResourceManagerClassLoader {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(DefaultResourceManagerClassLoader.class.getName());
    
    /**
     * Stores the 'Unable to load class: ' message prefix.
     */
    private static final String UNABLE_TO_LOAD_CLASS = "Unable to load class: ";

    /**
     * Set that keeps a list of classes we know aren't there, so we don't have to search for them again.
     */
    private Set<String> notFoundClasses = ConcurrentHashMap.newKeySet();

    /**
     * Stores the resource manager.
     */
    private ResourceManager resourceManager;

    /**
     * Stores the delegate class loader.
     */
    private ClassLoader delegateClassLoader;

    /**
     * Stores the loaded classes.
     */
    private final ConcurrentHashMap<String, Class<?>> classes = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public DefaultResourceManagerClassLoader() {
        this(getSystemClassLoader()); // Calls the other constructor
    }

    /**
     * Another Constructor.
     *
     * @param delegateClassLoader classloader which is consulted first
     */
    public DefaultResourceManagerClassLoader(ClassLoader delegateClassLoader) {
        this(delegateClassLoader, null);
    }

    /**
     * Yet another Constructor.
     *
     * @param resourceManager the resource manager.
     */
    public DefaultResourceManagerClassLoader(ResourceManager resourceManager) {
        this(null, resourceManager);
    }

    /**
     * Yet another Constructor.
     * @param classLoader the class loader.
     * @param resourceManager the resource manager.
     */
    public DefaultResourceManagerClassLoader(ClassLoader classLoader, ResourceManager resourceManager) {
        super(null); // Calls the super constructor
        this.delegateClassLoader = classLoader;
        this.resourceManager = resourceManager; // Assigns the resource manager
    }

    /**
     * Load the class.
     *
     * @param name the name.
     * @param resolve the resolve flag.
     * @return the class.
     * @throws ClassNotFoundException when the class cannot be found.
     */
    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result;

        try {
            result = delegateClassLoader.loadClass(name);
        } catch (ClassNotFoundException cnfe) {
            result = null;
        }

        if (result == null) {
            try {
                if (classes.containsKey(name)) {
                    return classes.get(name);
                }

                result = internalLoadClass(name, resolve);
            } catch (Throwable throwable) {
                throw new ClassNotFoundException(UNABLE_TO_LOAD_CLASS + name, throwable);
            }
        }

        if (result == null) {
            if (notFoundClasses.contains(name)) {
                throw new ClassNotFoundException("Unable to load previosly failed to find class: " + name);
            }

            notFoundClasses.add(name);

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != this) {
                try {
                    result = contextClassLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                    // Ignore, throw our own one if needed
                }
                if (result != null) {
                    notFoundClasses.remove(name);
                }
            }
        }

        if (result == null) { // Checks if the result is null
            throw new ClassNotFoundException(UNABLE_TO_LOAD_CLASS + name);
        }

        return result; // Returns the result
    }

    @Override
    protected Class<?> findClass(String moduleName, String name) {
        try {
            Class<?> loadedClass = loadClass(name);
            String loadedModuleName = loadedClass.getModule().getName();
            if (loadedModuleName != null && loadedModuleName.equals(moduleName)) {
                return loadedClass;
            }
        } catch (ClassNotFoundException ignored) {
        }

        return null; // Returns the null
    }

    /**
     * Inner load class.
     *
     * @param name the name.
     * @param resolve the resolve flog.
     * @return the class.
     */
    protected Class<?> internalLoadClass(String name, boolean resolve) {
        Class<?> result = null;
        try {
            // Check with the super class. This can contain dynamic classes
            // that have been "hacked" into our classloader by e.g. Weld or
            // Javasist.
            try {
                result = super.loadClass(name, resolve);
            } catch (ClassNotFoundException cnfe) {
                // Ignore
            }

            if (result == null) {

                // Define class

                byte[] bytes = null;
                try (InputStream resourceStream = resourceManager.getResourceAsStream(normalizeName(name))) {
                    if (resourceStream == null) {
                        return null;
                    }

                    bytes = readClassBytes(resourceStream);
                }

                synchronized (this) {
                    result = classes.get(name);

                    if (result == null) {
                        result = internalDefineClass(name, bytes, resolve);
                        classes.put(name, result);
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new IllegalStateException(UNABLE_TO_LOAD_CLASS + name, throwable);
        }

        return result;
    }

    @Override
    public URL getResource(String name) {
        URL resource = findResource(name);

        if (resource == null) {
            resource = delegateClassLoader.getResource(name);
        }

        return resource;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        // Assume for now amount of resources is reasonably small to afford allocating
        // new collections.
        List<URL> resources = list(delegateClassLoader.getResources(name));

        resources.addAll(list(findResources(name)));

        return enumeration(resources);
    }

    @Override
    protected URL findResource(String name) {
        URL result = null;
        try {
            result = resourceManager.getResource(name);
        } catch (MalformedURLException mue) {
            LOGGER.log(DEBUG, "Malformed URL used to find resource", mue);
        }
        return result;
    }

    @Override
    protected URL findResource(String moduleName, String name) throws IOException {
        URL result = null;
        try {
            result = resourceManager.getResource(name);
        } catch (MalformedURLException mue) {
            LOGGER.log(DEBUG, "Malformed URL used to find resource", mue);
        }
        return result;
    }

    /**
     * Find the resources.
     *
     * @param name the name of the resource.
     * @return the enumeration of the resource urls.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    protected Enumeration<URL> findResources(String name) throws IOException  {
        return enumeration(resourceManager.getResources(name));
    }

    /**
     * Set the resource manager.
     *
     * @param resourceManager the resource manager.
     */
    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Set the delegate classloader.
     *
     * @param delegateClassLoader the delegate class loader.
     */
    public void setDelegateClassLoader(ClassLoader delegateClassLoader) {
        this.delegateClassLoader = delegateClassLoader;
    }

    /**
     * {@return the delegate classloader}
     */
    public ClassLoader getDelegateClassLoader() {
        return delegateClassLoader;
    }

    /**
     * Normalize the name to a .class name.
     *
     * @param name the name.
     * @return the .class name.
     */
    protected String normalizeName(String name) {
        return name.replace(".", "/") + ".class";
    }

    /**
     * Read the class bytes from the input stream.
     *
     * @param resourceStream the input stream.
     * @return the bytes.
     * @throws IOException when an I/O error occurs.
     */
    protected byte[] readClassBytes(InputStream resourceStream) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(resourceStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = inputStream.read();
        while (read != -1) {
            outputStream.write((byte) read);
            read = inputStream.read();
        }

        return outputStream.toByteArray();
    }

    /**
     * Define the class.
     *
     * @param name the name.
     * @param bytes the bytes.
     * @param resolve the resolve flag.
     * @return the class.
     */
    protected Class<?> internalDefineClass(String name, byte[] bytes, boolean resolve) {

        CodeSource codeSource = new CodeSource(getResource(normalizeName(name)), (CodeSigner[]) null);
        ProtectionDomain protectionDomain = new ProtectionDomain(codeSource, null);
        Class<?> result = defineClass(name, bytes, 0, bytes.length, protectionDomain);


        // Define package

        String packageName = null;
        int lastDotPosition = name.lastIndexOf('.');
        if (lastDotPosition != -1) {
            packageName = name.substring(0, lastDotPosition);
        }

        if (packageName != null) {
            Package classPackage = getDefinedPackage(packageName);

            if (classPackage == null) {
                try {
                    definePackage(packageName, null, null, null, null, null, null, null);
                } catch (IllegalArgumentException e) {
                    // Ignore, package already defined
                }
            }
        }

        if (resolve) {
            resolveClass(result);
        }

        return result;
    }
}

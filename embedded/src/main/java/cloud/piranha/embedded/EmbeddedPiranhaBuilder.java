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
package cloud.piranha.embedded;

import cloud.piranha.core.api.HttpSessionManager;
import cloud.piranha.core.api.PiranhaBuilder;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import cloud.piranha.resource.api.Resource;
import cloud.piranha.resource.impl.AliasedDirectoryResource;
import cloud.piranha.resource.impl.DirectoryResource;
import cloud.piranha.resource.impl.StringResource;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletRegistration;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The builder so you can easily build instances of
 * {@link cloud.piranha.embedded.EmbeddedPiranha}.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.embedded.EmbeddedPiranha
 */
public class EmbeddedPiranhaBuilder implements PiranhaBuilder<EmbeddedPiranha> {

    /**
     * Stores the async supported flags.
     */
    private final Map<String, Boolean> asyncSupportedServlets;

    /**
     * Stores the attributes.
     */
    private final Map<String, Object> attributes;
    
    /**
     * Stores the class loader.
     */
    private ClassLoader classLoader;

    /**
     * Stores the extension.
     */
    private List<Class<? extends WebApplicationExtension>> extensionClasses;

    /**
     * Stores the initializers.
     */
    private final List<String> initializers;

    /**
     * Stores the filters.
     */
    private final Map<String, String> filters;

    /**
     * Stores the filter init parameters map.
     */
    private final Map<String, HashMap<String, String>> filterInitParameters;

    /**
     * Stores the filter mappings.
     */
    private final Map<String, List<String>> filterMappings;

    /**
     * Stores the HTTP session manager.
     */
    private HttpSessionManager httpSessionManager;

    /**
     * Stores the listeners.
     */
    private final ArrayList<String> listeners;

    /**
     * Stores the resources.
     */
    private final ArrayList<Resource> resources;

    /**
     * Stores the servlets.
     */
    private final LinkedHashMap<String, String> servlets;

    /**
     * Stores the servlet init parameters map.
     */
    private final LinkedHashMap<String, HashMap<String, String>> servletInitParameters;

    /**
     * Stores the servlet mappings.
     */
    private final LinkedHashMap<String, List<String>> servletMappings;

    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Constructor.
     */
    public EmbeddedPiranhaBuilder() {
        asyncSupportedServlets = new LinkedHashMap<>();
        attributes = new LinkedHashMap<>();
        extensionClasses = new ArrayList<>();
        filters = new LinkedHashMap<>();
        filterInitParameters = new LinkedHashMap<>();
        filterMappings = new LinkedHashMap<>();
        initializers = new ArrayList<>();
        listeners = new ArrayList<>();
        resources = new ArrayList<>();
        servlets = new LinkedHashMap<>();
        servletInitParameters = new LinkedHashMap<>();
        servletMappings = new LinkedHashMap<>();
    }

    /**
     * Add an aliased directory resource.
     *
     * @param path the path.
     * @param alias the alias.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder aliasedDirectoryResource(String path, String alias) {
        resources.add(new AliasedDirectoryResource(new File(path), alias));
        return this;
    }

    /**
     * Add an attribute.
     *
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder attribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    /**
     * Build the Piranha Embedded instance.
     *
     * @return the instance.
     */
    public EmbeddedPiranha build() {
        EmbeddedPiranha piranha;

        if (webApplication == null) {
            piranha = new EmbeddedPiranha();
            webApplication = piranha.getWebApplication();
        } else {
            piranha = new EmbeddedPiranha(webApplication);
        }
        
        if (classLoader != null) {
            webApplication.setClassLoader(classLoader);
        }

        if (extensionClasses != null && !extensionClasses.isEmpty()) {
            DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
            for (Class<? extends WebApplicationExtension> extensionClass : extensionClasses) {
                context.add(extensionClass);
            }
            context.configure(webApplication);
        }

        if (httpSessionManager != null) {
            webApplication.getManager().setHttpSessionManager(httpSessionManager);
            httpSessionManager.setWebApplication(webApplication);
        }

        attributes.entrySet().forEach(attribute -> {
            String attributeName = attribute.getKey();
            Object attributeValue = attribute.getValue();
            webApplication.setAttribute(attributeName, attributeValue);
        });

        resources.forEach(webApplication::addResource);

        initializers.forEach(webApplication::addInitializer);

        listeners.forEach(webApplication::addListener);

        servlets.entrySet().forEach(entry -> {
            String servletName = entry.getKey();
            String className = entry.getValue();
            ServletRegistration.Dynamic servlet = webApplication.addServlet(servletName, className);
            HashMap<String, String> initParameters = servletInitParameters.get(servletName);
            if (initParameters != null) {
                initParameters.entrySet().forEach(initParameter -> {
                    String name = initParameter.getKey();
                    String value = initParameter.getValue();
                    servlet.setInitParameter(name, value);
                });
            }
            servlet.setAsyncSupported(asyncSupportedServlets.get(servletName));
        });
        servletMappings.entrySet().forEach(servletMapping -> {
            String servletName = servletMapping.getKey();
            List<String> urlPatterns = servletMapping.getValue();
            ServletRegistration servlet = webApplication.getServletRegistration(servletName);
            servlet.addMapping(urlPatterns.toArray(new String[0]));
        });

        filters.entrySet().forEach(entry -> {
            String filterName = entry.getKey();
            String className = entry.getValue();
            FilterRegistration.Dynamic filter = webApplication.addFilter(filterName, className);
            HashMap<String, String> initParameters = filterInitParameters.get(filterName);
            if (initParameters != null) {
                initParameters.entrySet().forEach(initParameter -> {
                    String name = initParameter.getKey();
                    String value = initParameter.getValue();
                    filter.setInitParameter(name, value);
                });
            }
        });
        filterMappings.entrySet().forEach(filterMapping -> {
            String filterName = filterMapping.getKey();
            List<String> urlPatterns = filterMapping.getValue();
            FilterRegistration filter = webApplication.getFilterRegistration(filterName);
            filter.addMappingForUrlPatterns(null, false, urlPatterns.toArray(new String[0]));
        });

        webApplication.initializeInitializers();
        webApplication.initializeFilters();
        webApplication.initializeServlets();
        webApplication.initializeFinish();

        return piranha;
    }

    /**
     * Build and starts the Piranha Embedded instance.
     *
     * @return the instance.
     */
    public EmbeddedPiranha buildAndStart() {
        return build()
                .start();
    }
    
    /**
     * Set the class loader.
     * 
     * @param classLoader the class loader.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    /**
     * Add a directory resource.
     *
     * @param path the path.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder directoryResource(String path) {
        resources.add(new DirectoryResource(path));
        return this;
    }

    /**
     * Set the web application extension.
     *
     * @param extensionClass the extension class.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder extension(Class<? extends WebApplicationExtension> extensionClass) {
        extensionClasses.add(extensionClass);
        return this;
    }

    /**
     * Set the web application extensions.
     *
     * @param extensionClasses the extension classes.
     * @return the builder.
     */
    @SafeVarargs
    public final EmbeddedPiranhaBuilder extensions(Class<? extends WebApplicationExtension>... extensionClasses) {
        for (Class<? extends WebApplicationExtension> extensionClass : extensionClasses) {
            extension(extensionClass);
        }
        return this;
    }

    /**
     * Add a filter.
     *
     * @param filterName the filter name.
     * @param filterClass the filter class.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filter(String filterName, Class<?> filterClass) {
        filters.put(filterName, filterClass.getName());
        return this;
    }

    /**
     * Add a filter.
     *
     * @param filterName the filter name.
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filter(String filterName, String className) {
        filters.put(filterName, className);
        return this;
    }

    /**
     * Set a filter init parameter.
     *
     * @param filterName the filter name.
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filterInitParam(String filterName, String name, String value) {
        HashMap<String, String> initParameters = filterInitParameters.get(filterName);
        if (initParameters == null) {
            initParameters = new HashMap<>();
            filterInitParameters.put(filterName, initParameters);
        }
        initParameters.put(name, value);
        return this;
    }

    /**
     * Add a filter mapping.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filterMapping(String filterName, String... urlPatterns) {
        filterMappings.put(filterName, Arrays.asList(urlPatterns));
        return this;
    }

    /**
     * Set the HTTP session manager.
     *
     * @param httpSessionManager the HTTP session manager.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder httpSessionManager(HttpSessionManager httpSessionManager) {
        this.httpSessionManager = httpSessionManager;
        return this;
    }

    /**
     * Add an initializer.
     *
     * @param initializerClass the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder initializer(Class<?> initializerClass) {
        initializers.add(initializerClass.getName());
        return this;
    }

    /**
     * Add an initializer.
     *
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder initializer(String className) {
        initializers.add(className);
        return this;
    }

    /**
     * Add initializers.
     *
     * @param initializerClasses the classes
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder initializers(Class<?>... initializerClasses) {
        for (Class<?> initializerClass : initializerClasses) {
            initializers.add(initializerClass.getName());
        }
        return this;
    }

    /**
     * Add a listeners.
     *
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder listener(String className) {
        listeners.add(className);
        return this;
    }

    private EmbeddedPiranhaBuilder processServletsMapped(Object... objects) {
        for (int i = 0; i < objects.length; i += 2) {
            Class<?> servletClass = (Class<?>) objects[i];
            String urlPattern = (String) objects[i + 1];
            servlet(servletClass.getSimpleName(), servletClass.getName(), false);
            servletMapping(servletClass.getSimpleName(), urlPattern);
        }
        return this;
    }

    /**
     * Add a servlet
     *
     * @param servletName the servlet name.
     * @param servletClass the servlet class.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servlet(String servletName, Class<?> servletClass) {
        return servlet(servletName, servletClass.getName(), false);
    }

    /**
     * Add a servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servlet(String servletName, String className) {
        return servlet(servletName, className, false);
    }

    /**
     * Add a servlet.
     *
     * @param servletName the servlet name.
     * @param servletClass the servlet class.
     * @param asyncSupported the async supported flag.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servlet(String servletName, Class<?> servletClass, boolean asyncSupported) {
        return servlet(servletName, servletClass.getName(), asyncSupported);
    }

    /**
     * Add a servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @param asyncSupported the async supported flag.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servlet(String servletName, String className, boolean asyncSupported) {
        servlets.put(servletName, className);
        asyncSupportedServlets.put(servletName, asyncSupported);
        return this;
    }

    /**
     * Set a servlet init parameter.
     *
     * @param servletName the servlet name.
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletInitParam(String servletName, String name, String value) {
        HashMap<String, String> initParameters = servletInitParameters.get(servletName);
        if (initParameters == null) {
            initParameters = new HashMap<>();
            servletInitParameters.put(servletName, initParameters);
        }
        initParameters.put(name, value);
        return this;
    }

    /**
     * Add a servlet and a servlet mapping.
     *
     * @param servletClass the servlet class.
     * @param urlPatterns the URL patterns
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletMapped(Class<?> servletClass, String... urlPatterns) {
        servlet(servletClass.getSimpleName(), servletClass.getName(), false);
        return servletMapping(servletClass.getSimpleName(), urlPatterns);
    }

    /**
     * Add a servlet and a servlet mapping.
     *
     * @param servletClass the servlet class.
     * @param asyncSupported the async supported flag.
     * @param urlPatterns the URL patterns
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletMapped(Class<?> servletClass, boolean asyncSupported, String... urlPatterns) {
        servlet(servletClass.getSimpleName(), servletClass.getName(), asyncSupported);
        return servletMapping(servletClass.getSimpleName(), urlPatterns);
    }

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletMapping(String servletName, String... urlPatterns) {
        servletMappings.put(servletName, Arrays.asList(urlPatterns));
        return this;
    }

    /**
     * Add servlets and their servlet mapping.
     *
     * @param servletClass1 the first servlet class.
     * @param urlPattern1 the first URL pattern.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletsMapped(Class<?> servletClass1, String urlPattern1) {
        return processServletsMapped(
                servletClass1, urlPattern1);
    }

    /**
     * Add servlets and their servlet mapping.
     *
     * @param servletClass1 the first servlet class.
     * @param urlPattern1 the first URL pattern.
     * @param servletClass2 the second servlet class.
     * @param urlPattern2 the second URL pattern.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletsMapped(Class<?> servletClass1, String urlPattern1, Class<?> servletClass2, String urlPattern2) {
        return processServletsMapped(
                servletClass1, urlPattern1, servletClass2, urlPattern2);
    }

    /**
     * Add servlets and their servlet mapping.
     *
     * @param servletClass1 the first servlet class.
     * @param urlPattern1 the first URL pattern.
     * @param servletClass2 the second servlet class.
     * @param urlPattern2 the second URL pattern.
     * @param servletClass3 the third servlet class.
     * @param urlPattern3 the third URL pattern.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletsMapped(Class<?> servletClass1, String urlPattern1, Class<?> servletClass2, String urlPattern2,
            Class<?> servletClass3, String urlPattern3) {
        return processServletsMapped(
                servletClass1, urlPattern1, servletClass2, urlPattern2, servletClass3, urlPattern3);
    }

    /**
     * Add servlets and their servlet mapping.
     *
     * @param servletClass1 the first servlet class.
     * @param urlPattern1 the first URL pattern.
     * @param servletClass2 the second servlet class.
     * @param urlPattern2 the second URL pattern.
     * @param servletClass3 the third servlet class.
     * @param urlPattern3 the third URL pattern.
     * @param servletClass4 the fourth servlet class.
     * @param urlPattern4 the fourth URL pattern.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletsMapped(Class<?> servletClass1, String urlPattern1, Class<?> servletClass2, String urlPattern2,
            Class<?> servletClass3, String urlPattern3, Class<?> servletClass4, String urlPattern4) {
        return processServletsMapped(
                servletClass1, urlPattern1, servletClass2, urlPattern2, servletClass3, urlPattern3, servletClass4, urlPattern4);
    }

    /**
     * Add servlets and their servlet mapping.
     *
     * @param servletClass1 the first servlet class.
     * @param urlPattern1 the first URL pattern.
     * @param servletClass2 the second servlet class.
     * @param urlPattern2 the second URL pattern.
     * @param servletClass3 the third servlet class.
     * @param urlPattern3 the third URL pattern.
     * @param servletClass4 the fourth servlet class.
     * @param urlPattern4 the fourth URL pattern.
     * @param servletClass5 the fifth servlet class.
     * @param urlPattern5 the fifth URL pattern.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletsMapped(Class<?> servletClass1, String urlPattern1, Class<?> servletClass2, String urlPattern2,
            Class<?> servletClass3, String urlPattern3, Class<?> servletClass4, String urlPattern4, Class<?> servletClass5, String urlPattern5) {
        return processServletsMapped(
                servletClass1, urlPattern1, servletClass2, urlPattern2, servletClass3, urlPattern3, servletClass4, urlPattern4, servletClass5, urlPattern5);
    }

    /**
     * Add a string resource.
     *
     * @param path the path.
     * @param value the string value added under the given path.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder stringResource(String path, String value) {
        resources.add(new StringResource(path, value));
        return this;
    }

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder webApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
        return this;
    }
}

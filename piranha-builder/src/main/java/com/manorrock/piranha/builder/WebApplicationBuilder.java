package com.manorrock.piranha.builder;

import java.util.Set;

import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.api.Resource;
import com.manorrock.piranha.api.WebApplication;

public class WebApplicationBuilder {

    private final WebApplication webApplication;

    public static WebApplicationBuilder newWebApplication() {
        return new WebApplicationBuilder();
    }
    
    public static WebApplicationBuilder newWebApplication(WebApplication webApplication) {
        return new WebApplicationBuilder(webApplication);
    }
    
    public WebApplicationBuilder() {
        this(new DefaultWebApplication());
    }
    
    public WebApplicationBuilder(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    public WebApplicationBuilder addResource(Resource resource) {
        webApplication.addResource(resource);

        return this;
    }

    public WebApplicationBuilder addInitializer(Class<?> clazz) {
        webApplication.addInitializer(clazz.getName());

        return this;
    }

    public WebApplicationBuilder addInitializer(Class<?> clazz, Set<Class<?>> classes) {
        webApplication.addInitializer(clazz.getName(), classes);

        return this;
    }

    public WebApplicationBuilder addInitializer(String className) {
        webApplication.addInitializer(className);

        return this;
    }

    public WebApplicationBuilder addInitializer(String className, Set<Class<?>> classes) {
        webApplication.addInitializer(className, classes);

        return this;
    }

    public WebApplication start() {
        webApplication.initialize();
        webApplication.start();

        return webApplication;
    }

}

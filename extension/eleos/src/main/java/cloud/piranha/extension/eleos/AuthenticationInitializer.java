/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.eleos;

import static jakarta.security.auth.message.config.AuthConfigFactory.DEFAULT_FACTORY_SECURITY_PROPERTY;
import static java.lang.System.Logger.Level.INFO;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readString;
import static java.util.Arrays.stream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.omnifaces.eleos.config.factory.ConfigParser;
import org.omnifaces.eleos.config.factory.DefaultConfigFactory;
import org.omnifaces.eleos.config.factory.DefaultConfigParser;
import org.omnifaces.eleos.config.helper.Caller;
import org.omnifaces.eleos.services.DefaultAuthenticationService;
import org.omnifaces.eleos.services.InMemoryStore;

import cloud.piranha.extension.webxml.WebXml;
import cloud.piranha.extension.webxml.WebXmlLoginConfig;
import cloud.piranha.extension.webxml.WebXmlManager;
import cloud.piranha.core.api.AuthenticatedIdentity;
import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultAuthenticatedIdentity;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The Eleos initializer.
 *
 * @author Arjan Tijms
 */
public class AuthenticationInitializer implements ServletContainerInitializer {

    /**
     * Stores the auth module class name.
     */
    public static final String AUTH_MODULE_CLASS = AuthenticationInitializer.class.getName() + ".auth.module.class";

    /**
     * Stores the auth service name.
     */
    public static final String AUTH_SERVICE = AuthenticationInitializer.class.getName() + ".auth.service";

    /**
     * Stores the logger.
     */
    public static final Logger LOGGER = System.getLogger(AuthenticationInitializer.class.getName());

    /**
     * Initialize Eleos
     *
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {

        LOGGER.log(INFO, "*** Initializing Authentication ***");

        String appContextId = servletContext.getVirtualServerName() + " " + servletContext.getContextPath();

        // This sets the authentication factory to the default factory. This factory stores and retrieves
        // the authentication artifacts.
        Security.setProperty(DEFAULT_FACTORY_SECURITY_PROPERTY, DefaultConfigFactory.class.getName());

        Map<String, Object> options = new HashMap<>();

        // Gets the authentication module class that was configured externally
        Class<?> authModuleClass = getAuthModuleClass(servletContext, options);

        // Defines the modules that we have available. Here it's only a single fixed module.
        ConfigParser configParser = new DefaultConfigParser(authModuleClass);

        // Indicates the module we want to use
        options.put("authModuleId", authModuleClass.getSimpleName());

        // This authentication service installs an authentication config provider in the default factory, which
        // is the one we setup above. This authentication config provider uses the passed-in configParser to
        // retrieve configuration for authentication modules from.
        DefaultAuthenticationService authenticationService = new DefaultAuthenticationService(appContextId, options, configParser, null);

        servletContext.setAttribute(AUTH_SERVICE, authenticationService);

        initIdentityStore(servletContext);

        setUsernamePasswordLoginHandler(servletContext, authenticationService);

        addFilter(servletContext, AuthenticationFilter.class);
    }

    private void setUsernamePasswordLoginHandler(ServletContext servletContext, DefaultAuthenticationService authenticationService) {
        WebApplication webApplication = (WebApplication) servletContext;

        webApplication.getManager(SecurityManager.class).setUsernamePasswordLoginHandler(
            (request, username, password) -> callerToIdentity(authenticationService.login(username, password))
        );
    }

    private AuthenticatedIdentity callerToIdentity(Caller caller) {
        if (caller == null) {
            return null;
        }

        return new DefaultAuthenticatedIdentity(caller.getCallerPrincipal(), caller.getGroups());
    }

    private Class<?> getAuthModuleClass(ServletContext servletContext, Map<String, Object> options) {
        Class<?> authModuleClass = (Class<?>) servletContext.getAttribute(AUTH_MODULE_CLASS);
        if (authModuleClass == null) {
            authModuleClass = DoNothingServerAuthModule.class;
            WebXmlLoginConfig loginConfig = getLoginConfig(servletContext);
            if (loginConfig != null) {
                options.put("authMethod", loginConfig.authMethod());
                options.put("realmName", loginConfig.realmName());
                options.put("formLoginPage", loginConfig.formLoginPage());
                options.put("formErrorPage", loginConfig.formErrorPage());
            }
        }

        return authModuleClass;
    }

    private WebXmlLoginConfig getLoginConfig(ServletContext servletContext) {
        WebXmlManager manager = (WebXmlManager) servletContext.getAttribute(WebXmlManager.KEY);
        WebXml webXml = manager.getWebXml();
        if (!isAnyNull(() -> webXml, () -> webXml.getLoginConfig(), () -> webXml.getLoginConfig().authMethod())) {
            return webXml.getLoginConfig();
        }

        return null;
    }

    void initIdentityStore(ServletContext servletContext) throws ServletException {
        try {
            // Try system property first, it overrides all
            String callers = System.getProperty("io.piranha.identitystore.callers");
            if (callers == null) {
                // Try the web archive next, it overrides global
                InputStream xmlStream = servletContext.getResourceAsStream("WEB-INF/piranha-callers.xml");
                if (xmlStream != null) {
                    callers = new String(xmlStream.readAllBytes(), UTF_8);
                }
                // Try global (inside the installed server) last
                if (callers == null) {
                    Path callerPath = Paths.get("etc/piranha-callers.xml");
                    if (exists(callerPath)) {
                        callers = readString(callerPath);
                    }
                }
            }

            InMemoryStore.initFromString(callers);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @SafeVarargs
    private boolean isAnyNull(Supplier<Object>... suppliers) {
        return stream(suppliers)
                .filter(e -> e.get() == null)
                .findFirst()
                .isPresent();
    }

    private void addFilter(ServletContext servletContext, Class<?> filterClass) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        FilterRegistration.Dynamic dynamic = servletContext.addFilter(filterClass.getSimpleName(), (Class) filterClass);
        dynamic.setAsyncSupported(true);
        ((WebApplication) servletContext).addFilterMapping(filterClass.getSimpleName(), "/*");
    }

}

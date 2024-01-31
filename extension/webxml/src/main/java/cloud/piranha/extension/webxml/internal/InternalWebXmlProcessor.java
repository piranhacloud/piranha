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
package cloud.piranha.extension.webxml.internal;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.Logger.Level.WARNING;
import static java.util.stream.Collectors.toSet;

import java.lang.System.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.CommonDataSource;

import cloud.piranha.core.api.ErrorPageManager;
import cloud.piranha.core.api.LocaleEncodingManager;
import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebXml;
import cloud.piranha.core.api.WebXmlContextParam;
import cloud.piranha.core.api.WebXmlDataSource;
import cloud.piranha.core.api.WebXmlErrorPage;
import cloud.piranha.core.api.WebXmlFilterInitParam;
import cloud.piranha.core.api.WebXmlJspConfigTaglib;
import cloud.piranha.core.api.WebXmlListener;
import cloud.piranha.core.api.WebXmlLoginConfig;
import cloud.piranha.core.api.WebXmlServlet;
import cloud.piranha.core.api.WebXmlServletMapping;
import cloud.piranha.core.api.WebXmlServletMultipartConfig;
import cloud.piranha.core.api.WebXmlSessionConfig;
import cloud.piranha.core.api.WelcomeFileManager;
import cloud.piranha.core.impl.DefaultJspConfigDescriptor;
import cloud.piranha.core.impl.DefaultTaglibDescriptor;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.descriptor.JspConfigDescriptor;

/**
 * The web.xml / web-fragment.xml processor.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class InternalWebXmlProcessor {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(InternalWebXmlProcessor.class.getName());

    /**
     * Stores the empty string array.
     */
    private static final String[] STRING_ARRAY = new String[0];

    /**
     * Process the web.xml into the web application.
     *
     * @param webXml the web.xml
     * @param webApplication the web application.
     */
    public void process(WebXml webXml, WebApplication webApplication) {
        LOGGER.log(TRACE, "Started WebXmlProcessor.process");
        processMetadataComplete(webApplication, webXml);
        processContextParameters(webApplication, webXml);
        processDefaultContextPath(webApplication, webXml);
        processDenyUncoveredHttpMethods(webApplication, webXml);
        processDisplayName(webApplication, webXml);
        processDistributable(webApplication, webXml);
        processErrorPages(webApplication, webXml);
        processFilters(webApplication, webXml);
        processFilterMappings(webApplication, webXml);
        processListeners(webApplication, webXml);
        processLoginConfig(webApplication, webXml);
        processMimeMappings(webApplication, webXml);
        processRequestCharacterEncoding(webApplication, webXml);
        processResponseCharacterEncoding(webApplication, webXml);
        processRoleNames(webApplication, webXml);
        processSecurityConstraints(webApplication, webXml);
        processServlets(webApplication, webXml);
        processServletMappings(webApplication, webXml);
        processWebApp(webApplication, webXml);
        processWelcomeFiles(webApplication, webXml);
        processLocaleEncodingMapping(webApplication, webXml);
        processSessionConfig(webApplication, webXml);
        processDataSources(webApplication, webXml);
        processJspConfig(webApplication, webXml);
        LOGGER.log(TRACE, "Finished WebXmlProcessor.process");
    }

    /**
     * Process the context parameters.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processContextParameters(WebApplication webApplication, WebXml webXml) {
        for (WebXmlContextParam contextParam : webXml.getContextParams()) {
            webApplication.setInitParameter(contextParam.name(), contextParam.value());
        }
    }

    /**
     * Process the data sources.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processDataSources(WebApplication webApplication, WebXml webXml) {
        for (WebXmlDataSource dataSourceXml : webXml.getDataSources()) {
            try {
                CommonDataSource dataSource = (CommonDataSource) Class.forName(dataSourceXml.getClassName())
                        .getDeclaredConstructor()
                        .newInstance();

                if (dataSourceXml.getUrl() != null) {
                    dataSource.getClass()
                            .getMethod("setUrl", String.class)
                            .invoke(dataSource, dataSourceXml.getUrl());
                }

                if (dataSourceXml.getPassword() != null) {
                    dataSource.getClass()
                            .getMethod("setPassword", String.class)
                            .invoke(dataSource, dataSourceXml.getPassword());
                }
                if (dataSourceXml.getUser() != null) {
                    dataSource.getClass()
                            .getMethod("setUser", String.class)
                            .invoke(dataSource, dataSourceXml.getUser());
                }

                if (!dataSourceXml.getProperties().isEmpty()) {
                    Method[] dataSourceMethods = dataSource.getClass().getMethods();
                    properties:
                    for (var property : dataSourceXml.getProperties().entrySet()) {
                        for (Method dataSourceMethod : dataSourceMethods) {
                            if (isStringSetter(dataSourceMethod)) {
                                String methodName = "set" + capitalize(property.getKey());

                                if (dataSourceMethod.getName().equals(methodName)) {
                                    dataSourceMethod.invoke(dataSource, property.getValue());
                                    break properties;
                                }
                            }
                        }
                    }
                }

                InitialContext initialContext = new InitialContext();
                initialContext.bind(dataSourceXml.getName(), dataSource);
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException | NamingException e) {
                LOGGER.log(WARNING, "Unable to create DataSource", e);
            }
        }
    }

    private boolean isStringSetter(Method method) {
        return method.getParameterCount() == 1 && method.getParameters()[0].getType().equals(String.class);
    }

    private String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Process the default context path.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processDefaultContextPath(WebApplication webApplication, WebXml webXml) {
        if (webXml.getDefaultContextPath() != null) {
            webApplication.setContextPath(webXml.getDefaultContextPath());
        }
    }

    /**
     * Process the metadata complete.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processMetadataComplete(WebApplication webApplication, WebXml webXml) {
        if (webXml.getMetadataComplete()) {
            webApplication.setMetadataComplete(true);
        }
    }

    /**
     * Process the deny uncovered HTTP methods flag.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processDenyUncoveredHttpMethods(WebApplication webApplication, WebXml webXml) {
        if (webApplication.getManager().getSecurityManager() != null) {
            webApplication.getManager().getSecurityManager()
                    .setDenyUncoveredHttpMethods(webXml.getDenyUncoveredHttpMethods());
        }
    }

    /**
     * Process the display name flag.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processDisplayName(WebApplication webApplication, WebXml webXml) {
        webApplication.setServletContextName(webXml.getDisplayName());
    }

    /**
     * Process the distributable flag.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processDistributable(WebApplication webApplication, WebXml webXml) {
        webApplication.setDistributable(webXml.isDistributable());
    }

    /**
     * Process the error pages.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processErrorPages(WebApplication webApplication, WebXml webXml) {
        if (webApplication.getManager().getErrorPageManager() != null) {
            ErrorPageManager errorPageManager = webApplication.getManager().getErrorPageManager();
            for (WebXmlErrorPage errorPage : webXml.getErrorPages()) {
                if (errorPage.errorCode() != null && !errorPage.errorCode().isEmpty()) {
                    errorPageManager.addErrorPage(Integer.parseInt(errorPage.errorCode()), errorPage.location());
                } else if (errorPage.exceptionType() != null && !errorPage.exceptionType().isEmpty()) {
                    errorPageManager.addErrorPage(errorPage.exceptionType(), errorPage.location());
                }
            }
        }
    }

    /**
     * Process the filter mappings mappings.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processFilterMappings(WebApplication webApplication, WebXml webXml) {
        webXml.getFilterMappings().forEach(filterMapping -> {
            FilterRegistration filterReg = webApplication
                    .getFilterRegistration(filterMapping.getFilterName());

            // Filter is mapped to a URL pattern, e.g. /path/customer
            filterReg.addMappingForUrlPatterns(
                    toEnumDispatcherTypes(filterMapping.getDispatchers()),
                    true,
                    filterMapping.getUrlPatterns().toArray(STRING_ARRAY));

            // Filter is mapped to a named Servlet, e.g. FacesServlet
            webApplication.addFilterMapping(
                    toDispatcherTypes(filterMapping.getDispatchers()),
                    filterMapping.getFilterName(),
                    true,
                    filterMapping.getServletNames().stream().map(e -> "servlet:// " + e).toArray(String[]::new));
        });
    }

    private EnumSet<DispatcherType> toEnumDispatcherTypes(List<String> dispatchers) {
        if (dispatchers == null) {
            return null;
        }

        EnumSet enumSet = EnumSet.noneOf(DispatcherType.class);
        for (String dispatcherType : dispatchers) {
            enumSet.add(DispatcherType.valueOf(dispatcherType));
        }
        return enumSet;
    }

    private Set<DispatcherType> toDispatcherTypes(List<String> dispatchers) {
        if (dispatchers == null) {
            return null;
        }

        return dispatchers.stream()
                .map(DispatcherType::valueOf)
                .collect(toSet());
    }

    /**
     * Process the filters.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processFilters(WebApplication webApplication, WebXml webXml) {
        webXml.getFilters().forEach(filter -> {
            FilterRegistration.Dynamic dynamic = null;

            if (filter.getClassName() != null) {
                dynamic = webApplication.addFilter(filter.getFilterName(), filter.getClassName());
            } else if (filter.getServletName() != null) {
                dynamic = webApplication.addFilter(filter.getFilterName(), filter.getServletName());
            }

            if (dynamic != null && filter.isAsyncSupported()) {
                dynamic.setAsyncSupported(true);
            }

            if (dynamic != null) {
                for (WebXmlFilterInitParam initParam : filter.getInitParams()) {
                    dynamic.setInitParameter(initParam.name(), initParam.value());
                }
            }
        });
    }

    /**
     * Process the jsp config.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processJspConfig(WebApplication webApplication, WebXml webXml) {
        List<WebXmlJspConfigTaglib> taglibs = webXml.getJspConfig().getTaglibs();
        if (!taglibs.isEmpty()) {
            JspConfigDescriptor descriptor = webApplication.getJspConfigDescriptor();
            if (descriptor == null) {
                descriptor = new DefaultJspConfigDescriptor();
                webApplication.setJspConfigDescriptor(descriptor);
            }
            for (WebXmlJspConfigTaglib taglib : taglibs) {
                DefaultTaglibDescriptor taglibDescriptor = new DefaultTaglibDescriptor();
                taglibDescriptor.setTaglibLocation(taglib.getLocation());
                taglibDescriptor.setTaglibURI(taglib.getUri());
                descriptor.getTaglibs().add(taglibDescriptor);
            }
        }
    }

    /**
     * Process the listeners.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processListeners(WebApplication webApplication, WebXml webXml) {
        for (WebXmlListener listener : webXml.getListeners()) {
            webApplication.addListener(listener.className());
        }
    }

    /**
     * Process the login-config.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processLoginConfig(WebApplication webApplication, WebXml webXml) {
        SecurityManager manager = webApplication.getManager().getSecurityManager();
        if (manager != null && webXml.getLoginConfig() != null) {
            WebXmlLoginConfig loginConfig = webXml.getLoginConfig();
            if (loginConfig.authMethod() != null) {
                manager.setAuthMethod(loginConfig.authMethod());
            }
            if (loginConfig.formErrorPage() != null) {
                manager.setFormErrorPage(loginConfig.formErrorPage());
            }
            if (loginConfig.formLoginPage() != null) {
                manager.setFormLoginPage(loginConfig.formLoginPage());
            }
            if (loginConfig.realmName() != null) {
                manager.setRealmName(loginConfig.realmName());
            }
        }
    }

    /**
     * Process the mime mappings.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processMimeMappings(WebApplication webApplication, WebXml webXml) {
        webXml.getMimeMappings().forEach(mapping
                -> webApplication.addMimeType(mapping.extension(), mapping.mimeType())
        );
    }

    /**
     * Process the request character encoding.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processRequestCharacterEncoding(WebApplication webApplication, WebXml webXml) {
        if (webXml.getRequestCharacterEncoding() != null) {
            webApplication.setRequestCharacterEncoding(webXml.getRequestCharacterEncoding());
        }
    }

    /**
     * Process the response character encoding.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processResponseCharacterEncoding(WebApplication webApplication, WebXml webXml) {
        if (webXml.getResponseCharacterEncoding() != null) {
            webApplication.setResponseCharacterEncoding(webXml.getResponseCharacterEncoding());
        }
    }

    private void processRoleNames(WebApplication webApplication, WebXml webXml) {
        if (webApplication.getManager().getSecurityManager() != null) {
            webApplication.getManager().getSecurityManager().declareRoles(webXml.getRoleNames());
        }
    }

    /**
     * Process the servlet mappings.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processServletMappings(WebApplication webApplication, WebXml webXml) {
        for (WebXmlServletMapping mapping : webXml.getServletMappings()) {

            // The application is allowed to override the *.jsp mapping set by a JSP implementation.
            if (mapping.urlPattern().equals("*.jsp")) {
                LOGGER.log(DEBUG, "Application defined *.jsp mapping, therefor overriding any existing mapping.");
                String oldServlet = webApplication.removeServletMapping(mapping.urlPattern());
                if (oldServlet != null) {
                    LOGGER.log(DEBUG, "The mapping for Servlet " + oldServlet + " for *.jsp has been overriden by mapping.servletName()");
                }

            }

            webApplication.addServletMapping(mapping.servletName(), mapping.urlPattern());
        }
    }

    /**
     * Process the web app. This is basically only the version contained within
     * it.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processWebApp(WebApplication webApplication, WebXml webXml) {
        if (webXml.getMajorVersion() != -1) {
            webApplication.setEffectiveMajorVersion(webXml.getMajorVersion());
        }
        if (webXml.getMinorVersion() != -1) {
            webApplication.setEffectiveMinorVersion(webXml.getMinorVersion());
        }
    }

    /**
     * Process the servlets.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processServlets(WebApplication webApplication, WebXml webXml) {
        LOGGER.log(DEBUG, "Configuring Servlets");

        for (WebXmlServlet servlet : webXml.getServlets()) {
            LOGGER.log(DEBUG, () -> "Configuring Servlet: " + servlet.getServletName());

            ServletRegistration.Dynamic dynamic;

            String jspFile = servlet.getJspFile();
            if (!isEmpty(jspFile)) {
                dynamic = webApplication.addJspFile(servlet.getServletName(), jspFile);
            } else {
                dynamic = webApplication.addServlet(servlet.getServletName(), servlet.getClassName());
            }

            if (servlet.isAsyncSupported()) {
                dynamic.setAsyncSupported(true);
            }

            WebXmlServletMultipartConfig multipartConfig = servlet.getMultipartConfig();
            if (multipartConfig != null) {
                dynamic.setMultipartConfig(
                        new MultipartConfigElement(
                                multipartConfig.getLocation(),
                                multipartConfig.getMaxFileSize(),
                                multipartConfig.getMaxRequestSize(),
                                multipartConfig.getFileSizeThreshold()));
            }

            servlet.getInitParams().forEach(initParam -> {
                ServletRegistration servletRegistration = webApplication.getServletRegistration(servlet.getServletName());
                if (servletRegistration != null) {
                    servletRegistration.setInitParameter(initParam.name(), initParam.value());
                }
            });

            LOGGER.log(DEBUG, () -> "Configured Servlet: " + servlet.getServletName());
        }
    }

    /**
     * Process the welcome files.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processWelcomeFiles(WebApplication webApplication, WebXml webXml) {
        LOGGER.log(DEBUG, "Adding welcome files");

        Iterator<String> iterator = webXml.getWelcomeFiles().iterator();
        WelcomeFileManager welcomeFileManager = webApplication.getManager().getWelcomeFileManager();
        if (welcomeFileManager != null) {
            while (iterator.hasNext()) {
                String welcomeFile = iterator.next();
                LOGGER.log(DEBUG, () -> "Adding welcome file: " + welcomeFile);
                welcomeFileManager.addWelcomeFile(welcomeFile);
            }
        }
    }

    /**
     * Process the locale-encoding mapping.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processLocaleEncodingMapping(WebApplication webApplication, WebXml webXml) {
        Map<String, String> localeMapping = webXml.getLocaleEncodingMapping();
        if (localeMapping != null) {
            LocaleEncodingManager localeEncodingManager = webApplication.getManager().getLocaleEncodingManager();
            if (localeEncodingManager != null) {
                localeMapping.forEach(localeEncodingManager::addCharacterEncoding);
            }
        }
    }

    /**
     * Process the session config.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processSessionConfig(WebApplication webApplication, WebXml webXml) {
        WebXmlSessionConfig sessionConfig = webXml.getSessionConfig();
        if (sessionConfig != null) {
            webApplication.setSessionTimeout(sessionConfig.getSessionTimeout());
        }
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Process the security constraints.
     *
     * @param webApplication the web application.
     * @param webXml the web.xml.
     */
    private void processSecurityConstraints(WebApplication webApplication, WebXml webXml) {
        // what are we to do here?
    }
}

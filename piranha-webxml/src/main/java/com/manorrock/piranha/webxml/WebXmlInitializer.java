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
package com.manorrock.piranha.webxml;

import static java.util.logging.Level.WARNING;
import static javax.xml.xpath.XPathConstants.NODE;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.manorrock.piranha.api.WebApplication;

/**
 * The web.xml initializer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WebXmlInitializer.class.getName());

    /**
     * Stores the WebXML context-param name.
     */
    public static final String WEB_XML = "com.manorrock.piranha.webxml.WebXml";

    /**
     * Stores the WebXML context-param name.
     */
    private static final String WEB_FRAGMENTS = "com.manorrock.piranha.webxml.WebFragments";
    
    /**
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        try {
            WebApplication webApp = (WebApplication) servletContext;
            InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/web.xml");
            if (inputStream != null) {
                WebXml webXml = parseWebXml(servletContext.getResourceAsStream("WEB-INF/web.xml"));
                webApp.setAttribute(WEB_XML, webXml);
            }

            Enumeration<URL> webFragments = servletContext.getClassLoader().getResources("/META-INF/web-fragment.xml");
            ArrayList<WebXml> webXmls = new ArrayList<>();
            while (webFragments.hasMoreElements()) {
                URL url = webFragments.nextElement();
                webXmls.add(parseWebXml(url.openStream()));
            }
            if (!webXmls.isEmpty()) {
                webApp.setAttribute(WEB_FRAGMENTS, webXmls);
            }

            if (webApp.getAttribute(WEB_XML) == null) {
                List<WebXml> fragments = (List<WebXml>) webApp.getAttribute(WEB_FRAGMENTS);
                if (fragments != null && !fragments.isEmpty()) {
                    webApp.setAttribute(WEB_XML, fragments.get(0));
                }
            }

            if (webApp.getAttribute(WEB_XML) != null) {
                WebXml webXml = (WebXml) webApp.getAttribute(WEB_XML);
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                XPath xPath = XPathFactory.newInstance().newXPath();

                /*
                 * Process <display-name> content.
                 */
                String displayName = (String) xPath.evaluate("//display-name/text()", document, XPathConstants.STRING);
                webApp.setServletContextName(displayName);

                /*
                 * Process <listener> entries
                 */
                NodeList list = (NodeList) xPath.evaluate("//listener", document, NODESET);
                if (list != null) {
                    for (int i = 0; i < list.getLength(); i++) {
                        WebXml.Listener listener = new WebXml.Listener();
                        webXml.listeners.add(listener);
                        listener.className = (String) xPath.evaluate("listener-class/text()", list.item(i), XPathConstants.STRING);
                        webApp.addListener(listener.className);
                    }
                }

                processServlets(webApp);
                processServletMappings(webApp);

                /*
                 * Process <security-constraint> entries
                 */
                list = (NodeList) xPath.evaluate("//security-constraint", document, NODESET);
                if (list != null) {
                    processSecurityConstraints(webXml, list);

                    webApp.setAttribute(
                            "com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.piranha.constraints",
                            webXml.securityConstraints
                    );
                }
                
                /*
                 * Process <login-config> entry
                 */
                Node node = (Node) xPath.evaluate("//login-config", document, NODE);
                if (node != null) {
                    processLoginConfig(xPath, webXml, node);
                }
                
                /*
                 * Process <deny-uncovered-http-methods> entry
                 */
                node = (Node) xPath.evaluate("//deny-uncovered-http-methods", document, NODE);
                if (node != null) {
                    webXml.denyUncoveredHttpMethods = true;
                }

                /*
                 * Process <mime-mapping> entries
                 */
                Iterator<WebXml.MimeMapping> mappingIterator = webXml.mimeMappings.iterator();
                while (mappingIterator.hasNext()) {
                    WebXml.MimeMapping mapping = mappingIterator.next();
                    webApp.getMimeTypeManager().addMimeType(mapping.extension, mapping.mimeType);
                }

                /*
                 * Process <context-param> entries
                 */
                list = (NodeList) xPath.evaluate("//context-param", document, NODESET);
                if (list != null) {
                    processContextParameters(webXml, list);
                    Iterator<WebXml.ContextParameter> iterator = webXml.contextParameters.iterator();
                    while (iterator.hasNext()) {
                        WebXml.ContextParameter contextParam = iterator.next();
                        webApp.setInitParameter(contextParam.name, contextParam.value);
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.info("No web.xml found!");
                }
            }
        } catch (SAXException | XPathExpressionException | IOException
                | ParserConfigurationException e) {
            LOGGER.log(WARNING, "Unable to parse web.xml", e);
        }
    }

    /**
     * Parse WebXml.
     *
     * @param inputStream the input stream.
     * @return the WebXml.
     */
    public WebXml parseWebXml(InputStream inputStream) {
        WebXml result = new WebXml();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            XPath xPath = XPathFactory.newInstance().newXPath();
            result.mimeMappings.addAll((List<WebXml.MimeMapping>) parseList(
                    xPath, document, "//mime-mapping", WebXmlInitializer::parseMimeMapping));
            result.servlets.addAll((List<WebXml.Servlet>) parseList(
                    xPath, document, "//servlet", WebXmlInitializer::parseServlet));
            result.servletMappings.addAll((List<WebXml.ServletMapping>) parseList(
                    xPath, document, "//servlet-mapping", WebXmlInitializer::parseServletMapping));
        } catch (Throwable t) {
            LOGGER.log(WARNING, "Unable to parse web.xml", t);
        }
        
        return result;
    }


    // ### Private methods

    /**
     * Parse the mime-mapping.
     *
     * @param xPath the XPath.
     * @param node the DOM node.
     * @return the mime-mapping, or null if an error occurred.
     */
    private static WebXml.MimeMapping parseMimeMapping(XPath xPath, Node node) {
        WebXml.MimeMapping result = new WebXml.MimeMapping();
        try {
            result.extension = (String) xPath.evaluate("//extension/text()", node, XPathConstants.STRING);
            result.mimeType = (String) xPath.evaluate("//mime-type/text()", node, XPathConstants.STRING);
        } catch (XPathException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse <mime-mapping>", xee);
            }
            result = null;
        }
        return result;
    }

    /**
     * Parse a servlet.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the servlet, or null if an error occurred.
     */
    private static WebXml.Servlet parseServlet(XPath xPath, Node node) {
        WebXml.Servlet result = new WebXml.Servlet();
        try {
            result.asyncSupported = (boolean) applyOrDefault(parseBoolean(xPath, node, "async-supported/text()"), false);
            result.name = (String) xPath.evaluate("servlet-name/text()", node, XPathConstants.STRING);
            result.className = (String) xPath.evaluate("servlet-class/text()", node, XPathConstants.STRING);
            Double loadOnStartupDouble = (Double) xPath.evaluate("load-on-startup/text()", node, XPathConstants.NUMBER);
            if (loadOnStartupDouble != null) {
                result.loadOnStartup = loadOnStartupDouble.intValue();
            } else {
                result.loadOnStartup = -1;
            }
            result.initParams.addAll((List<WebXml.Servlet.InitParam>) parseList(
                    xPath, node, "init-param", WebXmlInitializer::parseServletInitParam));
            return result;
        } catch (XPathExpressionException xee) {
            LOGGER.log(WARNING, "Unable to parse <servlet>", xee);
            result = null;
        }
        
        return result;
    }

    /**
     * Parse a servlet init-param.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the init-param, or null if an error occurred.
     */
    private static WebXml.Servlet.InitParam parseServletInitParam(XPath xPath, Node node) {
        WebXml.Servlet.InitParam result = new WebXml.Servlet.InitParam();
        try {
            result.name = (String) xPath.evaluate("param-name/text()", node, XPathConstants.STRING);
            result.value = (String) xPath.evaluate("param-value/text()", node, XPathConstants.STRING);
        } catch (XPathExpressionException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse <init-param>", xee);
            }
            result = null;
        }
        return result;
    }

    /**
     * Parse a servlet-mapping.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the servlet-mapping, or null if an error occurred.
     */
    private static WebXml.ServletMapping parseServletMapping(XPath xPath, Node node) {
        WebXml.ServletMapping result = new WebXml.ServletMapping();
        try {
            result.servletName = (String) xPath.evaluate("servlet-name/text()", node, XPathConstants.STRING);
            result.urlPattern = (String) xPath.evaluate("url-pattern/text()", node, XPathConstants.STRING);
        } catch (XPathExpressionException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse <servlet-mapping>", xee);
            }
            result = null;
        }
        return result;
    }

    
    /**
     * Process the WebXml.ServletMapping list.
     *
     * @param webApplication the web application.
     */
    private void processServletMappings(WebApplication webApplication) {
        WebXml webXml = (WebXml) webApplication.getAttribute(WEB_XML);
        Iterator<WebXml.ServletMapping> iterator = webXml.servletMappings.iterator();
        while (iterator.hasNext()) {
            WebXml.ServletMapping mapping = iterator.next();
            webApplication.addServletMapping(mapping.servletName, mapping.urlPattern);
        }
    }

    /**
     * Process the WebXml.Servlet list.
     *
     * @param webApplication the web application.
     */
    private void processServlets(WebApplication webApplication) {
        WebXml webXml = (WebXml) webApplication.getAttribute(WEB_XML);
        Iterator<WebXml.Servlet> iterator = webXml.servlets.iterator();
        while (iterator.hasNext()) {
            WebXml.Servlet servlet = iterator.next();
            Dynamic registration = webApplication.addServlet(servlet.name, servlet.className);
            if (servlet.asyncSupported) {
                registration.setAsyncSupported(true);
            }
            if (!servlet.initParams.isEmpty()) {
                servlet.initParams.forEach((initParam) -> {
                    registration.setInitParameter(initParam.name, initParam.value);
                });
            }
        }
    }
    
    /**
     * Process the context-param entries.
     *
     * @param webXml the web.xml to add to.
     * @param nodeList the node list.
     * @return the web.xml.
     */
    private void processContextParameters(WebXml webXml, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            processContextParameter(webXml, nodeList.item(i));
        }
    }

    /**
     * Process the context-param section.
     *
     * @param webXml the web.xml to add to.
     * @param node the DOM node.
     * @return the web.xml.
     */
    private void processContextParameter(WebXml webXml, Node node) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            WebXml.ContextParameter contextParameter = new WebXml.ContextParameter();
            contextParameter.name = (String) xPath.evaluate("//param-name/text()", node, XPathConstants.STRING);
            contextParameter.value = (String) xPath.evaluate("//param-value/text()", node, XPathConstants.STRING);
            webXml.contextParameters.add(contextParameter);
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <context-param> section", xpe);
        }
    }
   
    
    /**
     * Process the mime-mapping section.
     *
     * @param webXml the web.xml to add to.
     * @param nodeList the node list.
     * @return the web.xml.
     */
    private void processMimeMappings(WebXml webXml, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            processMimeMapping(webXml, nodeList.item(i));
        }
    }

    /**
     * Process the mime-mapping entries.
     *
     * @param webXml the web.xml to add to.
     * @param node the DOM node.
     * @return the web.xml.
     */
    private void processMimeMapping(WebXml webXml, Node node) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            WebXml.MimeMapping mimeMapping = new WebXml.MimeMapping();
            mimeMapping.extension = (String) xPath.evaluate("//extension/text()", node, XPathConstants.STRING);
            mimeMapping.mimeType = (String) xPath.evaluate("//mime-type/text()", node, XPathConstants.STRING);
            webXml.mimeMappings.add(mimeMapping);
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <mime-mapping> section", xpe);
        }
    }

    private void processSecurityConstraints(WebXml webXml, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            processSecurityConstraint(webXml, nodeList.item(i));
        }
    }

    private void processSecurityConstraint(WebXml webXml, Node node) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            WebXml.SecurityConstraint securityConstraint = new WebXml.SecurityConstraint();

            forEachNode(xPath, node, "//web-resource-collection", webResourceCollectionNode -> {
                WebXml.SecurityConstraint.WebResourceCollection webResourceCollection = new WebXml.SecurityConstraint.WebResourceCollection();

                forEachString(xPath, webResourceCollectionNode, "//url-pattern",
                        urlPattern -> webResourceCollection.urlPatterns.add(urlPattern)
                );

                forEachString(xPath, webResourceCollectionNode, "//http-method",
                        httpMethod -> webResourceCollection.httpMethods.add(httpMethod)
                );

                forEachString(xPath, webResourceCollectionNode, "//http-method-omission",
                        httpMethodOmission -> webResourceCollection.httpMethodOmissions.add(httpMethodOmission)
                );

                securityConstraint.webResourceCollections.add(webResourceCollection);
            });

            forEachString(xPath, getNodes(xPath, node, "//auth-constraint"), "//role-name/text()",
                    roleName -> securityConstraint.roleNames.add(roleName)
            );

            securityConstraint.transportGuarantee = getString(xPath, node, "//user-data-constraint/transport-guarantee/text()");

            webXml.securityConstraints.add(securityConstraint);

        } catch (Exception xpe) {
            LOGGER.log(WARNING, "Unable to parse <servlet> section", xpe);
        }
    }
    
    private void processLoginConfig(XPath xPath, WebXml webXml, Node node) throws XPathExpressionException {
        webXml.loginConfig.authMethod = getString(xPath, node, "//auth-method/text()");
        webXml.loginConfig.realmName = getString(xPath, node, "//realm-name/text()");
        webXml.loginConfig.formLoginPage = getString(xPath, node, "//form-login-config/form-login-page/text()");
        webXml.loginConfig.formErrorPage = getString(xPath, node, "//form-login-config/form-error-page/text()");
    }
    
    
    // ### Utility methods

    /**
     * Short-cut method for forEachNode - forEachString, when only one node's
     * string value is needed
     *
     */
    private void forEachString(XPath xPath, NodeList nodes, String expression, ThrowingConsumer<String> consumer) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            consumer.accept((String) xPath.evaluate(expression, nodes.item(i), XPathConstants.STRING));
        }
    }

    private void forEachString(XPath xPath, Node parent, String expression, ThrowingConsumer<String> consumer) throws XPathExpressionException {
        forEachNode(xPath, parent, expression, node -> consumer.accept(getString(xPath, node, "child::text()")));
    }

    private void forEachNode(XPath xPath, Node node, String expression, ThrowingConsumer<Node> consumer) throws XPathExpressionException {
        NodeList nodes = (NodeList) xPath.evaluate(expression, node, NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            consumer.accept(nodes.item(i));
        }
    }

    private String getString(XPath xPath, Node node, String expression) throws XPathExpressionException {
        return (String) xPath.evaluate(expression, node, STRING);
    }

    private NodeList getNodes(XPath xPath, Node node, String expression) throws XPathExpressionException {
        return (NodeList) xPath.evaluate(expression, node, NODESET);
    }

    private interface ThrowingConsumer<T> {

        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        void accept(T t) throws XPathExpressionException;
    }
    
    /**
     * Apply non null value or default value.
     *
     * @param object the object.
     * @param defaultValue the default value.
     * @return the non null value (either the object or the default value).
     */
    private static Object applyOrDefault(Object object, Object defaultValue) {
        Object result = object;
        if (object == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Parse a boolean.
     *
     * @param xPath the XPath to use.
     * @param node the node to use.
     * @param expression the expression to use.
     * @param defaultValue the default value.
     * @return the boolean, or the default value if an error occurred.
     */
    private static Boolean parseBoolean(XPath xPath, Node node, String expression) {
        Boolean result = null;
        try {
            result = (Boolean) xPath.evaluate(expression, node, XPathConstants.BOOLEAN);
        } catch (XPathExpressionException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse boolean", xee);
            }
        }
        return result;
    }
    
    /**
     * Parse a list.
     *
     * <pre>
     *  1. Get the node list based on the expression.
     *  2. For each element in the node list
     *      a. Call the bi-function
     *      b. Add the result of the bi-function to the result list.
     *  3. Return the result list.
     * </pre>
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @param expression the XPath expression.
     * @param biFunction the bi-function.
     * @return the list, or null if an error occurred.
     */
    private static List parseList(XPath xPath, Node node, String expression, BiFunction<XPath, Node, Object> biFunction) {
        ArrayList result = new ArrayList();
        try {
            NodeList nodeList = (NodeList) xPath.evaluate(expression, node, NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Object functionResult = biFunction.apply(xPath, nodeList.item(i));
                if (functionResult != null) {
                    result.add(functionResult);
                }
            }
        } catch (XPathExpressionException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse <" + expression + ">", xee);
            }
            result = null;
        }
        return result;
    }

}

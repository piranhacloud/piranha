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

import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
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
import java.util.ArrayList;
import java.util.List;

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
    private static final String WEB_XML = "com.manorrock.piranha.webxml.WebXml";

    /**
     * Parse a servlet section.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the WebXml Servlet definition.
     * @throws Exception when a serious error occurs.
     */
    private WebXml.Servlet parseServlet(XPath xPath, Node node) throws Exception {
        WebXml.Servlet result = new WebXml.Servlet();
        Boolean asyncSupported = (Boolean) xPath.evaluate("async-supported/text()", node, XPathConstants.BOOLEAN);
        if (asyncSupported != null) {
            result.asyncSupported = asyncSupported;
        }
        result.name = (String) xPath.evaluate("servlet-name/text()", node, XPathConstants.STRING);
        result.className = (String) xPath.evaluate("servlet-class/text()", node, XPathConstants.STRING);
        Double loadOnStartupDouble = (Double) xPath.evaluate("load-on-startup/text()", node, XPathConstants.NUMBER);
        if (loadOnStartupDouble != null) {
            result.loadOnStartup = loadOnStartupDouble.intValue();
        } else {
            result.loadOnStartup = -1;
        }
        NodeList initParams = (NodeList) xPath.evaluate("init-param", node, NODESET);
        for (int i = 0; i < initParams.getLength(); i++) {
            WebXml.Servlet.InitParam initParam = new WebXml.Servlet.InitParam();
            initParam.name = (String) xPath.evaluate("param-name/text()", initParams.item(i), XPathConstants.STRING);
            initParam.value = (String) xPath.evaluate("param-value/text()", initParams.item(i), XPathConstants.STRING);
            result.initParams.add(initParam);
        }
        return result;
    }

    /**
     * Parse a servlet-mapping section.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the list of WebXml ServletMapping definitions.
     * @throws Exception when a serious error occurs.
     */
    private WebXml.ServletMapping parseServletMapping(XPath xPath, Node node) throws Exception {
        WebXml.ServletMapping result = new WebXml.ServletMapping();
        result.servletName = (String) xPath.evaluate("servlet-name/text()", node, XPathConstants.STRING);
        result.urlPattern = (String) xPath.evaluate("url-pattern/text()", node, XPathConstants.STRING);
        return result;
    }

    /**
     * Parse the servlet-mapping sections.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the list of WebXml ServletMapping definitions.
     * @throws Exception when a serious error occurs.
     */
    private List<WebXml.ServletMapping> parseServletMappings(XPath xPath, Node node) throws Exception {
        ArrayList<WebXml.ServletMapping> result = new ArrayList<>();
        NodeList servletList = (NodeList) xPath.evaluate("//servlet-mapping", node, NODESET);
        for (int i = 0; i < servletList.getLength(); i++) {
            result.add(parseServletMapping(xPath, servletList.item(i)));
        }
        return result;
    }

    /**
     * Parse the servlet sections.
     *
     * @param xPath the XPath to use.
     * @param node the DOM node to parse.
     * @return the list of WebXml Servlet definitions.
     * @throws Exception when a serious error occurs.
     */
    public List<WebXml.Servlet> parseServlets(XPath xPath, Node node) throws Exception {
        ArrayList<WebXml.Servlet> result = new ArrayList<>();
        NodeList servletList = (NodeList) xPath.evaluate("//servlet", node, NODESET);
        for (int i = 0; i < servletList.getLength(); i++) {
            result.add(parseServlet(xPath, servletList.item(i)));
        }
        return result;
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
            result.servlets.addAll(parseServlets(xPath, document));
            result.servletMappings.addAll(parseServletMappings(xPath, document));
        } catch (Throwable t) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to parse web.xml", t);
            }
        }
        return result;
    }

    /**
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {
        try {
            WebApplication webApp = (WebApplication) servletContext;
            InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/web.xml");
            if (inputStream != null) {
                WebXml webXml = parseWebXml(servletContext.getResourceAsStream("WEB-INF/web.xml"));
                webApp.setAttribute(WEB_XML, webXml);

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
                 * Process <mime-mapping> entries
                 */
                list = (NodeList) xPath.evaluate("//mime-mapping", document, NODESET);
                if (list != null) {
                    processMimeMappings(webXml, list);
                    Iterator<WebXml.MimeMapping> mappingIterator = webXml.mimeMappings.iterator();
                    while (mappingIterator.hasNext()) {
                        WebXml.MimeMapping mapping = mappingIterator.next();
                        webApp.getMimeTypeManager().addMimeType(mapping.extension, mapping.mimeType);
                    }
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
            LOGGER.log(Level.WARNING, "Unable to parse web.xml", e);
        }
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
            LOGGER.log(Level.WARNING, "Unable to parse <context-param> section", xpe);
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
            LOGGER.log(Level.WARNING, "Unable to parse <mime-mapping> section", xpe);
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
            LOGGER.log(Level.WARNING, "Unable to parse <servlet> section", xpe);
        }
    }

    /**
     * Short-cut method for forEachNode - forEachString, when only one node's
     * string value is needed
     *
     */
    public static void forEachString(XPath xPath, NodeList nodes, String expression, ThrowingConsumer<String> consumer) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            consumer.accept((String) xPath.evaluate(expression, nodes.item(i), XPathConstants.STRING));
        }
    }

    public static void forEachString(XPath xPath, Node parent, String expression, ThrowingConsumer<String> consumer) throws XPathExpressionException {
        forEachNode(xPath, parent, expression, node -> consumer.accept(getString(xPath, node, "child::text()")));
    }

    public static void forEachNode(XPath xPath, Node node, String expression, ThrowingConsumer<Node> consumer) throws XPathExpressionException {
        NodeList nodes = (NodeList) xPath.evaluate(expression, node, NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            consumer.accept(nodes.item(i));
        }
    }

    public static String getString(XPath xPath, Node node, String expression) throws XPathExpressionException {
        return (String) xPath.evaluate(expression, node, STRING);
    }

    public static NodeList getNodes(XPath xPath, Node node, String expression) throws XPathExpressionException {
        return (NodeList) xPath.evaluate(expression, node, NODESET);
    }

    public interface ThrowingConsumer<T> {

        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        void accept(T t) throws XPathExpressionException;
    }

}

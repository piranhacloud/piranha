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
package cloud.piranha.webapp.webxml;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cloud.piranha.webapp.api.WebApplication;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

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
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Entering WebXmlInitializer.onStartup");
        }
        try {
            WebXmlParser parser = new WebXmlParser();
            WebXmlManager manager = new WebXmlManager();
            servletContext.setAttribute(WebXmlManager.KEY, manager);

            WebApplication webApp = (WebApplication) servletContext;
            InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/web.xml");
            if (inputStream != null) {
                WebXml webXml = parser.parse(servletContext.getResourceAsStream("WEB-INF/web.xml"));
                manager.setWebXml(webXml);
            }

            Enumeration<URL> webFragmentUrls = servletContext.getClassLoader().getResources("/META-INF/web-fragment.xml");
            ArrayList<WebXml> webFragments = new ArrayList<>();
            while (webFragmentUrls.hasMoreElements()) {
                URL url = webFragmentUrls.nextElement();
                WebXml webFragment = parser.parse(url.openStream());
                webFragment.setFragment(true);
                webFragments.add(webFragment);
            }
            if (!webFragments.isEmpty()) {
                manager.setWebFragments(webFragments);
            }

            if (manager.getWebXml() == null && !webFragments.isEmpty()) {
                manager.setWebXml(webFragments.get(0));
            }

            if (manager.getWebXml() != null) {
                WebXml webXml = manager.getWebXml();
                WebXmlProcessor processor = new WebXmlProcessor();
                processor.process(webXml, webApp);

                if (inputStream != null) {
                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = documentBuilder.parse(inputStream);
                    XPath xPath = XPathFactory.newInstance().newXPath();

                    /*
                     * Process <security-constraint> entries
                     */
                    NodeList list = (NodeList) xPath.evaluate("//security-constraint", document, NODESET);
                    if (list != null) {
                        processSecurityConstraints(webXml, list);
                    }
                }
            } else {
                if (LOGGER.isLoggable(FINE)) {
                    LOGGER.info("No web.xml found!");
                }
            }
        } catch (SAXException | XPathExpressionException | IOException
                | ParserConfigurationException e) {
            LOGGER.log(WARNING, "Unable to parse web.xml", e);
        }
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(Level.FINE, "Exiting WebXmlInitializer.onStartup");
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
}

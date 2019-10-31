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
package com.manorrock.piranha.servlet;

import com.manorrock.piranha.api.WebApplication;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {
        try {
            WebApplication webApp = (WebApplication) servletContext;
            InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/web.xml");
            if (inputStream != null) {
                WebXml webXml = new WebXml();
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                XPath xPath = XPathFactory.newInstance().newXPath();
                
                /*
                 * Process <servlet> entries
                 */
                NodeList list = (NodeList) xPath.evaluate("//servlet", document, XPathConstants.NODESET);
                processServlets(webXml, list);
                Iterator<WebXml.Servlet> servletIterator = webXml.servlets.iterator();
                while(servletIterator.hasNext()) {
                    WebXml.Servlet servlet = servletIterator.next();
                    webApp.addServlet(servlet.name, servlet.className);
                }
                
                /*
                 * Process <servlet-mapping> entries
                 */
                list = (NodeList) xPath.evaluate("//servlet-mapping", document, XPathConstants.NODESET);
                processServletMappings(webXml, list);
                Iterator<WebXml.ServletMapping> mappingIterator = webXml.servletMappings.iterator();
                while(mappingIterator.hasNext()) {
                    WebXml.ServletMapping mapping = mappingIterator.next();
                    webApp.addServletMapping(mapping.servletName, mapping.urlPattern);
                }
            }
        } catch (SAXException | XPathExpressionException | IOException
                | ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "Unable to parse web.xml", e);
        }
    }

    /**
     * Process the servlet section.
     *
     * @param webXml the web.xml to add to.
     * @param nodeList the node list.
     * @return the web.xml.
     */
    private void processServlets(WebXml webXml, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            processServlet(webXml, nodeList.item(i));
        }
    }

    /**
     * Process the servlet-mapping section.
     *
     * @param webXml the web.xml to add to.
     * @param nodeList the node list.
     * @return the web.xml.
     */
    private void processServletMappings(WebXml webXml, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            processServletMapping(webXml, nodeList.item(i));
        }
    }

    /**
     * Process the servlet.
     *
     * @param webXml the web.xml to add to.
     * @param node the DOM node.
     * @return the web.xml.
     */
    private void processServlet(WebXml webXml, Node node) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            WebXml.Servlet servlet = new WebXml.Servlet();
            servlet.name = (String) xPath.evaluate("//servlet-name/text()", node, XPathConstants.STRING);
            servlet.className = (String) xPath.evaluate("//servlet-class/text()", node, XPathConstants.STRING);
            Double loadOnStartupDouble = (Double) xPath.evaluate("//loadOnStartup/text()", node, XPathConstants.NUMBER);
            if (loadOnStartupDouble != null) {
                servlet.loadOnStartup = loadOnStartupDouble.intValue();
            } else {
                servlet.loadOnStartup = -1;
            }
            webXml.servlets.add(servlet);
        } catch (XPathException xpe) {
            LOGGER.log(Level.WARNING, "Unable to parse <servlet> section", xpe);
        }
    }

    /**
     * Process the servlet-mapping.
     *
     * @param webXml the web.xml to add to.
     * @param node the DOM node.
     * @return the web.xml.
     */
    private void processServletMapping(WebXml webXml, Node node) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            WebXml.ServletMapping mapping = new WebXml.ServletMapping();
            mapping.servletName = (String) xPath.evaluate("//servlet-name/text()", node, XPathConstants.STRING);
            mapping.urlPattern = (String) xPath.evaluate("//url-pattern/text()", node, XPathConstants.STRING);
            webXml.servletMappings.add(mapping);
        } catch (XPathException xpe) {
            LOGGER.log(Level.WARNING, "Unable to parse <servlet-mapping> section", xpe);
        }
    }
}

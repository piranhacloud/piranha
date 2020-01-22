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
package cloud.piranha.servlet.webxml;

import java.io.InputStream;
import java.util.List;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import static javax.xml.xpath.XPathConstants.NODE;
import static javax.xml.xpath.XPathConstants.NODESET;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The web.xml / web-fragment.xml parser.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlParser {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WebXmlParser.class.getName());

    /**
     * Parse the input stream.
     *
     * @param inputStream the input stream.
     * @return the WebXml.
     */
    public WebXml parse(InputStream inputStream) {
        WebXml webXml = new WebXml();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            XPath xPath = XPathFactory.newInstance().newXPath();
            parseContextParameters(webXml, xPath, document);
            parseDefaultContextPath(webXml, xPath, document);
            parseDenyUncoveredHttpMethods(webXml, xPath, document);
            parseDisplayName(webXml, xPath, document);
            parseDistributable(webXml, xPath, document);
            parseErrorPages(webXml, xPath, document);
            parseFilterMappings(webXml, xPath, document);
            parseFilters(webXml, xPath, document);
            parseListeners(webXml, xPath, document);
            parseLoginConfig(webXml, xPath, document);
            parseMimeMappings(webXml, xPath, document);
            parseRequestCharacterEncoding(webXml, xPath, document);
            parseResponseCharacterEncoding(webXml, xPath, document);
            parseServletMappings(webXml, xPath, document);
            parseServlets(webXml, xPath, document);
            parseSessionConfig(webXml, xPath, document);
        } catch (Throwable t) {
            LOGGER.log(WARNING, "Unable to parse web.xml", t);
        }
        return webXml;
    }

    /**
     * Parse a boolean.
     *
     * @param xPath the XPath to use.
     * @param node the node to use.
     * @param expression the expression to use.
     * @return the boolean, or null if an error occurred.
     */
    private static Boolean parseBoolean(XPath xPath, String expression, Node node) {
        Boolean result = null;
        try {
            result = (Boolean) xPath.evaluate(expression, node, XPathConstants.BOOLEAN);
        } catch (XPathException xpe) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse boolean", xpe);
            }
        }
        return result;
    }

    /**
     * Parse the context-param sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param nodeList the node list.
     */
    private void parseContextParameters(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//context-param", node, NODESET);
            if (nodeList != null) {
                List<WebXmlContextParam> contextParams = webXml.getContextParams();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String name = parseString(xPath, "//param-name/text()", nodeList.item(i));
                    String value = parseString(xPath, "//param-value/text()", nodeList.item(i));
                    contextParams.add(new WebXmlContextParam(name, value));
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <context-param> sections", xpe);
        }
    }

    /**
     * Parse the deny-uncovered-http-methods section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseDenyUncoveredHttpMethods(WebXml webXml, XPath xPath, Node node) {
        try {
            Node denyNode = (Node) xPath.evaluate("//deny-uncovered-http-methods", node, NODE);
            if (denyNode != null) {
                webXml.setDenyUncoveredHttpMethods(true);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <deny-uncovered-http-methods> section", xpe);
        }
    }

    /**
     * Parse the default-context-path section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseDefaultContextPath(WebXml webXml, XPath xPath, Node node) {
        try {
            Node contextPathNode = (Node) xPath.evaluate("//default-context-path", node, NODE);
            if (contextPathNode != null) {
                String defaultContextPath = parseString(xPath, "//default-context-path/text()", node);
                if (defaultContextPath != null) {
                    webXml.setDefaultContextPath(defaultContextPath);
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <default-context-path> section", xpe);
        }
    }

    /**
     * Parse the display-name section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseDisplayName(WebXml webXml, XPath xPath, Node node) {
        try {
            String displayName = parseString(xPath, "//display-name/text()", node);
            if (displayName != null) {
                webXml.setDisplayName(displayName);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <display-name> section", xpe);
        }
    }

    /**
     * Parse the distributable section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseDistributable(WebXml webXml, XPath xPath, Node node) {
        try {
            Node denyNode = (Node) xPath.evaluate("//distributable", node, NODE);
            if (denyNode != null) {
                webXml.setDistributable(true);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <distributable> section", xpe);
        }
    }

    /**
     * Parse the error-page sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseErrorPages(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//error-page", node, NODESET);
            if (nodeList != null) {
                List<WebXmlErrorPage> errorPages = webXml.getErrorPages();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String errorCode = parseString(xPath, "error-code/text()", nodeList.item(i));
                    String exceptionType = parseString(xPath, "exception-type/text()", nodeList.item(i));
                    String location = parseString(xPath, "location/text()", nodeList.item(i));
                    errorPages.add(new WebXmlErrorPage(errorCode, exceptionType, location));
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <error-page> sections", xpe);
        }
    }

    /**
     * Parse the filter-mapping sections.
     *
     * @param webXml the web.xml to use.
     * @param xPath the XPath to use.
     * @param nodeList the Node list to parse.
     */
    private void parseFilterMappings(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//filter-mapping", node, NODESET);
            if (nodeList != null) {
                List<WebXmlFilterMapping> filterMappings = webXml.getFilterMappings();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String filterName = parseString(xPath, "filter-name/text()", nodeList.item(i));
                    String urlPattern = parseString(xPath, "url-pattern/text()", nodeList.item(i));
                    filterMappings.add(new WebXmlFilterMapping(filterName, urlPattern));
                }
            }
        } catch (XPathExpressionException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse <filter-mapping> sections", xee);
            }
        }
    }

    /**
     * Parse the filter sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseFilters(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//filter", node, NODESET);
            if (nodeList != null) {
                List<WebXmlFilter> filters = webXml.getFilters();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    WebXmlFilter filter = new WebXmlFilter();
                    String filterName = parseString(xPath, "filter-name/text()", nodeList.item(i));
                    filter.setFilterName(filterName);
                    String className = parseString(xPath, "filter-class/text()", nodeList.item(i));
                    filter.setClassName(className);
                    String servletName = parseString(xPath, "servlet-name/text()", nodeList.item(i));
                    filter.setServletName(servletName);
                    filters.add(filter);
                    NodeList paramNodeList = (NodeList) xPath.evaluate("init-param", nodeList.item(i), NODESET);
                    for (int j = 0; j < paramNodeList.getLength(); j++) {
                        WebXmlFilterInitParam initParam = new WebXmlFilterInitParam();
                        String name = parseString(xPath, "param-name/text()", paramNodeList.item(j));
                        initParam.setName(name);
                        String value = parseString(xPath, "param-value/text()", paramNodeList.item(j));
                        initParam.setValue(value);
                        filter.addInitParam(initParam);
                    }
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <filter> sections", xpe);
        }
    }

    /**
     * Parse an integer.
     *
     * @param xPath the XPath to use.
     * @param expression the expression.
     * @param node the node.
     * @return the string.
     * @throws XPathExpressionException when the expression was invalid.
     */
    private int parseInteger(XPath xPath, String expression, Node node)
            throws XPathExpressionException {
        Double doubleValue = (Double) xPath.evaluate(expression, node, XPathConstants.NUMBER);
        return doubleValue.intValue();
    }

    /**
     * Parse the listener sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseListeners(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//listener", node, NODESET);
            if (nodeList != null) {
                List<WebXmlListener> listeners = webXml.getListeners();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String className = parseString(xPath, "listener-class/text()", nodeList.item(i));
                    listeners.add(new WebXmlListener(className));
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <listener> sections", xpe);
        }
    }

    /**
     * Parse the login-config section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseLoginConfig(WebXml webXml, XPath xPath, Node node) {
        try {
            Node configNode = (Node) xPath.evaluate("//login-config", node, NODE);
            if (configNode != null) {
                String authMethod = parseString(xPath,
                        "//auth-method/text()", configNode);
                String realmName = parseString(xPath,
                        "//realm-name/text()", configNode);
                String formLoginPage = parseString(xPath,
                        "//form-login-config/form-login-page/text()", configNode);
                String formErrorPage = parseString(xPath,
                        "//form-login-config/form-error-page/text()", configNode);
                WebXmlLoginConfig config = new WebXmlLoginConfig(
                        authMethod, realmName, formLoginPage, formErrorPage);
                webXml.setLoginConfig(config);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <login-config> section", xpe);
        }
    }

    /**
     * Parse the mime-mapping sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param nodeList the node list.
     */
    private void parseMimeMappings(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//mime-mapping", node, NODESET);
            if (nodeList != null) {
                List<WebXmlMimeMapping> mimeMappings = webXml.getMimeMappings();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String extension = parseString(xPath, "//extension/text()", nodeList.item(i));
                    String mimeType = parseString(xPath, "//mime-type/text()", nodeList.item(i));
                    mimeMappings.add(new WebXmlMimeMapping(extension, mimeType));
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <mime-mapping> sections", xpe);
        }
    }

    /**
     * Parse the request-character-encoding section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseRequestCharacterEncoding(WebXml webXml, XPath xPath, Node node) {
        try {
            Node rceNode = (Node) xPath.evaluate("//request-character-encoding", node, NODE);
            if (rceNode != null) {
                String requestCharacterEncoding = parseString(
                        xPath, "//request-character-encoding/text()", node);
                webXml.setRequestCharacterEncoding(requestCharacterEncoding);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <request-character-encoding> section", xpe);
        }
    }

    /**
     * Parse the response-character-encoding section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseResponseCharacterEncoding(WebXml webXml, XPath xPath, Node node) {
        try {
            Node rceNode = (Node) xPath.evaluate("//response-character-encoding", node, NODE);
            if (rceNode != null) {
                String responseCharacterEncoding = parseString(
                        xPath, "//response-character-encoding/text()", node);
                webXml.setResponseCharacterEncoding(responseCharacterEncoding);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <response-character-encoding> section", xpe);
        }
    }

    /**
     * Parse the servlet-mapping sections.
     *
     * @param webXml the web.xml to use.
     * @param xPath the XPath to use.
     * @param nodeList the Node list to parse.
     */
    private void parseServletMappings(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//servlet-mapping", node, NODESET);
            if (nodeList != null) {
                List<WebXmlServletMapping> servletMappings = webXml.getServletMappings();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String servletName = parseString(xPath, "servlet-name/text()", nodeList.item(i));
                    String urlPattern = parseString(xPath, "url-pattern/text()", nodeList.item(i));
                    servletMappings.add(new WebXmlServletMapping(servletName, urlPattern));
                }
            }
        } catch (XPathExpressionException xee) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to parse <servlet-mapping> section", xee);
            }
        }
    }

    /**
     * Parse the servlet sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseServlets(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//servlet", node, NODESET);
            if (nodeList != null) {
                List<WebXmlServlet> servlets = webXml.getServlets();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    WebXmlServlet servlet = new WebXmlServlet();
                    String servletName = parseString(xPath, "servlet-name/text()", nodeList.item(i));
                    servlet.setServletName(servletName);
                    String className = parseString(xPath, "servlet-class/text()", nodeList.item(i));
                    servlet.setClassName(className);
                    Boolean asyncSupported = parseBoolean(xPath, "async-supported/text()", nodeList.item(i));
                    if (asyncSupported != null) {
                        servlet.setAsyncSupported(asyncSupported);
                    }
                    servlets.add(servlet);
                    NodeList paramNodeList = (NodeList) xPath.evaluate("init-param", nodeList.item(i), NODESET);
                    for (int j = 0; j < paramNodeList.getLength(); j++) {
                        WebXmlServletInitParam initParam = new WebXmlServletInitParam();
                        String name = parseString(xPath, "param-name/text()", paramNodeList.item(j));
                        initParam.setName(name);
                        String value = parseString(xPath, "param-value/text()", paramNodeList.item(j));
                        initParam.setValue(value);
                        servlet.addInitParam(initParam);
                    }
                    if (LOGGER.isLoggable(FINE)) {
                        LOGGER.log(FINE, "Configured servlet: {0}", servlet.toString());
                    }
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <filter> sections", xpe);
        }
    }

    /**
     * Parse the session-config section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseSessionConfig(WebXml webXml, XPath xPath, Node node) {
        try {
            Node scNode = (Node) xPath.evaluate("session-config", node, NODE);
            if (scNode != null) {
                WebXmlSessionConfig sessionConfig = new WebXmlSessionConfig();
                int sessionTimeout = parseInteger(
                        xPath, "session-timeout/text()", scNode);
                sessionConfig.setSessionTimeout(sessionTimeout);
                webXml.setSessionConfig(sessionConfig);
                Node cNode = (Node) xPath.evaluate("cookie-config", scNode, NODE);
                if (cNode != null) {
                    WebXmlCookieConfig cookieConfig = new WebXmlCookieConfig();
                    String name = parseString(xPath, "name/text()", cNode);
                    if (name != null) {
                        cookieConfig.setName(name);
                    }
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <session-config> section", xpe);
        }
    }

    /**
     * Parse a string.
     *
     * @param xPath the XPath to use.
     * @param expression the expression.
     * @param node the node.
     * @return the string.
     * @throws XPathExpressionException when the expression was invalid.
     */
    private String parseString(XPath xPath, String expression, Node node)
            throws XPathExpressionException {
        return (String) xPath.evaluate(expression, node, XPathConstants.STRING);
    }
}

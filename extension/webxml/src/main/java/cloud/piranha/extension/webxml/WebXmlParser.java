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
package cloud.piranha.extension.webxml;

import static cloud.piranha.extension.webxml.WebXml.OTHERS_TAG;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import static java.util.regex.Pattern.quote;
import static javax.xml.xpath.XPathConstants.NODE;
import static javax.xml.xpath.XPathConstants.NODESET;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.lang.System.Logger;
import java.util.stream.StreamSupport;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The web.xml / web-fragment.xml parser.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlParser {

    /**
     * Stores the 'location/text()' selector.
     */
    private static final String LOCATION_TEXT_SELECTOR = "location/text()";

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(WebXmlParser.class.getName());

    /**
     * Stores the 'servlet-name/text()' selector.
     */
    private static final String SERVLET_NAME_TEXT_SELECTOR = "servlet-name/text()";

    /**
     * Stores the 'text()' selector constant.
     */
    private static final String TEXT_SELECTOR = "text()";

    /**
     * Stores the 'url-pattern/text()' selector.
     */
    private static final String URL_PATTERN_TEXT_SELECTOR = "url-pattern/text()";

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
            parseAbsoluteOrdering(webXml, xPath, document);
            parseOrdering(webXml, xPath, document);
            parseContextParameters(webXml, xPath, document);
            parseDefaultContextPath(webXml, xPath, document);
            parseDenyUncoveredHttpMethods(webXml, xPath, document);
            parseDisplayName(webXml, xPath, document);
            parseFragmentName(webXml, xPath, document);
            parseDistributable(webXml, xPath, document);
            parseErrorPages(webXml, xPath, document);
            parseFilterMappings(webXml, xPath, document);
            parseFilters(webXml, xPath, document);
            parseListeners(webXml, xPath, document);
            parseLoginConfig(webXml, xPath, document);
            parseMimeMappings(webXml, xPath, document);
            parseRequestCharacterEncoding(webXml, xPath, document);
            parseResponseCharacterEncoding(webXml, xPath, document);
            parseLocaleEncodingMapping(webXml, xPath, document);
            processSecurityConstraints(webXml, xPath, document);
            processSecurityRoles(webXml, xPath, document);
            parseServletMappings(webXml, xPath, document);
            parseServlets(webXml, xPath, document);
            parseSessionConfig(webXml, xPath, document);
            parseWebApp(webXml, xPath, document);
            parseWelcomeFiles(webXml, xPath, document);
        } catch (Throwable t) {
            LOGGER.log(WARNING, "Unable to parse web.xml", t);
        }
        return webXml;
    }

    /**
     * Parse the name section of a fragment.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseFragmentName(WebXml webXml, XPath xPath, Node node) {
        try {
            String fragmentName = parseString(xPath, "//name/text()", node);
            if (fragmentName != null) {
                webXml.setFragmentName(fragmentName);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <name> section", xpe);
        }
    }

    private void parseAbsoluteOrdering(WebXml webXml, XPath xPath, Node rootNode) {
        try {
            Node absoluteOrderingNode = (Node) xPath.evaluate("//absolute-ordering", rootNode, NODE);
            if (absoluteOrderingNode == null) {
                return;
            }
            // It is possible to have only the <absolute-ordering/> to disable fragments
            List<String> fragmentNames = new ArrayList<>();
            NodeList childNodes = absoluteOrderingNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if ("others".equalsIgnoreCase(item.getNodeName())) {
                    fragmentNames.add(OTHERS_TAG);
                    continue;
                }
                String s = parseString(xPath, TEXT_SELECTOR, item);
                if (s != null && !s.trim().isEmpty()) {
                    fragmentNames.add(s);
                }
            }
            webXml.setAbsoluteOrdering(fragmentNames);
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <absolute-ordering> section", xpe);
        }
    }

    private void parseOrdering(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList before = (NodeList) xPath.evaluate("//ordering/before", node, NODESET);
            if (before.getLength() > 1) {
                throw new IllegalStateException("Cannot have multiple <before> tags in <ordering>");
            }

            NodeList after = (NodeList) xPath.evaluate("//ordering/after", node, NODESET);
            if (after.getLength() > 1) {
                throw new IllegalStateException("Cannot have multiple <after> tags in <ordering>");
            }

            List<String> beforeValues = parseOrderingChildren(xPath, before);

            List<String> afterValues = parseOrderingChildren(xPath, after);

            if (!beforeValues.isEmpty() || !afterValues.isEmpty()) {
                webXml.setRelativeOrdering(new WebXml.RelativeOrder(beforeValues, afterValues));
            }
        } catch (Exception xpe) {
            LOGGER.log(WARNING, "Unable to parse <ordering> section", xpe);
        }

    }

    private List<String> parseOrderingChildren(XPath xPath, NodeList orderingChild) throws XPathExpressionException {
        List<String> values = new ArrayList<>();
        if (orderingChild.getLength() == 1) {
            Node beforeTag = orderingChild.item(0);
            for (Node orderingNode : parseNodes(xPath, "*", beforeTag)) {
                String fragmentName = parseString(xPath, TEXT_SELECTOR, orderingNode);
                if (fragmentName != null && !fragmentName.trim().isEmpty()) {
                    values.add(fragmentName);
                    continue;
                }
                if ("others".equalsIgnoreCase(orderingNode.getNodeName())) {
                    values.add(OTHERS_TAG);
                }
            }
        }
        return values;
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
            result = Boolean.parseBoolean((String) xPath.evaluate(expression, node, XPathConstants.STRING));
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse boolean", xpe);
        }
        return result;
    }

    /**
     * Parse a long.
     *
     * @param xPath the XPath to use.
     * @param node the node to use.
     * @param expression the expression to use.
     * @return the long, or null if an error occurred.
     */
    private static Long parseLong(XPath xPath, String expression, Node node) {
        try {
            return Long.parseLong((String) xPath.evaluate(expression, node, XPathConstants.STRING));
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse boolean", xpe);
        }

        return null;
    }

    /**
     * Parse the context-param sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the node to use.
     */
    private void parseContextParameters(WebXml webXml, XPath xPath, Node node) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.evaluate("//context-param", node, NODESET);
        if (nodeList != null) {
            List<WebXmlContextParam> contextParams = webXml.getContextParams();
            for (int i = 0; i < nodeList.getLength(); i++) {
                String name = parseString(xPath, "//param-name/text()", nodeList.item(i));
                String value = parseString(xPath, "//param-value/text()", nodeList.item(i));
                contextParams.add(new WebXmlContextParam(name, value));
            }
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
     * @throws XPathExpressionException when an XPath error occurs.
     */
    private void parseDefaultContextPath(WebXml webXml, XPath xPath, Node node)
            throws XPathExpressionException {

        Node contextPathNode = (Node) xPath.evaluate("//default-context-path", node, NODE);
        if (contextPathNode != null) {
            String defaultContextPath = parseString(xPath, "//default-context-path/text()", node);
            if (defaultContextPath != null) {
                webXml.setDefaultContextPath(defaultContextPath);
            }
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
                    String location = parseString(xPath, LOCATION_TEXT_SELECTOR, nodeList.item(i));
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
     * @param rootNode the node to use.
     */
    private void parseFilterMappings(WebXml webXml, XPath xPath, Node rootNode) {
        try {
            for (Node node : parseNodes(xPath, "//filter-mapping", rootNode)) {
                String filterName = parseString(xPath, "filter-name/text()", node);
                WebXmlFilterMapping webXmlFilterMapping = new WebXmlFilterMapping(filterName);

                for (String urlPattern : parseStrings(xPath, URL_PATTERN_TEXT_SELECTOR, node)) {
                    webXmlFilterMapping.getUrlPatterns().add(urlPattern);
                }

                for (String servletName : parseStrings(xPath, SERVLET_NAME_TEXT_SELECTOR, node)) {
                    webXmlFilterMapping.getServletNames().add(servletName);
                }

                for (String dispatcher : parseStrings(xPath, "dispatcher/text()", node)) {
                    webXmlFilterMapping.getDispatchers().add(dispatcher);
                }

                webXml.getFilterMappings().add(webXmlFilterMapping);
            }
        } catch (XPathExpressionException xee) {
            LOGGER.log(WARNING, "Unable to parse <filter-mapping> sections", xee);
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
                    String servletName = parseString(xPath, SERVLET_NAME_TEXT_SELECTOR, nodeList.item(i));
                    filter.setServletName(servletName);

                    Boolean asyncSupported = parseBoolean(xPath, "async-supported/text()", nodeList.item(i));
                    if (asyncSupported != null) {
                        filter.setAsyncSupported(asyncSupported);
                    }

                    filters.add(filter);

                    NodeList paramNodeList = (NodeList) xPath.evaluate("init-param", nodeList.item(i), NODESET);
                    for (int j = 0; j < paramNodeList.getLength(); j++) {
                        String name = parseString(xPath, "param-name/text()", paramNodeList.item(j));
                        String value = parseString(xPath, "param-value/text()", paramNodeList.item(j));
                        filter.addInitParam(new WebXmlFilterInitParam(name, value));
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
    private int parseInteger(XPath xPath, String expression, Node node) throws XPathExpressionException {
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
     * @param node the node to use.
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

    private void processSecurityConstraints(WebXml webXml, XPath xPath, Node rootNode) {
        try {
            for (Node node : parseNodes(xPath, "//security-constraint", rootNode)) {
                processSecurityConstraint(webXml, xPath, node);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <security-constraint> sections", xpe);
        }
    }

    private void processSecurityConstraint(WebXml webXml, XPath xPath, Node rootNode) {
        try {
            WebXmlSecurityConstraint securityConstraint = new WebXmlSecurityConstraint();

            for (Node node : parseNodes(xPath, "web-resource-collection", rootNode)) {
                WebXmlSecurityConstraint.WebResourceCollection webResourceCollection = new WebXmlSecurityConstraint.WebResourceCollection();

                for (String urlPattern : parseStrings(xPath, URL_PATTERN_TEXT_SELECTOR, node)) {
                    webResourceCollection.getUrlPatterns().add(urlPattern);
                }

                for (String httpMethod : parseStrings(xPath, "http-method/text()", node)) {
                    webResourceCollection.getHttpMethods().add(httpMethod);
                }

                for (String httpMethodOmission : parseStrings(xPath, "http-method-omission/text()", node)) {
                    webResourceCollection.getHttpMethodOmissions().add(httpMethodOmission);
                }

                securityConstraint.getWebResourceCollections().add(webResourceCollection);
            }

            for (Node node : parseNodes(xPath, "auth-constraint", rootNode)) {
                for (String roleName : parseStrings(xPath, "role-name/text()", node)) {
                    securityConstraint.getRoleNames().add(roleName);
                }
            }

            securityConstraint.setTransportGuarantee(parseString(xPath, "user-data-constraint/transport-guarantee/text()", rootNode));

            webXml.getSecurityConstraints().add(securityConstraint);
        } catch (XPathExpressionException xee) {
            LOGGER.log(WARNING, "Unable to parse <security-constraint> sections", xee);
        }
    }

    private void processSecurityRoles(WebXml webXml, XPath xPath, Node rootNode) {
        try {
            for (String roleName : parseStrings(xPath, "//security-role/role-name/text()", rootNode)) {
                webXml.getRoleNames().add(roleName);
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <security-constraint> sections", xpe);
        }
    }

    /**
     * Parse the servlet-mapping sections.
     *
     * @param webXml the web.xml to use.
     * @param xPath the XPath to use.
     * @param node the node to use.
     */
    private void parseServletMappings(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//servlet-mapping", node, NODESET);
            if (nodeList != null) {
                List<WebXmlServletMapping> servletMappings = webXml.getServletMappings();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String servletName = parseString(xPath, SERVLET_NAME_TEXT_SELECTOR, nodeList.item(i));
                    for (String urlPattern : parseStrings(xPath, URL_PATTERN_TEXT_SELECTOR, nodeList.item(i))) {
                        servletMappings.add(new WebXmlServletMapping(servletName, urlPattern));
                    }
                }
            }
        } catch (XPathExpressionException xee) {
            LOGGER.log(WARNING, "Unable to parse <servlet-mapping> section", xee);
        }
    }

    /**
     * Parse the servlet sections.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param rootNode the DOM node.
     */
    private void parseServlets(WebXml webXml, XPath xPath, Node rootNode) {
        try {
            List<WebXmlServlet> servlets = webXml.getServlets();
            for (Node servletNode : parseNodes(xPath, "//servlet", rootNode)) {
                WebXmlServlet servlet = new WebXmlServlet();
                servlet.setServletName(parseString(xPath, SERVLET_NAME_TEXT_SELECTOR, servletNode));
                servlet.setClassName(parseString(xPath, "servlet-class/text()", servletNode));
                servlet.setJspFile(parseString(xPath, "jsp-file/text()", servletNode));

                Boolean asyncSupported = parseBoolean(xPath, "async-supported/text()", servletNode);
                if (asyncSupported != null) {
                    servlet.setAsyncSupported(asyncSupported);
                }

                for (Node initParamNode : parseNodes(xPath, "init-param", servletNode)) {
                    String name = parseString(xPath, "param-name/text()", initParamNode);
                    String value = parseString(xPath, "param-value/text()", initParamNode);
                    servlet.getInitParams().add(new WebXmlServletInitParam(name, value));
                }

                for (Node securityRoleRefNode : parseNodes(xPath, "security-role-ref", servletNode)) {
                    String roleName = parseString(xPath, "role-name/text()", securityRoleRefNode);
                    String roleLink = parseString(xPath, "role-link/text()", securityRoleRefNode);
                    servlet.getSecurityRoleRefs().add(new WebXmlServletSecurityRoleRef(roleName, roleLink));
                }

                for (Node multipartConfigNode : parseNodes(xPath, "multipart-config", servletNode)) {
                    if (servlet.getMultipartConfig() != null) {
                        LOGGER.log(WARNING, "Duplicate <multipart-config> sections in web.xml where only 1 allowed.");
                        break;
                    }

                    WebXmlServletMultipartConfig multipartConfig = new WebXmlServletMultipartConfig();
                    multipartConfig.setLocation(parseString(xPath, LOCATION_TEXT_SELECTOR, multipartConfigNode));
                    multipartConfig.setMaxFileSize(parseLong(xPath, LOCATION_TEXT_SELECTOR, multipartConfigNode));
                    multipartConfig.setMaxRequestSize(parseLong(xPath, LOCATION_TEXT_SELECTOR, multipartConfigNode));
                    multipartConfig.setFileSizeThreshold(parseInteger(xPath, LOCATION_TEXT_SELECTOR, multipartConfigNode));

                    servlet.setMultipartConfig(multipartConfig);
                }

                servlets.add(servlet);

                LOGGER.log(DEBUG, "Configured servlet: {0}", servlet);
            }

        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <servlet> sections", xpe);
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
            Node scNode = (Node) xPath.evaluate("//session-config", node, NODE);
            if (scNode != null) {
                int sessionTimeout = parseInteger(xPath, "session-timeout/text()", scNode);
                webXml.setSessionConfig(new WebXmlSessionConfig(sessionTimeout));
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <session-config> section", xpe);
        }
    }

    /**
     * Parse the default-context-path section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseWebApp(WebXml webXml, XPath xPath, Node node) {
        try {
            Node webAppNode = (Node) xPath.evaluate("//web-app", node, NODE);
            if (webAppNode != null) {
                NamedNodeMap attributes = webAppNode.getAttributes();
                if (attributes != null) {
                    Node versionNode = attributes.getNamedItem("version");
                    if (versionNode != null) {
                        String version = versionNode.getTextContent();
                        if (version != null) {
                            String[] versionComponents = version.split(quote("."));
                            if (versionComponents.length > 0) {
                                webXml.setMajorVersion(Integer.valueOf(versionComponents[0]));
                            }
                            if (versionComponents.length > 1) {
                                webXml.setMinorVersion(Integer.valueOf(versionComponents[1]));
                            }
                        }
                    }
                    Node metadataCompleteNode = attributes.getNamedItem("metadata-complete");
                    if (metadataCompleteNode != null) {
                        webXml.setMetadataComplete(Boolean.parseBoolean(metadataCompleteNode.getTextContent()));
                    }
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <web-app> section", xpe);
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
    private String parseString(XPath xPath, String expression, Node node) throws XPathExpressionException {
        return (String) xPath.evaluate(expression, node, XPathConstants.STRING);
    }

    private Iterable<Node> parseNodes(XPath xPath, String expression, Node node) throws XPathExpressionException {
        return StreamSupport
                .stream(toIterable((NodeList) xPath.evaluate(expression, node, NODESET)).spliterator(), false)::iterator;
    }

    private Iterable<String> parseStrings(XPath xPath, String expression, Node node) throws XPathExpressionException {
        return StreamSupport
                .stream(toIterable((NodeList) xPath.evaluate(expression, node, NODESET)).spliterator(), false)
                .map(Node::getNodeValue)::iterator;
    }

    /**
     * Convert nodes list to iterable nodes.
     *
     * @param nodeList the node list.
     * @return the iterable nodes.
     */
    public static Iterable<Node> toIterable(NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int position;

            @Override
            public boolean hasNext() {
                return position < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (hasNext()) {
                    return nodeList.item(position++);
                }

                throw new NoSuchElementException();
            }
        };
    }

    /**
     * Parse the welcome file section.
     *
     * @param webXml the web.xml to add to.
     * @param xPath the XPath to use.
     * @param node the DOM node.
     */
    private void parseWelcomeFiles(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//welcome-file-list/welcome-file", node, NODESET);
            if (nodeList != null) {
                List<String> welcomeFiles = webXml.getWelcomeFiles();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String welcomeFile = parseString(xPath, TEXT_SELECTOR, nodeList.item(i));
                    welcomeFiles.add(welcomeFile);
                    LOGGER.log(DEBUG, "Parsed welcome-file: {0}", welcomeFile);
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <welcome-file-list> sections", xpe);
        }
    }

    private void parseLocaleEncodingMapping(WebXml webXml, XPath xPath, Node node) {
        try {
            NodeList nodeList = (NodeList) xPath.evaluate("//locale-encoding-mapping-list/locale-encoding-mapping", node, NODESET);
            if (nodeList != null) {
                Map<String, String> localeEncodingMapping = webXml.getLocaleEncodingMapping();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String locale = parseString(xPath, ".//locale/text()", nodeList.item(i));
                    String encoding = parseString(xPath, ".//encoding/text()", nodeList.item(i));
                    localeEncodingMapping.put(locale, encoding);
                }
            }
        } catch (XPathException xpe) {
            LOGGER.log(WARNING, "Unable to parse <locale-encoding-mapping-list> sections", xpe);
        }
    }

}

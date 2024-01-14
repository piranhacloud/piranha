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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.CurrentRequestHolder;
import static cloud.piranha.core.api.CurrentRequestHolder.CURRENT_REQUEST_ATTRIBUTE;
import cloud.piranha.core.api.FilterEnvironment;
import cloud.piranha.core.api.ServletEnvironment;
import cloud.piranha.core.api.WebApplicationRequest;
import static cloud.piranha.core.impl.DefaultWebApplicationRequest.unwrap;
import static jakarta.servlet.AsyncContext.ASYNC_CONTEXT_PATH;
import static jakarta.servlet.AsyncContext.ASYNC_PATH_INFO;
import static jakarta.servlet.AsyncContext.ASYNC_QUERY_STRING;
import static jakarta.servlet.AsyncContext.ASYNC_REQUEST_URI;
import static jakarta.servlet.AsyncContext.ASYNC_SERVLET_PATH;
import static jakarta.servlet.DispatcherType.ASYNC;
import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;
import static jakarta.servlet.DispatcherType.INCLUDE;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The default ServletRequestDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServletRequestDispatcher implements RequestDispatcher {

    /**
     * Stores the previous request attribute name
     */
    static final String PREVIOUS_REQUEST = "piranha.previous.request";

    /**
     * Stores the async attributes.
     */
    private static final List<String> ASYNC_ATTRIBUTES = Arrays.asList(
            ASYNC_CONTEXT_PATH,
            ASYNC_PATH_INFO,
            ASYNC_QUERY_STRING,
            ASYNC_REQUEST_URI,
            ASYNC_SERVLET_PATH);

    /**
     * Stores the servlet invocation.
     */
    private final DefaultServletInvocation servletInvocation;

    /**
     * The servletEnvironment corresponding to the target resource to which this
     * dispatcher forwards or includes.
     *
     * <p>
     * It contains the actual Servlet, to process the forwarded or included
     * request, as well as meta data for this Servlet.
     */
    private final ServletEnvironment servletEnvironment;

    /**
     * Stores the path.
     */
    private final String path;

    /**
     * Stores the invocation finder.
     */
    private final DefaultInvocationFinder invocationFinder;

    /**
     * Stores the web application.
     */
    private final DefaultWebApplication webApplication;

    /**
     * Constructor.
     *
     * @param servletInvocation The servlet invocation containing all info this
     * dispatcher uses to dispatch to the contained Servlet.
     * @param webApplication the web application.
     */
    public DefaultServletRequestDispatcher(DefaultServletInvocation servletInvocation, DefaultWebApplication webApplication) {
        this.servletInvocation = servletInvocation;

        this.webApplication = webApplication;
        this.invocationFinder = webApplication.invocationFinder;

        this.servletEnvironment = servletInvocation == null ? null : servletInvocation.getServletEnvironment();
        this.path = servletInvocation == null ? null : servletInvocation.getInvocationPath();
    }

    /**
     * Dispatches using the REQUEST dispatch type
     *
     * @param webappRequest the request.
     * @param webappResponse the response.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    public void request(DefaultWebApplicationRequest webappRequest, DefaultWebApplicationResponse webappResponse) throws ServletException, IOException {
        Throwable exception = null;

        if (servletInvocation == null || !servletInvocation.canInvoke() && !servletInvocation.isServletUnavailable()) {
            // If there's nothing to invoke at all, there was nothing found, so return a 404
            webappResponse.sendError(404);
        } else {

            // There's either a Servlet, Filter or both found matching the request.
            try {
                if (servletInvocation.getServletEnvironment() != null) {
                    webappRequest.setAsyncSupported(isAsyncSupportedInChain());
                }
                webappRequest.setMultipartConfig(servletEnvironment.getMultipartConfig());
                webappRequest.setServletPath(servletInvocation.getServletPath());
                webappRequest.setOriginalServletPath(servletInvocation.getOriginalServletPath());
                webappRequest.setPathInfo(servletInvocation.getPathInfo());
                webappRequest.setHttpServletMapping(servletInvocation.getHttpServletMapping());

                servletInvocation.getFilterChain().doFilter(webappRequest, webappResponse);
            } catch (Exception e) {
                if (webappRequest.getAttribute("piranha.request.exception") != null) {
                    exception = (Exception) webappRequest.getAttribute("piranha.request.exception");
                } else {
                    exception = e;
                }
            }
        }

        /*
         * REFACTOR - We used a response header to signal that we are not 
         * listening to add/setHeader. In the block below we remove the header
         * and reset the buffer as we need the code below do its work. However
         * we  really need to refactor this and move the code below to the 
         * DefaultWebApplicationResponse, because it should be handled in the
         * sendError call.
         */
        if (webappResponse.getHeader("sendErrorCalled") != null) {
            webappResponse.headerManager.removeHeader("sendErrorCalled");
            if (!webappResponse.isCommitted()) {
                webappResponse.resetBuffer();
            }
        }

        if (exception != null) {
            webappResponse.setStatus(exception instanceof UnavailableException ? SC_NOT_FOUND : SC_INTERNAL_SERVER_ERROR);
        }

        String errorPagePath = webApplication.getManager().getErrorPageManager() != null
                ? webApplication.getManager().getErrorPageManager().getErrorPage(exception, webappResponse)
                : null;

        if (errorPagePath != null) {
            try {
                webApplication.getRequestDispatcher(errorPagePath).error(servletInvocation == null ? null : servletInvocation.getServletName(), webappRequest, webappResponse, exception);
            } catch (Exception e) {
                rethrow(e);
            }
        } else if (exception != null) {
            exception.printStackTrace(webappResponse.getWriter());
            webappResponse.flushBuffer();
            rethrow(exception);
        } else if (webappRequest.getAttribute(ERROR_MESSAGE) != null) {
            // Specified by spec/javadoc: "The server defaults to creating the response to look like an HTML-formatted server error page containing the specified message,
            // setting the content type to "text/html"."
            webappResponse.setContentType("text/html");
            webappResponse.getWriter()
                    .write("<html><body>" + webappRequest.getAttribute(ERROR_MESSAGE) + "</body></html>");

        }

        if (!webappRequest.isAsyncStarted()) {
            webappResponse.flushBuffer();
        }
    }

    /**
     * Dispatches using the FORWARD or ASYNC dispatch type - Forward the request
     * and response.
     *
     * @param servletRequest the request.
     * @param servletResponse the response.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        if (servletRequest.getDispatcherType().equals(ASYNC)) {

            // Asynchronous forward
            asyncForward(servletRequest, servletResponse);
            return;
        }

        // Regular (synchronous) forward
        dispatchSyncForward(servletRequest, servletResponse);
    }

    /**
     * Dispatches using the INCLUDE dispatch type - Include the request and
     * response.
     *
     * @param servletRequest the request.
     * @param servletResponse the response.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        /*
         * We only do includes for a HttpServletRequest/Response pair.
         */
        if (!(servletRequest instanceof HttpServletRequest)) {
            throw new ServletException("Request is not a HttpServletRequest");
        }
        if (!(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("Response is not a HttpServletResponse");
        }

        /*
         * Unwrap the passed ServletRequest to the underlying WebApplicationRequest.
         */
        WebApplicationRequest request = unwrap(servletRequest, WebApplicationRequest.class);
        
        /**
         * Set the include attributes.
         */
        request.setAttribute(INCLUDE_CONTEXT_PATH, request.getContextPath());
        request.setAttribute(INCLUDE_PATH_INFO, request.getPathInfo());
        request.setAttribute(INCLUDE_QUERY_STRING, request.getQueryString());
        request.setAttribute(INCLUDE_REQUEST_URI, request.getRequestURI());
        request.setAttribute(INCLUDE_SERVLET_PATH, request.getServletPath());
        
        /**
         * Set the new dispatcher type.
         */
        request.setDispatcherType(INCLUDE);
        request.setServletPath(path == null ? "/" + servletEnvironment.getServletName() : getServletPath(path));
        request.setPathInfo(null);

        invocationFinder.addFilters(INCLUDE, servletInvocation, request.getServletPath(), "");

        if (servletInvocation.getServletEnvironment() != null) {
            request.setAsyncSupported(request.isAsyncSupported() && isAsyncSupportedInChain());
        }

        /*
         * Execute the filter chain.
         */
        try {
            servletEnvironment.getWebApplication().linkRequestAndResponse(servletRequest, servletResponse);
            servletInvocation.getFilterChain().doFilter(servletRequest, servletResponse);
            servletEnvironment.getWebApplication().unlinkRequestAndResponse(servletRequest, servletResponse);
        } catch(Exception e) {
            rethrow(e);
        } finally {
            /*
             * Set servlet path and path info back to original values.
             */
            request.setServletPath((String) request.getAttribute(INCLUDE_SERVLET_PATH));
            request.setPathInfo((String) request.getAttribute(INCLUDE_PATH_INFO));
            
            /*
             * Remove include attributes.
             */
            request.removeAttribute(INCLUDE_CONTEXT_PATH);
            request.removeAttribute(INCLUDE_PATH_INFO);
            request.removeAttribute(INCLUDE_QUERY_STRING);
            request.removeAttribute(INCLUDE_REQUEST_URI);
            request.removeAttribute(INCLUDE_SERVLET_PATH);
        }
    }

    private ServletRequestWrapper getLastWrapper(ServletRequestWrapper wrapper) {
        ServletRequestWrapper currentWrapper = wrapper;
        ServletRequest currentRequest = wrapper;
        while (currentRequest instanceof ServletRequestWrapper wrapped) {
            currentWrapper = wrapped;
            currentRequest = currentWrapper.getRequest();
        }

        return currentWrapper;
    }

    /**
     * Send an error response.
     *
     * @param servletName the servlet name.
     * @param servletRequest the servlet request.
     * @param servletResponse the servlet response.
     * @param throwable the throwable.
     * @throws Exception when a serious error occurs.
     */
    public void error(String servletName, ServletRequest servletRequest, ServletResponse servletResponse, Throwable throwable) throws Exception {
        DefaultWebApplicationRequest errorRequest = new DefaultWebApplicationRequest();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.resetBuffer();

        errorRequest.setWebApplication(servletEnvironment.getWebApplication());
        errorRequest.setMultipartConfig(servletEnvironment.getMultipartConfig());
        errorRequest.setContextPath(request.getContextPath());
        errorRequest.setDispatcherType(ERROR);
        HttpSession session = request.getSession(false);
        if (session != null) {
            errorRequest.setCurrentSessionId(session.getId());
        }

        if (path != null) {
            setForwardAttributes(request, errorRequest,
                    FORWARD_CONTEXT_PATH,
                    FORWARD_PATH_INFO,
                    FORWARD_QUERY_STRING,
                    FORWARD_REQUEST_URI,
                    FORWARD_SERVLET_PATH);

            errorRequest.setServletPath(getServletPath(path));
            errorRequest.setQueryString(getQueryString(path));

        } else {
            errorRequest.setServletPath("/" + servletEnvironment.getServletName());
        }

        if (throwable instanceof ServletException servletException) {
            errorRequest.setAttribute(ERROR_EXCEPTION, servletException.getRootCause() == null ? servletException : servletException.getRootCause());
            errorRequest.setAttribute(ERROR_EXCEPTION_TYPE, servletException.getRootCause() == null ? servletException.getClass() : servletException.getRootCause().getClass());
            errorRequest.setAttribute(ERROR_MESSAGE, servletException.getMessage());
        } else {
            errorRequest.setAttribute(ERROR_EXCEPTION, throwable);
            errorRequest.setAttribute(ERROR_EXCEPTION_TYPE, throwable == null ? null : throwable.getClass());
            errorRequest.setAttribute(ERROR_MESSAGE, throwable == null ? null : throwable.getMessage());
        }
        errorRequest.setAttribute(ERROR_STATUS_CODE, response.getStatus());
        errorRequest.setAttribute(ERROR_REQUEST_URI, request.getRequestURI());
        errorRequest.setAttribute(ERROR_SERVLET_NAME, servletName);

        CurrentRequestHolder currentRequestHolder = updateCurrentRequest(request, errorRequest);

        copyAttributesFromRequest(request, errorRequest, attribute -> true);

        invocationFinder.addFilters(ERROR, servletInvocation, errorRequest.getServletPath(), "");

        if (servletInvocation.getServletEnvironment() != null) {
            errorRequest.setAsyncSupported(request.isAsyncSupported() && isAsyncSupportedInChain());
        }

        try {
            servletEnvironment.getWebApplication().linkRequestAndResponse(errorRequest, servletResponse);

            servletInvocation.getFilterChain().doFilter(errorRequest, servletResponse);

            servletEnvironment.getWebApplication().unlinkRequestAndResponse(errorRequest, servletResponse);
        } finally {
            restoreCurrentRequest(currentRequestHolder, request);
        }

        response.flushBuffer();
    }

    /**
     * Checks if all filters (if any) and the servlet support async
     *
     * @return true if all supports, false otherwise
     */
    private boolean isAsyncSupportedInChain() {
        List<FilterEnvironment> filterEnvironments = servletInvocation.getFilterEnvironments();
        boolean hasFilterAsync = filterEnvironments == null || filterEnvironments.stream().allMatch(FilterEnvironment::isAsyncSupported);
        return servletInvocation.getServletEnvironment().isAsyncSupported() && hasFilterAsync;
    }

    private void setForwardAttributes(HttpServletRequest originalRequest, HttpServletRequest forwardedRequest, String... dispatcherKeys) {
        for (String dispatcherKey : dispatcherKeys) {
            setForwardAttribute(originalRequest, forwardedRequest, dispatcherKey);
        }
    }

    /**
     * Set forward attribute.
     *
     * @param originalRequest the original request
     * @param forwardedRequest the forward request.
     * @param dispatcherKey the dispatcher key.
     */
    private void setForwardAttribute(HttpServletRequest originalRequest, HttpServletRequest forwardedRequest, String dispatcherKey) {
        String value = null;

        if (originalRequest.getAttribute(dispatcherKey) != null) {
            value = (String) originalRequest.getAttribute(dispatcherKey);
        } else {
            if (dispatcherKey.equals(FORWARD_CONTEXT_PATH)) {
                value = originalRequest.getContextPath();
            }
            if (dispatcherKey.equals(FORWARD_PATH_INFO)) {
                value = originalRequest.getPathInfo();
            }
            if (dispatcherKey.equals(FORWARD_QUERY_STRING)) {
                value = originalRequest.getQueryString();
            }
            if (dispatcherKey.equals(FORWARD_REQUEST_URI)) {
                value = originalRequest.getRequestURI();
            }
            if (dispatcherKey.equals(FORWARD_SERVLET_PATH)) {
                value = originalRequest.getServletPath();
            }
        }

        forwardedRequest.setAttribute(dispatcherKey, value);
    }

    private CurrentRequestHolder updateCurrentRequest(HttpServletRequest originalRequest, HttpServletRequest forwardedRequest) {
        CurrentRequestHolder currentRequestHolder = (CurrentRequestHolder) originalRequest.getAttribute(CURRENT_REQUEST_ATTRIBUTE);
        if (currentRequestHolder != null) {
            currentRequestHolder.setRequest(forwardedRequest);
            forwardedRequest.setAttribute(CURRENT_REQUEST_ATTRIBUTE, currentRequestHolder);
        }

        forwardedRequest.setAttribute(PREVIOUS_REQUEST, originalRequest);

        return currentRequestHolder;
    }

    private void copyAttributesFromRequest(ServletRequest fromRequest, ServletRequest toRequest, Predicate<String> attributesToExclude) {
        Collections.list(fromRequest.getAttributeNames())
                .stream()
                .filter(attributesToExclude)
                .forEach(attributeName -> toRequest.setAttribute(attributeName, fromRequest.getAttribute(attributeName)));
    }

    private void restoreCurrentRequest(CurrentRequestHolder currentRequestHolder, HttpServletRequest originalRequest) {
        if (currentRequestHolder != null) {
            currentRequestHolder.setRequest(originalRequest);
        }
    }

    // #### ASYNC forward private methods
    private void asyncForward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

        if (servletRequest instanceof AsyncHttpDispatchWrapper
                || servletRequest instanceof AsyncNonHttpDispatchWrapper) {

            if (servletRequest instanceof AsyncHttpDispatchWrapper wrapper) {
                // The caller provided or let us default to an HttpServletRequest
                asyncHttpForward(wrapper, servletResponse);
                return;
            }

            // The caller provided a ServletRequest
            asyncNonHttpForward((AsyncNonHttpDispatchWrapper) servletRequest, servletResponse);
        } else {
            throw new IllegalStateException("Async invocations without wrapper not supported at this moment.");
        }
    }

    private void asyncHttpForward(AsyncHttpDispatchWrapper asyncHttpDispatchWrapper, ServletResponse servletResponse) throws ServletException, IOException {
        // A typical chain to arrive here is DefaultAsyncContext#dispatch -> DefaultAsyncDispatcher#dispatch -> forward -> asyncForwrd -> asyncHttpForward

        HttpServletRequest asyncStartRequest = asyncHttpDispatchWrapper.getRequest();

        if (asyncStartRequest instanceof WebApplicationRequest) {
            // original request or previously dispatched request passed-in, not an application wrapped one
            // In this case our asyncHttpDispatchWrapper is both the object with which the Servlet will be invoked, as well as the
            // object on which the path and attributes for the previous path will be set.

            invokeTargetAsyncServlet(asyncHttpDispatchWrapper, servletResponse);

        } else if (asyncStartRequest instanceof HttpServletRequestWrapper applicationProvidedWrapper) {
            // Application wrapped request passed-in. We now need no make sure that the applications sees this request

            // We swap our asyncHttpDispatchWrapper from being the head of the chain, to be in between the request that was provided by the application
            // and the request it is wrapping.
            ServletRequest wrappedRequest = applicationProvidedWrapper.getRequest();

            applicationProvidedWrapper.setRequest(asyncHttpDispatchWrapper);
            asyncHttpDispatchWrapper.setRequest(wrappedRequest);

            // Original chain: asyncHttpDispatchWrapper -> applicationProvidedWrapper (asyncStartRequest) -> wrappedRequest
            // New chain: applicationProvidedWrapper (asyncStartRequest) -> asyncHttpDispatchWrapper -> wrappedRequest
            invokeTargetAsyncServlet(applicationProvidedWrapper, asyncHttpDispatchWrapper, servletResponse);

        } else {
            throw new IllegalStateException("Async invocation with a request that was neither the original one nor a wrapped one: " + asyncStartRequest);
        }
    }

    private void asyncNonHttpForward(AsyncNonHttpDispatchWrapper asyncNonHttpDispatchWrapper, ServletResponse servletResponse) throws ServletException, IOException {
        // A typical chain to arrive here is DefaultAsyncContext#dispatch -> DefaultAsyncDispatcher#dispatch -> forward -> asyncForward -> asyncNonHttpForward

        ServletRequest asyncStartRequest = asyncNonHttpDispatchWrapper.getRequest();

        if (asyncStartRequest instanceof ServletRequestWrapper applicationProvidedWrapper) {

            HttpServletRequest httpServletRequestInChain = findHttpServletRequestInChain(applicationProvidedWrapper);

            if (httpServletRequestInChain != null) {

                // We swap our asyncHttpDispatchWrapper from being the head of the chain, with a new wrapper, wrapping the HttpServletRequest that we found, and put
                // that in between the request that was provided by the application and the request it is wrapping.
                ServletRequest wrappedRequest = applicationProvidedWrapper.getRequest();

                // Note that by doing this, methods called on HttpServletRequestWrapper itself (and not its super interface) will throw.
                AsyncHttpDispatchWrapper newAsyncHttpDispatchWrapper = new AsyncHttpDispatchWrapper((HttpServletRequest) wrappedRequest);

                applicationProvidedWrapper.setRequest(newAsyncHttpDispatchWrapper);

                // Original chain: asyncNonHttpDispatchWrapper -> applicationProvidedWrapper (asyncStartRequest) -> wrappedRequest -> .... -> HttpServletRequest
                // New chain: applicationProvidedWrapper (asyncStartRequest) -> newAsyncHttpDispatchWrapper -> wrappedRequest -> .... -> HttpServletRequest
                invokeTargetAsyncServlet(asyncStartRequest, httpServletRequestInChain, newAsyncHttpDispatchWrapper, servletResponse);
            }

        }
    }

    private void invokeTargetAsyncServlet(AsyncHttpDispatchWrapper asyncHttpDispatchWrapper, ServletResponse servletResponse) throws ServletException, IOException {
        invokeTargetAsyncServlet(asyncHttpDispatchWrapper, asyncHttpDispatchWrapper, servletResponse);
    }

    private void invokeTargetAsyncServlet(HttpServletRequest invokeServletRequest, AsyncHttpDispatchWrapper asyncHttpDispatchWrapper, ServletResponse servletResponse) throws ServletException, IOException {
        invokeTargetAsyncServlet(invokeServletRequest, invokeServletRequest, asyncHttpDispatchWrapper, servletResponse);
    }

    private void invokeTargetAsyncServlet(ServletRequest invokeServletRequest, HttpServletRequest previousPathRequest, AsyncHttpDispatchWrapper asyncHttpDispatchWrapper, ServletResponse servletResponse) throws ServletException, IOException {
        // A typical call chain to arrive here is DefaultAsyncContext#dispatch -> DefaultAsyncDispatcher#dispatch -> forward -> asyncForwrd -> asyncHttpForward -> invokeTargetAsyncServlet

        if (path != null) {

            setAsyncAttributes(previousPathRequest, asyncHttpDispatchWrapper);

            asyncHttpDispatchWrapper.setServletPath(getServletPath(path));

            String queryString = getQueryString(path);
            if (queryString != null && !queryString.trim().equals("")) {
                asyncHttpDispatchWrapper.setQueryString(queryString);
                setRequestParameters(queryString, asyncHttpDispatchWrapper);
            } else {
                asyncHttpDispatchWrapper.setQueryString(previousPathRequest.getQueryString());
            }

            asyncHttpDispatchWrapper.setRequestURI(previousPathRequest.getServletContext().getContextPath() + getServletPath(path));
            asyncHttpDispatchWrapper.setAsWrapperAttribute(PREVIOUS_REQUEST, invokeServletRequest);

        } else {
            asyncHttpDispatchWrapper.setServletPath("/" + servletEnvironment.getServletName());
        }

        servletEnvironment.getWebApplication().linkRequestAndResponse(invokeServletRequest, servletResponse);
        servletEnvironment.getServlet().service(invokeServletRequest, servletResponse);
        servletEnvironment.getWebApplication().unlinkRequestAndResponse(invokeServletRequest, servletResponse);
    }

    private void setAsyncAttributes(HttpServletRequest asyncStartRequest, AsyncHttpDispatchWrapper asyncHttpDispatchWrapper) {
        for (String asyncAttribute : ASYNC_ATTRIBUTES) {
            // Set the spec demanded attributes on asyncHttpDispatchWrapper with the values taken from asyncStartRequest
            setAsyncAttribute(asyncStartRequest, asyncHttpDispatchWrapper, asyncAttribute);
        }
    }

    private void setAsyncAttribute(HttpServletRequest originalRequest, AsyncHttpDispatchWrapper asyncHttpDispatchWrapper, String dispatcherKey) {
        String value = null;

        if (originalRequest.getAttribute(dispatcherKey) != null) {
            value = (String) originalRequest.getAttribute(dispatcherKey);
        } else {
            if (dispatcherKey.equals(ASYNC_CONTEXT_PATH)) {
                value = originalRequest.getContextPath();
            }
            if (dispatcherKey.equals(ASYNC_PATH_INFO)) {
                value = originalRequest.getPathInfo();
            }
            if (dispatcherKey.equals(ASYNC_QUERY_STRING)) {
                value = originalRequest.getQueryString();
            }
            if (dispatcherKey.equals(ASYNC_REQUEST_URI)) {
                value = originalRequest.getRequestURI();
            }
            if (dispatcherKey.equals(ASYNC_SERVLET_PATH)) {
                value = originalRequest.getServletPath();
            }
        }

        asyncHttpDispatchWrapper.setAsWrapperAttribute(dispatcherKey, value);
    }

    private void setRequestParameters(String queryString, AsyncHttpDispatchWrapper asyncHttpDispatchWrapper) {
        Map<String, String[]> parameters = asyncHttpDispatchWrapper.getWrapperParameters();

        if (queryString != null) {
            for (String param : queryString.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                }
                String[] values = parameters.get(key);
                if (values == null) {
                    values = new String[]{value};
                    parameters.put(key, values);
                } else {
                    String[] newValues = new String[values.length + 1];
                    System.arraycopy(values, 0, newValues, 0, values.length);
                    newValues[values.length] = value;
                    parameters.put(key, newValues);
                }
            }
        }

    }

    private String getServletPath(String path) {
        return !path.contains("?") ? path : path.substring(0, path.indexOf("?"));
    }

    private String getQueryString(String path) {
        return !path.contains("?") ? null : path.substring(path.indexOf("?") + 1);
    }

    private HttpServletRequest findHttpServletRequestInChain(ServletRequest request) {
        ServletRequest currentRequest = request;
        while (currentRequest instanceof ServletRequestWrapper wrapper) {
            currentRequest = wrapper.getRequest();

            if (currentRequest instanceof HttpServletRequest httpServletRequest) {
                return httpServletRequest;
            }
        }
        return null;
    }

    private void rethrow(Throwable exception) throws ServletException, IOException {
        if (exception instanceof ServletException servletException) {
            throw servletException;
        }

        if (exception instanceof IOException ioException) {
            throw ioException;
        }

        if (exception instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }

        throw new IllegalStateException(exception);
    }

    /**
     * Execute a forward.
     *
     * @param servletRequest the Servlet request.
     * @param servletResponse the Servlet response.
     */
    private void dispatchSyncForward(
            ServletRequest servletRequest,
            ServletResponse servletResponse)
            throws ServletException, IOException {

        /*
         * If response is already committed we need to throw an IllegalStateException.
        */
        if (servletResponse.isCommitted()) {
            throw new IllegalStateException("Response already committed");
        }

        /*
         * We only do forwards for a HttpServletRequest/Response pair.
         */
        if (!(servletRequest instanceof HttpServletRequest)) {
            throw new ServletException("Request is not a HttpServletRequest");
        }
        if (!(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("Response is not a HttpServletResponse");
        }

        /*
         * Reset the buffer if we haven't committed yet.
         */
        servletResponse.resetBuffer();

        /*
         * Unwrap the passed ServletRequest to the underlying WebApplicationRequest.
         */
        WebApplicationRequest request = unwrap(servletRequest, WebApplicationRequest.class);
        
        /*
         * Set the forward attributes.
         */
        request.setAttribute(FORWARD_CONTEXT_PATH, request.getContextPath());
        request.setAttribute(FORWARD_PATH_INFO, request.getPathInfo());
        request.setAttribute(FORWARD_QUERY_STRING, request.getQueryString());
        request.setAttribute(FORWARD_REQUEST_URI, request.getRequestURI());
        request.setAttribute(FORWARD_SERVLET_PATH, request.getServletPath());
        
        /*
         * Set the new dispatcher type.
         */
        request.setDispatcherType(FORWARD);

        /*
         * Set the new servlet path and the new query string.
         */
        if (path != null) {
            request.setServletPath(getServletPath(path));
            request.setQueryString(getQueryString(path));
        } else {
            request.setServletPath("/" + servletEnvironment.getServletName());
            request.setQueryString(request.getQueryString());
        }
        
        /*
         * Aggregate the request parameters with giving the new parameter values
         * precedence over the old ones.
         */
        if (request.getQueryString() != null) {
            String queryString = request.getQueryString();
            Map<String, String[]> parameters = request.getModifiableParameterMap();
            for (String param : queryString.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                }
                String[] values = parameters.get(key);
                if (values == null) {
                    values = new String[]{value};
                    parameters.put(key, values);
                } else {
                    String[] newValues = new String[values.length + 1];
                    System.arraycopy(values, 0, newValues, 1, values.length);
                    newValues[0] = value;
                    parameters.put(key, newValues);
                }
            }
        }
        
        invocationFinder.addFilters(FORWARD, servletInvocation, request.getServletPath(), "");

        if (servletInvocation.getServletEnvironment() != null) {
            request.setAsyncSupported(request.isAsyncSupported() && isAsyncSupportedInChain());
        }

        /*
         * Execute the filter chain.
         */
        servletEnvironment.getWebApplication().linkRequestAndResponse(servletRequest, servletResponse);
        servletInvocation.getFilterChain().doFilter(servletRequest, servletResponse);
        servletEnvironment.getWebApplication().unlinkRequestAndResponse(servletRequest, servletResponse);

        /*
         * Flush the response buffer.
         */
        servletResponse.flushBuffer();
    }
}

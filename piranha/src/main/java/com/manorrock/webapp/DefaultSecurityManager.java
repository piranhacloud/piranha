/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * The default SecurityManager.
 *
 * <p>
 * This security manager implies the use of DefaultHttpServletRequest, if your
 * server / web application does not want to use DefaultHttpServletRequest or
 * subclass DefaultHttpServletRequest you have to implement your own security
 * manager.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultSecurityManager implements SecurityManager {

    /**
     * Stores the logins.
     */
    protected final HashMap<String, String> logins = new HashMap<>();

    /**
     * Stores the roles.
     */
    protected ArrayList<String> roles = new ArrayList<>();

    /**
     * Stores the user roles.
     */
    protected final HashMap<String, String[]> userRoles = new HashMap<>();

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Add user.
     *
     * @param username the username.
     * @param password the password.
     * @param roles the roles.
     */
    public void addUser(String username, String password, String... roles) {
        logins.put(username, password);
        userRoles.put(username, roles);
    }

    /**
     * Authenticate the request.
     *
     * @param request the request.
     * @param response the response.
     * @return true if authenticated, false otherwise.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public boolean authenticate(
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        boolean result = false;

        if (request.getAuthType() != null) {
            if (request.getAuthType().equals(HttpServletRequest.BASIC_AUTH)) {
                throw new ServletException("Basic auth is not supported");
            }

            if (request.getAuthType().equals(HttpServletRequest.CLIENT_CERT_AUTH)) {
                throw new ServletException("Client cert auth is not supported");
            }

            if (request.getAuthType().equals(HttpServletRequest.DIGEST_AUTH)) {
                throw new ServletException("Digest auth is not supported");
            }

            if (request.getAuthType().equals(HttpServletRequest.FORM_AUTH)) {
                String username = request.getParameter("j_username");
                String password = request.getParameter("j_password");
                login(request, username, password);
                if (request.getUserPrincipal() != null) {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Declare roles.
     *
     * @param roles the roles.
     */
    @Override
    public void declareRoles(String[] roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }

    /**
     * Is the user in the given role.
     *
     * @param request the request.
     * @param role the role.
     * @return true if in the role, false otherwise.
     */
    @Override
    public boolean isUserInRole(HttpServletRequest request, String role) {
        boolean result = false;

        if (request.getRemoteUser() != null) {
            String foundRoles[] = userRoles.get(request.getRemoteUser());
            if (foundRoles != null) {
                for (String foundRole : foundRoles) {
                    if (foundRole.equals(role)) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Login with the given username and password.
     *
     * @param request the servlet request.
     * @param username the username.
     * @param password the password.
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public void login(HttpServletRequest request, String username, String password) throws ServletException {

        if (logins.containsKey(username) && password != null && password.equals(logins.get(username))) {
            DefaultHttpServletRequest abstractRequest;

            while (request instanceof HttpServletRequestWrapper) {
                HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper) request;
                request = (HttpServletRequest) wrapper.getRequest();
            }
            abstractRequest = (DefaultHttpServletRequest) request;
            abstractRequest.setUserPrincipal(new DefaultSecurityPrincipal(username));
        } else {
            throw new ServletException("Unable to login using the given username and password");
        }
    }

    /**
     * Logout.
     *
     * @param request the request.
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public void logout(HttpServletRequest request) throws ServletException {
    }

    /**
     * Remove the given user.
     *
     * @param username the username.
     */
    public void removeUser(String username) {
        logins.remove(username);
        userRoles.remove(username);
    }

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }
}

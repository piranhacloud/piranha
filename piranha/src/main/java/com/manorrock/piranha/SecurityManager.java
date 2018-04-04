/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The SecurityManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface SecurityManager {

    /**
     * Authenticate the request.
     *
     * @param request the request.
     * @param response the response.
     * @return true if authenticated.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    boolean authenticate(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException;

    /**
     * Declare roles.
     *
     * @param roles the roles.
     */
    void declareRoles(String[] roles);

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    WebApplication getWebApplication();

    /**
     * Is the user in the specific role.
     *
     * @param request the request.
     * @param role the role.
     * @return true if in the role, false otherwise.
     */
    boolean isUserInRole(HttpServletRequest request, String role);

    /**
     * Login.
     *
     * @param request the request.
     * @param username the username.
     * @param password the password.
     * @throws ServletException when unable to login.
     */
    void login(HttpServletRequest request,
            String username, String password) throws ServletException;

    /**
     * Logout.
     *
     * @param request the request.
     * @throws ServletException when a servlet error occurs.
     */
    void logout(HttpServletRequest request) throws ServletException;

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);
}

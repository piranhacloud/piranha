/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.soteria;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.security.DeclareRoles;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.glassfish.soteria.identitystores.annotation.Credentials;
import org.glassfish.soteria.identitystores.annotation.EmbeddedIdentityStoreDefinition;

@BasicAuthenticationMechanismDefinition(realmName = "soteria")
@EmbeddedIdentityStoreDefinition({ 
    @Credentials(callerName = "admin", password = "admin", groups = {"group1", "group2"})
    ,@Credentials(callerName = "user", password = "user", groups = {"group1", "group3"})
    ,@Credentials(callerName = "guest", password = "guest", groups = {"group1"})}
)
@WebServlet("/servlet")
@DeclareRoles({"group1", "group2", "group3"})
@ServletSecurity(
        @HttpConstraint(rolesAllowed = "group1"))
public class Servlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = null;
        if (request.getUserPrincipal() != null) {
            username = request.getUserPrincipal().getName();
        }
        PrintWriter writer = response.getWriter();
        writer.write("Username: " + username + "\n");
        writer.write("In group1: " + request.isUserInRole("group1") + "\n");
        writer.write("In group2: " + request.isUserInRole("group2") + "\n");
        writer.write("In group3: " + request.isUserInRole("group3") + "\n");
    }
}

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
package cloud.piranha.test.eleos.programmatic.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/public/authenticate")
public class AuthenticateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.getWriter().write("This is a public servlet \n");

        String webName = null;
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }

        response.getWriter().write("before web username: " + webName + "\n");
        boolean webHasRole = request.isUserInRole("architect");
        response.getWriter().write("before web user has role \"architect\": " + webHasRole + "\n");
        
        // By *not* setting the "doLogin" request attribute the request.authenticate call
        // would do nothing. request.authenticate is a mandatory authentication, so doing
        // nothing is not allowed. But one or more initial failures should not prevent
        // a later successful authentication.
        if (request.getParameter("failFirst") != null) {
            try {
                request.authenticate(response);
            } catch (IOException | ServletException e) {
            }
        }
        
        if ("2".equals(request.getParameter("failFirst"))) {
            try {
                request.authenticate(response);
            } catch (IOException | ServletException e) {
            }
        }
        
        // Programmatically trigger the authentication chain
        request.setAttribute("doLogin", true);
        boolean authenticateOutcome = request.authenticate(response);
        
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }
        
        response.getWriter().write("request.authenticate outcome: " + authenticateOutcome + "\n");
        
        response.getWriter().write("after web username: " + webName + "\n");
        webHasRole = request.isUserInRole("architect");
        response.getWriter().write("after web user has role \"architect\": " + webHasRole + "\n");

    }

}

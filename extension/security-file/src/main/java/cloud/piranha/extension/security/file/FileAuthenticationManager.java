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
package cloud.piranha.extension.security.file;

import cloud.piranha.core.api.AuthenticationManager;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.webapp.impl.DefaultSecurityPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import static jakarta.servlet.http.HttpServletRequest.BASIC_AUTH;
import static jakarta.servlet.http.HttpServletRequest.CLIENT_CERT_AUTH;
import static jakarta.servlet.http.HttpServletRequest.DIGEST_AUTH;
import static jakarta.servlet.http.HttpServletRequest.FORM_AUTH;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * The file-based authentication manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileAuthenticationManager implements AuthenticationManager {

    /**
     * Stores the Base64 decoder.
     */
    private Decoder decoder = Base64.getDecoder();

    /**
     * Stores the logins.
     */
    private final HashMap<String, String> logins = new HashMap<>();
    
    /**
     * Stores the security mappings.
     */
    private final LinkedHashMap<String, String> securityMappings = new LinkedHashMap<>();

    /**
     * Stores the users file.
     */
    private File usersFile;

    /**
     * Constructor.
     *
     * @param usersFile the users file.
     */
    public FileAuthenticationManager(File usersFile) {
        this.usersFile = usersFile;
        Properties userProperties = new Properties();
        try ( FileInputStream fileInput = new FileInputStream(usersFile)) {
            userProperties.load(fileInput);
        } catch (IOException ioe) {
        }
        userProperties.entrySet().forEach(entry -> {
            String username = (String) entry.getKey();
            String password = (String) entry.getValue();
            logins.put(username, password);
        });
    }

    @Override
    public void addSecurityMapping(String urlPattern) {
        this.securityMappings.put(urlPattern, urlPattern);
    }

    @Override
    public boolean authenticate(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        if (request.getAuthType() != null) {
            switch (request.getAuthType()) {
                case BASIC_AUTH -> {
                    return authenticateBasic(request, response);
                }
                case CLIENT_CERT_AUTH -> {
                    return authenticateClientCert(request, response);
                }
                case DIGEST_AUTH -> {
                    return authenticateDigest(request, response);
                }
                case FORM_AUTH -> {
                    return authenticateForm(request, response);
                }
            }
        }
        throw new ServletException("AuthType '" + request.getAuthType() + "' is not supported");
    }

    /**
     * Authenticate using BASIC auth.
     *
     * @param request the request,
     * @param response the response.
     */
    private boolean authenticateBasic(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        boolean result = false;
        if (request.getHeader("Authorization") != null) {
            String decodedString = new String(decoder.decode(
                    request.getHeader("Authorization").substring("Basic ".length())));
            String username = decodedString.substring(0, decodedString.indexOf(':'));
            String password = decodedString.substring(decodedString.indexOf(':') + 1);
            login(request, username, password);
            if (request.getUserPrincipal() != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Authenticate using CLIENT_CERT auth.
     *
     * @param request the request,
     * @param response the response.
     */
    private boolean authenticateClientCert(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        throw new ServletException("AuthType '" + request.getAuthType() + "' is not supported yet");
    }

    /**
     * Authenticate using DIGEST auth.
     *
     * @param request the request,
     * @param response the response.
     */
    private boolean authenticateDigest(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        throw new ServletException("AuthType '" + request.getAuthType() + "' is not supported yet");
    }

    /**
     * Authenticate using FORM based.
     *
     * @param request the request,
     * @param response the response.
     */
    private boolean authenticateForm(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        boolean result = false;
        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");
        login(request, username, password);
        if (request.getUserPrincipal() != null) {
            result = true;
        }
        return result;
    }

    @Override
    public void login(HttpServletRequest request, String username,
            String password) throws ServletException {
        if (logins.containsKey(username) && password != null && password.equals(logins.get(username))) {
            while (request instanceof HttpServletRequestWrapper wrapper) {
                request = (HttpServletRequest) wrapper.getRequest();
            }
            if (request instanceof WebApplicationRequest webAppRequest) {
                webAppRequest.setUserPrincipal(new DefaultSecurityPrincipal(username));
            }
        } else {
            throw new ServletException("Unable to login using the given username and/or password");
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        while (request instanceof HttpServletRequestWrapper wrapper) {
            request = (HttpServletRequest) wrapper.getRequest();
        }
        if (request instanceof WebApplicationRequest webAppRequest) {
            webAppRequest.setUserPrincipal(null);
        }
    }

    @Override
    public boolean needsAuthentication(HttpServletRequest request) {
        boolean result = false;
        String requestUri = request.getRequestURI();
        for(String securityMapping : securityMappings.keySet()) {
            if (securityMapping.endsWith("*")) {
                securityMapping = securityMapping.substring(0, securityMapping.length() - 1);
                if (requestUri.indexOf(securityMapping) == 0) {
                    result = true;
                    break;
                }
            } else if (securityMapping.startsWith("*")) {
                
            } else {
                
            }
        }
        return result;
    }
    
    @Override
    public void requestAuthentication(HttpServletRequest request, HttpServletResponse response) {
    }
}

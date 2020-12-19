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
package cloud.piranha.servlet4.impl;

/**
 * The web.xml login-config.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlLoginConfig {

    /**
     * Stores the auth method.
     */
    private final String authMethod;

    /**
     * Stores the realm name.
     */
    private final String realmName;

    /**
     * Stores the form login page.
     */
    private final String formLoginPage;

    /**
     * Stores the form error page.
     */
    private final String formErrorPage;

    /**
     * Constructor.
     *
     * @param authMethod the auth method.
     * @param realmName the realm name.
     * @param formLoginPage the form login page.
     * @param formErrorPage the form error page.
     */
    public WebXmlLoginConfig(String authMethod, String realmName, String formLoginPage, String formErrorPage) {
        this.authMethod = authMethod;
        this.realmName = realmName;
        this.formLoginPage = formLoginPage;
        this.formErrorPage = formErrorPage;
    }

    /**
     * Get the auth method.
     *
     * @return the auth method.
     */
    public String getAuthMethod() {
        return authMethod;
    }

    /**
     * Get the realm name.
     *
     * @return the realm name.
     */
    public String getRealmName() {
        return realmName;
    }

    /**
     * Get the form login page.
     *
     * @return the form login page.
     */
    public String getFormLoginPage() {
        return formLoginPage;
    }

    /**
     * Get the form error page.
     *
     * @return the form error page.
     */
    public String getFormErrorPage() {
        return formErrorPage;
    }
}

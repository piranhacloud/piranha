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
package cloud.piranha.micro.core;

import static java.util.Collections.emptySet;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 * A basic in-memory identity store.
 * 
 * <p>
 * This identity store functions as the default identity store for among others
 * Servlet security.
 * 
 * @author Arjan Tijms
 *
 */
@ApplicationScoped
public class InMemoryIdentityStore implements IdentityStore {
    
    public static class Credential {
        
        /**
         * Stores the caller name.
         */
        private final String callerName;
        
        /**
         * Stores the password.
         */
        private final String password;
        
        /**
         * Stores the groups.
         */
        private final List<String> groups;
        
        /**
         * Constructor.
         * 
         * @param callerName the caller name.
         * @param password the password.
         * @param groups the groups.
         */
        public Credential(String callerName, String password, List<String> groups) {
            super();
            this.callerName = callerName;
            this.password = password;
            this.groups = groups;
        }
        
        /**
         * Get the caller name.
         * 
         * @return the caller name.
         */
        public String getCallerName() {
            return callerName;
        }

        /**
         * Get the password.
         * 
         * @return the password.
         */
        public String getPassword() {
            return password;
        }

        /**
         * Get the groups.
         * 
         * @return the groups.
         */
        public List<String> getGroups() {
            return groups;
        }
    }

    /**
     * Stores the caller to credentials map.
     */
    private static final Map<String, Credential> CALLER_TO_CREDENTIALS = new ConcurrentHashMap<>();

    /**
     * Get the caller to credentials map.
     * 
     * @return the caller to credentials map.
     */
    public static Map<String, Credential> getCALLER_TO_CREDENTIALS() {
        return CALLER_TO_CREDENTIALS;
    }

    /**
     * Add the credential.
     * 
     * @param callerName the caller name.
     * @param password the password.
     * @param groups the groups.
     */
    public static void addCredential(String callerName, String password, List<String> groups) {
        addCredential(new Credential(callerName, password, groups));
    }
    
    /**
     * Add the credential.
     * 
     * @param credential the credential. 
     */
    public static void addCredential(Credential credential) {
        CALLER_TO_CREDENTIALS.put(credential.getCallerName(), credential);
    }
    
    /**
     * Validate the username password credential.
     * 
     * @param usernamePasswordCredential the username password credential.
     * @return the credential validation result.
     */
    public CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {
        Credential credential = CALLER_TO_CREDENTIALS.get(usernamePasswordCredential.getCaller());

        if (credential != null && usernamePasswordCredential.getPassword().compareTo(credential.getPassword())) {
            return new CredentialValidationResult(
                new CallerPrincipal(credential.getCallerName()), 
                new HashSet<>(credential.getGroups())
            );
        }

        return INVALID_RESULT;
    }
    
    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        Credential credentials = CALLER_TO_CREDENTIALS.get(validationResult.getCallerPrincipal().getName());

        return credentials != null ? new HashSet<>(credentials.getGroups()) : emptySet();
    }

}

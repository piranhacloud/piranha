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
package cloud.piranha.micro;

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
 * @author Arjan Tijms
 *
 */
@ApplicationScoped
public class InMemmoryIdentityStore implements IdentityStore {
    
    public static class Credential {
        
        private final String callerName;
        private final String password;
        private final List<String> groups;
        
        public Credential(String callerName, String password, List<String> groups) {
            super();
            this.callerName = callerName;
            this.password = password;
            this.groups = groups;
        }
        
        public String getCallerName() {
            return callerName;
        }

        public String getPassword() {
            return password;
        }

        public List<String> getGroups() {
            return groups;
        }
    }

    private static final Map<String, Credential> CALLER_TO_CREDENTIALS = new ConcurrentHashMap<>();

    public static Map<String, Credential> getCALLER_TO_CREDENTIALS() {
        return CALLER_TO_CREDENTIALS;
    }

    public static void addCredential(String callerName, String password, List<String> groups) {
        addCredential(new Credential(callerName, password, groups));
    }
    
    public static void addCredential(Credential credentials) {
        CALLER_TO_CREDENTIALS.put(credentials.getCallerName(), credentials);
    }
    
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

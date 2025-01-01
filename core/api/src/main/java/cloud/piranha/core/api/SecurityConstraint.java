/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A security constraint.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SecurityConstraint {

    /**
     * Stores the role names.
     */
    private List<String> roleNames;
    
    /**
     * Stores the security web resource collections.
     */
    private List<SecurityWebResourceCollection> securityWebResourceCollections;
    
    /**
     * Stores the transport guarantee.
     */
    private String transportGuarantee;

    /**
     * Constructor.
     */
    public SecurityConstraint() {
        roleNames = new ArrayList<>();
        securityWebResourceCollections = new ArrayList<>();
        transportGuarantee = "NONE";
    }
    
    /**
     * Get the role names.
     * 
     * @return the role names.
     */
    public List<String> getRoleNames() {
        return roleNames;
    }

    /**
     * Get the security web resource collection.
     * 
     * @return the security web resource collection.
     */
    public List<SecurityWebResourceCollection> getSecurityWebResourceCollections() {
        return securityWebResourceCollections;
    }

    /**
     * Get the transport guarantee.
     * 
     * @return the transport guarantee.
     */
    public String getTransportGuarantee() {
        return transportGuarantee;
    }

    /**
     * Set the role names.
     * 
     * @param roleNames the role names.
     */
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    /**
     * Set the security web resource collections.
     * 
     * @param securityWebResourceCollections the security web resource collections.
     */
    public void setSecurityWebResourceCollections(
            List<SecurityWebResourceCollection> securityWebResourceCollections) {
        this.securityWebResourceCollections = securityWebResourceCollections;
    }

    /**
     * Set the transport guarantee.
     * 
     * @param transportGuarantee the transport guarantee.
     */
    public void setTransportGuarantee(String transportGuarantee) {
        this.transportGuarantee = transportGuarantee;
    }
}

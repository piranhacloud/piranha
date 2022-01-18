/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package jakarta.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;

/**
 * The ServletSecurityElement API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletSecurityElement extends HttpConstraintElement {

    /**
     * Stores the method constraints.
     */
    private final Collection<HttpMethodConstraintElement> methodConstraints;

    /**
     * Stores the method names.
     */
    private final Collection<String> methodNames;

    /**
     * Constructor.
     */
    public ServletSecurityElement() {
        super();
        methodConstraints = new HashSet<>();
        methodNames = Collections.emptySet();
    }

    /**
     * Constructor.
     *
     * @param constraint the constraint.
     */
    public ServletSecurityElement(HttpConstraintElement constraint) {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        methodConstraints = new HashSet<>();
        methodNames = Collections.emptySet();
    }

    /**
     * Constructor.
     *
     * @param methodConstraints the method constraints.
     */
    public ServletSecurityElement(Collection<HttpMethodConstraintElement> methodConstraints) {
        this.methodConstraints = methodConstraints;
        this.methodNames = collectMethodNames(this.methodConstraints);
    }

    /**
     * Constructor.
     *
     * @param constraint the constraint.
     * @param methodConstraints the method constraints.
     */
    public ServletSecurityElement(HttpConstraintElement constraint, Collection<HttpMethodConstraintElement> methodConstraints) {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        this.methodConstraints = methodConstraints;
        methodNames = collectMethodNames(this.methodConstraints);
    }

    /**
     * Constructor.
     *
     * @param annotation the annotation.
     */
    public ServletSecurityElement(ServletSecurity annotation) {
        super(annotation.value().value(), annotation.value().transportGuarantee(), annotation.value().rolesAllowed());
        this.methodConstraints = new HashSet<>();
        for (HttpMethodConstraint constraint : annotation.httpMethodConstraints()) {
            this.methodConstraints.add(new HttpMethodConstraintElement(constraint.value(),
                    new HttpConstraintElement(constraint.emptyRoleSemantic(),
                            constraint.transportGuarantee(),
                            constraint.rolesAllowed())));
        }
        methodNames = collectMethodNames(methodConstraints);
    }

    /**
     * Collect the method names.
     *
     * @param methodConstraints the method constraints.
     * @return the method names.
     */
    private Collection<String> collectMethodNames(Collection<HttpMethodConstraintElement> methodConstraints) {
        Collection<String> result = new HashSet<>();
        for (HttpMethodConstraintElement methodConstraint : methodConstraints) {
            String methodName = methodConstraint.getMethodName();
            if (!result.add(methodName)) {
                throw new IllegalArgumentException("Duplicate HTTP method name: " + methodName);
            }
        }
        return result;
    }

    /**
     * {@return the HTTP method constraints}
     */
    public Collection<HttpMethodConstraintElement> getHttpMethodConstraints() {
        return Collections.unmodifiableCollection(methodConstraints);
    }

    /**
     * {@return the method names}
     */
    public Collection<String> getMethodNames() {
        return Collections.unmodifiableCollection(methodNames);
    }
}

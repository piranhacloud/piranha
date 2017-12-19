/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;

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
     * Get the HTTP method constraints.
     *
     * @return the HTTP method constraints.
     */
    public Collection<HttpMethodConstraintElement> getHttpMethodConstraints() {
        return Collections.unmodifiableCollection(methodConstraints);
    }

    /**
     * Get the method names.
     *
     * @return the method names.
     */
    public Collection<String> getMethodNames() {
        return Collections.unmodifiableCollection(methodNames);
    }
}

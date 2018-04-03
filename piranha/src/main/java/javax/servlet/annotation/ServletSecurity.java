/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ServletSecurity API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServletSecurity {

    /**
     * Get the HTTP method constraints.
     *
     * @return the HTTP method constraints.
     */
    HttpMethodConstraint[] httpMethodConstraints() default {};

    /**
     * Get the HTTP constraint.
     *
     * @return the HTTP constraint.
     */
    HttpConstraint value() default @HttpConstraint;

    /**
     * The EmptyRoleSemantic API.
     */
    enum EmptyRoleSemantic {
        /**
         * Permit access for EmptyRole.
         */
        PERMIT,
        /**
         * Deny access for EmptyRole.
         */
        DENY
    }

    /**
     * The TransportGuarantee API.
     */
    enum TransportGuarantee {
        /**
         * Transport is NOT encrypted.
         */
        NONE,
        /**
         * Transport is encrypted.
         */
        CONFIDENTIAL
    }
}

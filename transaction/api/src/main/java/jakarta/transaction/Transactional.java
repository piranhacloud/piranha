/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jakarta.transaction;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import jakarta.interceptor.InterceptorBinding;
import static jakarta.transaction.Transactional.TxType.REQUIRED;

/**
 * The Transactional annotation.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Inherited
@InterceptorBinding
@Target(value = {TYPE, METHOD})
@Retention(value = RUNTIME)
public @interface Transactional {

    /**
     * Don't rollback on the given exceptions.
     *
     * @return the exception classes.
     */
    Class[] dontRollbackOn() default {};

    /**
     * Rollback on the given exceptions.
     *
     * @return the exception classes.
     */
    Class[] rollbackOn() default {};

    /**
     * Get the transactional type.
     *
     * @return transaction type.
     */
    TxType value() default REQUIRED;

    /**
     * Defines the Transaction type.
     */
    public static enum TxType {
        /**
         * A transaction is mandatory.
         */
        MANDATORY,
        /**
         * A transaction should not exist.
         */
        NEVER,
        /**
         * A transaction is not supported.
         */
        NOT_SUPPORTED,
        /**
         * A transaction is required.
         */
        REQUIRED,
        /**
         * A new transaction is required.
         */
        REQUIRES_NEW,
        /**
         * A transaction can be supported.
         */
        SUPPORTS
    }
}

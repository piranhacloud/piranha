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
package jakarta.servlet;

import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.HttpConstraintElement;
import jakarta.servlet.HttpMethodConstraintElement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;

import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ServletSecurityElement class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ServletSecurityElementTest {

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        HttpConstraintElement httpConstraintElement = new HttpConstraintElement(ServletSecurity.EmptyRoleSemantic.PERMIT);
        ServletSecurityElement element = new ServletSecurityElement(httpConstraintElement);
        assertEquals(ServletSecurity.EmptyRoleSemantic.PERMIT, element.getEmptyRoleSemantic());
        assertNotNull(element.getHttpMethodConstraints());
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor2() {
        HttpConstraintElement httpConstraintElement = new HttpConstraintElement(ServletSecurity.EmptyRoleSemantic.PERMIT);
        ArrayList<HttpMethodConstraintElement> methodConstraints = new ArrayList<>();
        methodConstraints.add(new HttpMethodConstraintElement("HEAD"));
        ServletSecurityElement element = new ServletSecurityElement(httpConstraintElement, methodConstraints);
        assertEquals(ServletSecurity.EmptyRoleSemantic.PERMIT, element.getEmptyRoleSemantic());
        assertNotNull(element.getHttpMethodConstraints());
        assertEquals("HEAD", element.getHttpMethodConstraints().iterator().next().getMethodName());
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor3() {
        HttpConstraintElement httpConstraintElement = new HttpConstraintElement(ServletSecurity.EmptyRoleSemantic.PERMIT);
        ArrayList<HttpMethodConstraintElement> methodConstraints = new ArrayList<>();
        methodConstraints.add(new HttpMethodConstraintElement("HEAD"));
        methodConstraints.add(new HttpMethodConstraintElement("HEAD"));
        assertThrows(IllegalArgumentException.class, () -> new ServletSecurityElement(httpConstraintElement, methodConstraints));
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor4() {
        ServletSecurity servletSecurity = new ServletSecurity() {
            @Override
            public
            HttpMethodConstraint[] httpMethodConstraints() {
                return new HttpMethodConstraint[]{
                    new HttpMethodConstraint() {
                        @Override
                        public ServletSecurity.EmptyRoleSemantic emptyRoleSemantic() {
                            return ServletSecurity.EmptyRoleSemantic.PERMIT;
                        }

                        @Override
                        public String[] rolesAllowed() {
                            return new String[]{};
                        }

                        @Override
                        public ServletSecurity.TransportGuarantee transportGuarantee() {
                            return ServletSecurity.TransportGuarantee.NONE;
                        }

                        @Override
                        public String value() {
                            return "HEAD";
                        }

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                    }
                };
            }

            @Override
            public HttpConstraint value() {
                return new HttpConstraint() {
                    @Override
                    public String[] rolesAllowed() {
                        return new String[]{};
                    }

                    @Override
                    public ServletSecurity.TransportGuarantee transportGuarantee() {
                        return ServletSecurity.TransportGuarantee.NONE;
                    }

                    @Override
                    public ServletSecurity.EmptyRoleSemantic value() {
                        return ServletSecurity.EmptyRoleSemantic.PERMIT;
                    }

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ServletSecurityElement servletSecurityElement = new ServletSecurityElement(servletSecurity);
        assertNotNull(servletSecurityElement);
    }

    /**
     * Test getHttpMethodConstraints method.
     */
    @Test
    void testGetHttpMethodConstraints() {
        ServletSecurityElement element = new ServletSecurityElement();
        assertNotNull(element.getHttpMethodConstraints());
        element = new ServletSecurityElement(new ArrayList<>());
        assertNotNull(element.getHttpMethodConstraints());
    }

    /**
     * Test getMethodNames method.
     */
    @Test
    void testGetMethodNames() {
        ServletSecurityElement element = new ServletSecurityElement();
        assertNotNull(element.getMethodNames());
    }
}

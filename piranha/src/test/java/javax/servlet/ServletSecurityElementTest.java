/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for the ServletSecurityElement class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletSecurityElementTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        HttpConstraintElement httpConstraintElement = new HttpConstraintElement(ServletSecurity.EmptyRoleSemantic.PERMIT);
        ServletSecurityElement element = new ServletSecurityElement(httpConstraintElement);
        assertEquals(ServletSecurity.EmptyRoleSemantic.PERMIT, element.getEmptyRoleSemantic());
        assertNotNull(element.getHttpMethodConstraints());
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor2() {
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
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() {
        HttpConstraintElement httpConstraintElement = new HttpConstraintElement(ServletSecurity.EmptyRoleSemantic.PERMIT);
        ArrayList<HttpMethodConstraintElement> methodConstraints = new ArrayList<>();
        methodConstraints.add(new HttpMethodConstraintElement("HEAD"));
        methodConstraints.add(new HttpMethodConstraintElement("HEAD"));
        ServletSecurityElement servletSecurityElement = new ServletSecurityElement(httpConstraintElement, methodConstraints);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor4() {
        ServletSecurity servletSecurity = new ServletSecurity() {
            @Override
            public HttpMethodConstraint[] httpMethodConstraints() {
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
    public void testGetHttpMethodConstraints() {
        ServletSecurityElement element = new ServletSecurityElement();
        assertNotNull(element.getHttpMethodConstraints());
        element = new ServletSecurityElement(new ArrayList<>());
        assertNotNull(element.getHttpMethodConstraints());
    }

    /**
     * Test getMethodNames method.
     */
    @Test
    public void testGetMethodNames() {
        ServletSecurityElement element = new ServletSecurityElement();
        assertNotNull(element.getMethodNames());
    }
}

package com.manorrock.piranha.test.authentication.eleos.wrapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * @author Arjan Tijms
 * 
 */
public class TestHttpServletResponseWrapper extends HttpServletResponseWrapper {

    public TestHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public String getHeader(String name) {

        if ("isWrapped".equals(name)) {
            return "true";
        }

        return super.getHeader(name);
    }

}

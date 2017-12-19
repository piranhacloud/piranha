/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.springmvc;

import com.manorrock.webapp.DefaultHttpServletRequest;

/**
 * A test HTTP servlet request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServletRequest extends DefaultHttpServletRequest {

    /**
     * Constructor.
     */
    public TestHttpServletRequest() {
        super();
        inputStream = new TestServletInputStream(new byte[0], this);
    }
}

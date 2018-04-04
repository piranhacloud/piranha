/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.weld;

import com.manorrock.piranha.DefaultHttpServletRequest;

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

/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.ServletOutputStream;

/**
 * The default ServletOutputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class DefaultServletOutputStream extends ServletOutputStream {

    /**
     * Set the response.
     *
     * @param response the response.
     */
    public abstract void setResponse(DefaultHttpServletResponse response);
}

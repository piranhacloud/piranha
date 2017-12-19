/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.jersey;

import com.manorrock.webapp.DefaultHttpServletResponse;
import java.util.List;
import javax.servlet.http.Cookie;

/**
 * A test HTTP servlet response.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServletResponse extends DefaultHttpServletResponse {

    /**
     * Constructor.
     */
    public TestHttpServletResponse() {
        super();
        this.outputStream = new TestServletOutputStream();
    }

    /**
     * Get the cookies.
     *
     * @return the cookies.
     */
    public List<Cookie> getCookies() {
        return cookies;
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return 0;
    }

    /**
     * Get the bytes in the buffer.
     *
     * @return the bytes in the buffer.
     */
    public byte[] getResponseBody() {
        if (this.gotWriter) {
            this.writer.flush();
        }
        TestServletOutputStream output = (TestServletOutputStream) this.outputStream;
        return output.getBytes();
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
        verifyNotCommitted("resetBuffer");
        TestServletOutputStream output = (TestServletOutputStream) this.outputStream;
        output.reset();
    }

    /**
     * Set the buffer size.
     *
     * @param size the buffer size.
     */
    @Override
    public void setBufferSize(int size) {
    }
}

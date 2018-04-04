/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DefaultHttpRequestParserTest {

    /**
     * Test parse method.
     */
    @Test
    public void testParse() {
        DefaultHttpRequestParser parser = new DefaultHttpRequestParser();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setInputStream(new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener rl) {
            }

            @Override
            public int read() throws IOException {
                throw new IOException("I/O error");
            }
        });
        parser.parse(request);
    }

    /**
     * Test parse method.
     */
    @Test
    public void testParse2() {
        DefaultHttpRequestParser parser = new DefaultHttpRequestParser();
        TestHttpServletRequest request = new TestHttpServletRequest();
        String requestString = "GET /index.html HTTP/1.1\nContent-Length: 12\n\n";
        byte[] buffer = requestString.getBytes();
        request.setInputStream(new TestServletInputStream(buffer, request));
        parser.parse(request);
        assertTrue(request.getRequestURL().toString().contains("index.html"));
    }
}

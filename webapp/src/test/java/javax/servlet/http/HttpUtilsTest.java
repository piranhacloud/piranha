/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * The JUnit tests for the HttpUtils class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpUtilsTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        HttpUtils utils = new HttpUtils();
        assertNotNull(utils);
    }

    /**
     * Test getRequestURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestURL() {
        HttpUtils.getRequestURL(null);
    }

    /**
     * Test parsePostData method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testParsePostData() {
        HttpUtils.parsePostData(0, null);
    }

    /**
     * Test parseQueryString method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testParseQueryString() {
        HttpUtils.parseQueryString(null);
    }
}

/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.Hashtable;
import javax.servlet.ServletInputStream;

/**
 * The HttpUtils API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @deprecated
 */
public class HttpUtils {

    /**
     * Constructor.
     */
    public HttpUtils() {
    }

    /**
     * Get the request URL.
     *
     * @param request the request.
     * @return the request URL.
     */
    public static StringBuffer getRequestURL(HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Parse the POST data.
     *
     * @param length the length.
     * @param inputStream the input stream.
     * @return the hash table populated with key/value pairs.
     */
    public static Hashtable<String, String[]> parsePostData(int length, ServletInputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    /**
     * Parse the query string.
     *
     * @param queryString the query string.
     * @return the hash table populated with key/value pairs.
     */
    public static Hashtable<String, String[]> parseQueryString(String queryString) {
        throw new UnsupportedOperationException();
    }
}

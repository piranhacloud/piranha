/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.classloader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * The Test 1 Servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@WebServlet(name = "Test1Servlet", urlPatterns = {"/Test1Servlet"})
public class Test1Servlet extends HttpServlet {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 5841417173572330850L;
}

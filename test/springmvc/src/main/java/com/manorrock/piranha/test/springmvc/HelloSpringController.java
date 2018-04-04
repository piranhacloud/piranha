/*
 * Copyright (c) 2002-2011 Manorrock.com. All Rights Reserved. 
 */
package com.manorrock.piranha.test.springmvc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * A simple Spring controller.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloSpringController implements Controller {

    /**
     * Handle the request.
     * 
     * @param request the request.
     * @param response the response.
     * @return the model and view.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        return new ModelAndView("/index.jsp");
    }
}

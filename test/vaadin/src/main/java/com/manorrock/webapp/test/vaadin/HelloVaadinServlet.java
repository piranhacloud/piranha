/*
 * Copyright (c) 2002-2017 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.vaadin;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import javax.servlet.annotation.WebServlet;

/**
 * The HelloVaadin servlet.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = HelloVaadin.class)
public class HelloVaadinServlet extends VaadinServlet {
}

/*
 * Copyright (c) 2002-2017 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The HelloVaadin UI.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@SuppressWarnings("serial")
public class HelloVaadin extends UI {

    /**
     * Init the UI.
     * 
     * @param request the request.
     */
    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        layout.addComponent(new Label("Hello Vaadin"));
    }
}

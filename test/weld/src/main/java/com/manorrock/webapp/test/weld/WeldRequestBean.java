/*
 * Copyright (c) 2002-2017 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.weld;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * A simple request bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named(value = "requestBean")
@RequestScoped
public class WeldRequestBean {

    /**
     * Get the "Hello Weld".
     *
     * @return "Hello Weld"
     */
    public String getHello() {
        return "Hello Weld";
    }
}

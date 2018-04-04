/*
 * Copyright (c) 2002-2011 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.struts;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloWorldAction extends ActionSupport {
    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Stores the message store.
     */
    private MessageStore messageStore;

    /**
     * Execute the action.
     * 
     * @return SUCCESS.
     * @throws Exception when an error occurs.
     */
    @Override
    public String execute() throws Exception {
        messageStore = new MessageStore();
        return SUCCESS;
    }
}

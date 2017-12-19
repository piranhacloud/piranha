/*
 * Copyright (c) 2002-2011 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.struts;

/**
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MessageStore {
    /**
     * Stores the message.
     */
    private String message;

    /**
     * Constructor.
     */
    public MessageStore() {
        setMessage("Hello World Struts2");
    }
    
    /**
     * Set the message.
     * 
     * @param message the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

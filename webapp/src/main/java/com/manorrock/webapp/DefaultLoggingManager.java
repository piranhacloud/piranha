/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

/**
 * The default LoggingManager.
 *
 * <p>
 * The default LoggingManager swallows everything. If you want to do some actual
 * logging you need to deliver your custom LoggingManager.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultLoggingManager implements LoggingManager {

    /**
     * Constructor.
     */
    public DefaultLoggingManager() {
    }

    /**
     * Log the message.
     *
     * @param message
     * @param throwable
     */
    @Override
    public void log(String message, Throwable throwable) {
    }
}

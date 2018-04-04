/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

/**
 * The LoggingManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface LoggingManager {

    /**
     * Log the message.
     *
     * @param message the message.
     * @param throwable the throwable.
     */
    public void log(String message, Throwable throwable);
}

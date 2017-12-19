/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

/**
 * The default MimeTypeManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultMimeTypeManager implements MimeTypeManager {

    /**
     * Get the mime type.
     *
     * @param filename the filename.
     * @return the mime type, or null if not found.
     */
    @Override
    public String getMimeType(String filename) {
        String result = null;
        if (filename != null) {
            if (filename.endsWith(".css")) {
                result = "text/css";
            } else if (filename.endsWith(".js")) {
                result = "text/javascript";
            }
        }
        return result;
    }
}

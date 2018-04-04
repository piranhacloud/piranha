/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

/**
 * The MimeTypeManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface MimeTypeManager {

    /**
     * Get the mime type.
     *
     * @param filename the filename.
     * @return the mime type or null if not found.
     */
    String getMimeType(String filename);
}

/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha.jndi;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * The default NamingEnumeration.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultNamingEnumeration implements NamingEnumeration<NameClassPair> {

    /**
     * Defines the "Not supported yet." constant.
     */
    private static final String NOT_SUPPORTED_YET = "Not supported yet.";

    @Override
    public NameClassPair next() throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public boolean hasMore() throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void close() throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public boolean hasMoreElements() {
        return false;
    }

    @Override
    public NameClassPair nextElement() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }
}

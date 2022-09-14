/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.naming.impl;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * The default Binding NamingEnumeration.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultBindingNamingEnumeration implements NamingEnumeration<Binding> {

    /**
     * Stores the list of bindings.
     */
    private List<Binding> list;

    /**
     * Stores the iterator.
     */
    private Iterator<Binding> iterator;

    /**
     * Constructor.
     *
     * @param list the list of bindings.
     */
    public DefaultBindingNamingEnumeration(List<Binding> list) {
        this.list = list;
        this.iterator = list.iterator();
    }

    /**
     * Get the next binding.
     * 
     * @return the next binding.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Binding next() throws NamingException {
        Binding result;
        checkClosed();
        if (iterator.hasNext()) {
            result = iterator.next();
        } else {
            throw new NoSuchElementException("Next binding was not found");
        }
        return result;
    }

    /**
     * Does the enumeration have more elements.
     *
     * @return true if it does, false otherwise.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public boolean hasMore() throws NamingException {
        checkClosed();
        return iterator.hasNext();
    }

    /**
     * Close the enumeration for use.
     *
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void close() throws NamingException {
        checkClosed();
        list = null;
        iterator = null;
    }

    /**
     * Does the enumeration have more elements.
     *
     * @return true if it does, false otherwise.
     */
    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     * Get the next element.
     * 
     * @return the next element.
     */
    @Override
    public Binding nextElement() {
        return iterator.next();
    }

    /**
     * Check if the enumeration has been closed.
     *
     * @throws NamingException when a naming error occurs.
     */
    private void checkClosed() throws NamingException {
        if (list == null) {
            throw new NamingException("Cannot call any method on a closed NamingEnumeration");
        }
    }
}

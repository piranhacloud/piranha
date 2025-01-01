/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributeManagerTest {

    private AttributeManager attributeManager;
    private Hashtable<String, Object> attributes;

    @BeforeEach
    void setUp() {
        attributes = new Hashtable<>();
        attributeManager = new AttributeManager() {
            @Override
            public Object getAttribute(String name) {
                return attributes.get(name);
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return attributes.keys();
            }

            @Override
            public void removeAttribute(String name) {
                attributes.remove(name);
            }

            @Override
            public void setAttribute(String name, Object value) {
                attributes.put(name, value);
            }
        };
    }

    @Test
    void testGetAttribute() {
        attributeManager.setAttribute("test", "value");
        assertEquals("value", attributeManager.getAttribute("test"));
    }

    @Test
    void testGetAttributeNames() {
        attributeManager.setAttribute("test1", "value1");
        attributeManager.setAttribute("test2", "value2");
        Enumeration<String> names = attributeManager.getAttributeNames();
        assertTrue(Collections.list(names).contains("test1"));
        names = attributeManager.getAttributeNames();
        assertTrue(Collections.list(names).contains("test2"));
    }

    @Test
    void testRemoveAttribute() {
        attributeManager.setAttribute("test", "value");
        attributeManager.removeAttribute("test");
        assertNull(attributeManager.getAttribute("test"));
    }

    @Test
    void testSetAttribute() {
        attributeManager.setAttribute("test", "value");
        assertEquals("value", attributeManager.getAttribute("test"));
    }

    @Test
    void testContainsAttribute() {
        attributeManager.setAttribute("test", "value");
        assertTrue(attributeManager.containsAttribute("test"));
        attributeManager.removeAttribute("test");
        assertFalse(attributeManager.containsAttribute("test"));
    }
}
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
package cloud.piranha.naming.impl.tests;

import cloud.piranha.naming.impl.DefaultInitialContext;
import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the SparrowInitialContext class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultInitialContextTest {

    /**
     * Test addToEnvironment method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testAddToEnvironment() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.addToEnvironment("test", "test");
        assertEquals("test", context.removeFromEnvironment("test"));
        assertNull(context.removeFromEnvironment("test"));
    }

    /**
     * Test bind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testBind() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("name");
        context.bind(name, "value");
        assertNotNull(context.lookup(name));
    }

    /**
     * Test bind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testBind2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.createSubcontext("context");
        context.bind("context/name1", "value1");
        assertNotNull(context.lookup("context/name1"));
        assertEquals("value1", context.lookup("context/name1"));
        context.bind("context/name2", "value2");
        assertNotNull(context.lookup("context/name2"));
        assertEquals("value2", context.lookup("context/name2"));
    }

    /**
     * Test bind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testBind3() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("name");
        context.bind(name, "value");
        assertNotNull(context.lookup(name));
        assertThrows(NameAlreadyBoundException.class, () -> context.bind(name, "value"));
    }

    /**
     * Test close method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testClose() {
        try {
            DefaultInitialContext context = new DefaultInitialContext();
            context.close();
        } catch (NamingException ex) {
            fail();
        }
    }

    /**
     * Test rename method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testClose2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", "value");
        context.close();
        assertThrows(NamingException.class, () -> context.rename("rename", "name"));
    }

    /**
     * Test createSubcontext method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testCreateSubcontext() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.createSubcontext(new CompositeName("context"));
        context.bind("context/name1", "value1");
        assertNotNull(context.lookup("context/name1"));
    }

    /**
     * Test composeName method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testComposeName() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertNotNull(context.composeName("name", ""));
    }

    /**
     * Test composeName method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testComposeName2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertThrows(NamingException.class, () -> context.composeName("name", "kaboom"));
    }

    /**
     * Test composeName method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testComposeName3() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertThrows(NamingException.class, () -> context.composeName("name", null));
    }

    /**
     * Test composeName method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testComposeName4() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertNotNull(context.composeName(new CompositeName("name"), new CompositeName("")));
    }

    /**
     * Test destroySubcontext method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testDestroySubcontext() {
        try {
            DefaultInitialContext context = new DefaultInitialContext();
            context.createSubcontext(new CompositeName("context"));
            context.destroySubcontext("context");
        } catch (NamingException ex) {
            fail();
        }
    }

    /**
     * Test destroySubcontext method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testDestroySubcontext2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.createSubcontext(new CompositeName("context"));
        context.bind("context/name", 12);
        assertThrows(ContextNotEmptyException.class, () -> context.destroySubcontext("context"));
    }

    /**
     * Test destroySubcontext method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testDestroySubcontext3() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", 12);
        assertThrows(NotContextException.class, () -> context.destroySubcontext("name"));
    }

    /**
     * Test destroySubcontext method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testDestroySubcontext4() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertThrows(NameNotFoundException.class, () -> context.destroySubcontext("name"));
    }

    /**
     * Test destroySubcontext method.
     */
    @Test
    void testDestroySubcontext5() {
        try {
            DefaultInitialContext context = new DefaultInitialContext();
            context.bind("context1/context2/name", 12);
            context.unbind("context1/context2/name");
            context.destroySubcontext(new CompositeName("context1/context2"));
        } catch (NamingException ex) {
            fail();
        }
    }

    /**
     * Test destroySubcontext method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testDestroySubcontext6() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("context1/name", 12);
        context.unbind("context1/name");
        assertThrows(NameNotFoundException.class, () -> context.destroySubcontext(new CompositeName("context1/context2")));
    }

    /**
     * Test getEnvironment method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetEnvironment() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertNotNull(context.getEnvironment());
    }

    /**
     * Test getNameInNamespace method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testGetNameInNamespace() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertThrows(OperationNotSupportedException.class, () -> context.getNameInNamespace());
    }

    /**
     * Test getNameParser method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetNameParser() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertNotNull(context.getNameParser(new CompositeName()));
    }

    /**
     * Test getNameParser method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetNameParser2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertNotNull(context.getNameParser(""));
    }

    /**
     * Test lookup method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testLookup() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("name");
        assertThrows(NameNotFoundException.class, () -> context.lookup(name));
    }

    /**
     * Test list method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testList() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("name");
        NamingEnumeration<NameClassPair> enumeration = context.list(name);
        assertNotNull(enumeration);
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.createSubcontext("name");
        NamingEnumeration<Binding> enumeration = context.listBindings("name");
        assertNotNull(enumeration);
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Context subContext = context.createSubcontext("subcontext");
        subContext.bind("name", "value");
        NamingEnumeration<Binding> enumeration = context.listBindings("subcontext");
        assertNotNull(enumeration);
        assertTrue(enumeration.hasMore());
        Binding binding = enumeration.next();
        assertEquals("name", binding.getName());
        assertThrows(NoSuchElementException.class, enumeration::next);
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings3() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        NamingEnumeration<Binding> enumeration = context.listBindings("name");
        assertNotNull(enumeration);
        enumeration.close();
        assertThrows(NamingException.class, enumeration::hasMore);
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings4() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Context subContext = context.createSubcontext("subcontext");
        subContext.bind("name", "value");
        NamingEnumeration<Binding> enumeration = context.listBindings("subcontext");
        assertNotNull(enumeration);
        assertTrue(enumeration.hasMoreElements());
        Binding binding = enumeration.nextElement();
        assertEquals("name", binding.getName());
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings5() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Context subContext = context.createSubcontext("context1");
        subContext.createSubcontext("context2");
        NamingEnumeration<Binding> enumeration = context.listBindings("context1/context2");
        assertNotNull(enumeration);
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings6() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Context subContext = context.createSubcontext("context1");
        subContext.bind("context2", "value");
        assertThrows(NamingException.class, () -> context.listBindings("context1/context2"));
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings7() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.createSubcontext("name");
        NamingEnumeration<Binding> enumeration = context.listBindings(new CompositeName("name"));
        assertNotNull(enumeration);
    }

    /**
     * Test listBindings method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListBindings8() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", new InitialContext());
        NamingEnumeration<Binding> enumeration = context.listBindings(new CompositeName("name"));
        assertNotNull(enumeration);
    }

    /**
     * Test lookup method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testLookup2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertThrows(NameNotFoundException.class, () -> context.lookup("name"));
    }

    /**
     * Test lookup method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testLookup3() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("subContext/name", true);
        assertNotNull(context.lookup("subContext/name"));
    }

    /**
     * Test lookup method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testLookup4() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("subContext/name", true);
        assertThrows(NameNotFoundException.class, () -> context.lookup("subContext/subContext2/name"));
    }

    /**
     * Test lookupLink method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testLookupLink() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", "value");
        assertNotNull(context.lookupLink("name"));
    }

    /**
     * Test lookupLink method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testLookupLink2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", "value");
        assertNotNull(context.lookupLink(new CompositeName("name")));
    }

    /**
     * Test rebind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testRebind() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("name");
        context.rebind(name, "value");
        assertNotNull(context.lookup(name));
    }

    /**
     * Test rebind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testRebind2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("context/name");
        context.rebind(name, "value");
        assertNotNull(context.lookup(name));
        context.rebind(name, "value");
        assertNotNull(context.lookup(name));
    }

    /**
     * Test rename method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testRename() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", "value");
        assertThrows(NameAlreadyBoundException.class, () -> context.rename("rename", "name"));
    }

    /**
     * Test rename method.
     */
    @Test
    void testRename2() {
        try {
            DefaultInitialContext context = new DefaultInitialContext();
            context.bind("name", "value");
            context.rename(new CompositeName("name"), new CompositeName("newname"));
        } catch (NamingException ex) {
            fail();
        }
    }

    /**
     * Test unbind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testUnbind() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("name", "value");
        assertNotNull(context.lookup("name"));
        context.unbind("name");
        assertThrows(NameNotFoundException.class, () -> context.lookup("name"));
    }

    /**
     * Test unbind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testUnbind2() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        Name name = new CompositeName("name");
        context.bind(name, "value");
        assertNotNull(context.lookup(name));
        context.unbind(name);
        assertThrows(NameNotFoundException.class, () -> context.lookup(name));
    }

    /**
     * Test unbind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testUnbind3() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        assertThrows(NameNotFoundException.class, () -> context.unbind("composite/name"));
    }

    /**
     * Test unbind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testUnbind4() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("composite/name", "test");
        context.unbind("composite/name");
        assertThrows(NameNotFoundException.class, () -> context.lookup("composite/name"));
    }

    /**
     * Test unbind method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testUnbind5() throws Exception {
        DefaultInitialContext context = new DefaultInitialContext();
        context.bind("composite/name", "test");
        assertThrows(NameNotFoundException.class, () -> context.unbind("composite2/name"));
    }
}

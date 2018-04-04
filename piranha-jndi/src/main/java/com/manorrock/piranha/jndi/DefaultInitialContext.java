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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;

/**
 * The default InitialContext.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultInitialContext implements Context {

    /**
     * Defines the "Not supported yet." constant.
     */
    private static final String NOT_SUPPORTED_YET = "Not supported yet.";
    
    /**
     * Stores the bindings.
     */
    private final Map<String, Object> bindings = new ConcurrentHashMap<>();

    /**
     * Stores the closed flag.
     */
    private boolean closed = false;

    /**
     * List the entries for the given name.
     *
     * @param name the name.
     * @return the name class pair enumeration.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return new DefaultNamingEnumeration();
    }

    /**
     * Lookup the link.
     *
     * @param name the name.
     * @return the link.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Object lookupLink(Name name) throws NamingException {
        return lookupLink(name.toString());
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    /**
     * Bind the object to the given name.
     *
     * @param name the name.
     * @param object the object.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void bind(Name name, Object object) throws NamingException {
        checkClosed();
        String nameString = name.toString();
        bind(nameString, object);
    }

    /**
     * Bind the object to the given name.
     *
     * @param name the name.
     * @param object the object.
     * @throws NamingException when an naming error occurs.
     */
    @Override
    public void bind(String name, Object object) throws NamingException {
        checkClosed();
        if (name.contains("/")) {
            String[] names = name.split("/");
            DefaultInitialContext contextMap = this;
            for (int i = 0; i < names.length - 1; i++) {
                try {
                    contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
                } catch (NameNotFoundException ne) {
                    contextMap = (DefaultInitialContext) contextMap.createSubcontext(names[i]);
                }
            }
            contextMap.bind(names[names.length - 1], object);
        } else if (!bindings.containsKey(name)) {
            bindings.put(name, object);
        } else {
            throw new NameAlreadyBoundException("Name '" + name + "' already bound");
        }
    }

    /**
     * Helper method to verify if we not are closed.
     *
     * @throws NamingException when the context is closed.
     */
    private void checkClosed() throws NamingException {
        if (closed) {
            throw new NamingException("Cannot call any method on a closed context");
        }
    }

    /**
     * Close the context.
     *
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void close() throws NamingException {
        checkClosed();
        if (!closed) {
            closed = true;
        }
    }

    /**
     * Create a sub context.
     *
     * @param name the name.
     * @return the sub context.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Context createSubcontext(Name name) throws NamingException {
        checkClosed();
        return createSubcontext(name.toString());
    }

    /**
     * Create a sub context.
     *
     * @param name the name.
     * @return the sub context.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Context createSubcontext(String name) throws NamingException {
        checkClosed();
        bindings.put(name, new DefaultInitialContext());
        return (Context) bindings.get(name);
    }

    /**
     * Compose the name.
     *
     * @param name the name.
     * @param prefix the prefix.
     * @return the name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public String composeName(String name, String prefix) throws NamingException {
        if (prefix == null || !"".equals(prefix)) {
            throw new NamingException("prefix is not the empty string");
        }
        return name;
    }

    /**
     * Destroy the sub context.
     *
     * @param name the name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void destroySubcontext(Name name) throws NamingException {
        checkClosed();
        destroySubcontext(name.toString());
    }

    /**
     * Destroy the sub context.
     *
     * @param name the name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void destroySubcontext(String name) throws NamingException {
        if (name.contains("/")) {
            String[] names = name.split("/");
            DefaultInitialContext contextMap = this;
            try {
                for (int i = 0; i < names.length - 1; i++) {
                    contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
                }
                contextMap.destroySubcontext(names[names.length - 1]);
            } catch (NameNotFoundException nnfe) {
                throw nnfe;
            }
        } else if (bindings.containsKey(name)) {
            if (bindings.get(name) instanceof DefaultInitialContext) {
                DefaultInitialContext subContext = (DefaultInitialContext) bindings.get(name);
                if (subContext.bindings.isEmpty()) {
                    bindings.remove(name);
                } else {
                    throw new ContextNotEmptyException("Context not empty: " + name);
                }
            } else {
                throw new NotContextException("Not a context: " + name);
            }
        } else {
            throw new NameNotFoundException("Unable to find name: " + name);
        }
    }

    /**
     * Get the environment.
     *
     * @return the environment.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        checkClosed();
        return new Hashtable();
    }

    /**
     * Get the name in the namespace.
     *
     * @return the name in its own namespace.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public String getNameInNamespace() throws NamingException {
        checkClosed();
        throw new OperationNotSupportedException();
    }

    /**
     * List the names in the named context.
     *
     * @param name the name.
     * @return the enumeration.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return list(name.toString());
    }

    /**
     * List the bindings.
     *
     * @param name the name.
     * @return the bindings.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return listBindings(name.toString());
    }

    /**
     * List the bindings.
     *
     * @param name the name.
     * @return the bindings.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        ArrayList<Binding> list = new ArrayList<>();
        DefaultInitialContext contextMap = this;

        if (name.contains("/")) {
            String[] names = name.split("/");
            for (int i = 0; i < names.length - 1; i++) {
                contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
            }
            if (contextMap.lookup(names[names.length - 1]) instanceof DefaultInitialContext) {
                contextMap = (DefaultInitialContext) contextMap.lookup(names[names.length - 1]);
            } else {
                throw new NamingException("Name " + name + " is not a named context");
            }
        } else {
            if (bindings.containsKey(name) && bindings.get(name) instanceof DefaultInitialContext) {
                contextMap = (DefaultInitialContext) bindings.get(name);
            } else {
                contextMap = null;
            }
        }

        if (contextMap != null) {
            for (Map.Entry<String, Object> entry : contextMap.bindings.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Binding binding = new Binding(key, value);
                list.add(binding);
            }
        }

        return new DefaultBindingNamingEnumeration(list);
    }

    /**
     * Look the name.
     *
     * @param name the name.
     * @return the object.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    /**
     * Lookup the name.
     *
     * @param name the name.
     * @return the object.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Object lookup(String name) throws NamingException {
        checkClosed();
        if (name.contains("/")) {
            String[] names = name.split("/");
            DefaultInitialContext contextMap = this;
            for (int i = 0; i < names.length - 1; i++) {
                contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
            }
            return contextMap.lookup(names[names.length - 1]);
        } else if (bindings.containsKey(name)) {
            return bindings.get(name);
        } else {
            throw new NameNotFoundException("Name " + name + " not found");
        }
    }

    /**
     * Rebind the name.
     *
     * @param name the name.
     * @param object the object.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void rebind(Name name, Object object) throws NamingException {
        rebind(name.toString(), object);
    }

    /**
     * Rebind to the given name.
     *
     * @param name the name.
     * @param obj the object.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void rebind(String name, Object obj) throws NamingException {
        checkClosed();
        if (name.contains("/")) {
            String[] names = name.split("/");
            DefaultInitialContext contextMap = this;
            for (int i = 0; i < names.length - 1; i++) {
                try {
                    contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
                } catch (NameNotFoundException nnfe) {
                    contextMap.createSubcontext(names[i]);
                    contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
                }
            }
            contextMap.rebind(names[names.length - 1], obj);
        } else {
            bindings.put(name, obj);
        }
    }

    /**
     * Rename the object.
     *
     * @param oldName the old name.
     * @param newName the new name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        rename(oldName.toString(), newName.toString());
    }

    /**
     * Rename the object.
     *
     * @param oldName the old name.
     * @param newName the new name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void rename(String oldName, String newName) throws NamingException {
        checkClosed();
        try {
            lookup(newName);
            throw new NameAlreadyBoundException(newName);
        } catch (NameNotFoundException nnfe) {
            // intentionally not caught.
        }
        Object object = lookup(oldName);
        bind(newName, object);
        unbind(oldName);
    }

    /**
     * Unbind the specified name.
     *
     * @param name the name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void unbind(Name name) throws NamingException {
        unbind(name.toString());
    }

    /**
     * Unbind the specified name.
     *
     * @param name the name.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public void unbind(String name) throws NamingException {
        checkClosed();
        if (name.contains("/")) {
            String[] names = name.split("/");
            DefaultInitialContext contextMap = this;
            for (int i = 0; i < names.length - 1; i++) {
                contextMap = (DefaultInitialContext) contextMap.lookup(names[i]);
            }
            contextMap.unbind(names[names.length - 1]);
        } else {
            bindings.remove(name);
        }
    }
}

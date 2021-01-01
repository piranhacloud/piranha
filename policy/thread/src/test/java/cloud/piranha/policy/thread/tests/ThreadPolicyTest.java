/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.policy.thread.tests;

import cloud.piranha.policy.thread.ThreadPolicy;
import java.security.AllPermission;
import java.security.NoSuchAlgorithmException;
import java.security.Policy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ThreadPolicy class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ThreadPolicyTest {
    
    /**
     * Test getParameters method.
     */
    @Test
    public void testGetParameters() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            assertNull(policy.getParameters());
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }

    /**
     * Test getPermissions method.
     */
    @Test
    public void testGetPermissions() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            assertNotNull(policy.getPermissions(getClass().getProtectionDomain().getCodeSource()));
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }

    /**
     * Test getPermissions method.
     */
    @Test
    public void testGetPermissions2() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            assertNotNull(policy.getPermissions(getClass().getProtectionDomain()));
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }

    /**
     * Test getProvider method.
     */
    @Test
    public void testGetProvider() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            assertNotNull(policy.getProvider());
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }

    /**
     * Test getType method.
     */
    @Test
    public void testGetType() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            assertNotNull(policy.getType());
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }

    /**
     * Test implies method.
     */
    @Test
    public void testImplies() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            assertFalse(policy.implies(getClass().getProtectionDomain(), new AllPermission()));
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }

    /**
     * Test refresh method.
     */
    @Test
    public void testRefresh() {
        ThreadPolicy policy = new ThreadPolicy();
        try {
            ThreadPolicy.setPolicy(Policy.getInstance("JavaPolicy", null));
            policy.refresh();
        } catch (NoSuchAlgorithmException nsae) {
            fail();
        } finally {
            ThreadPolicy.removePolicy();
        }
    }
}

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
package cloud.piranha.extension.webxml.internal;

import cloud.piranha.core.api.WebXml;
import static cloud.piranha.core.api.WebXml.OTHERS_TAG;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class InternalWebXmlManagerTest {

    private static WebXml toWebFragment(String fragmentName) {
        WebXml fragment = new WebXml();
        fragment.setFragmentName(fragmentName);
        return fragment;
    }

    @Test
    void testNoOrdering() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        List<String> fragmentNames = Arrays.asList("a", "b", "c", "d", "e");
        Collections.shuffle(fragmentNames);

        manager.setWebFragments(fragmentNames.stream().map(InternalWebXmlManagerTest::toWebFragment).toList());

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(5, orderedFragments.size());
        assertAll(() -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("a"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("b"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("c"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("d"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("e"::equals)));
    }

    @Test
    void testAbsoluteOrdering() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        webXml.setAbsoluteOrdering(List.of("a", "c", "e"));

        List<String> fragmentNames = Arrays.asList("a", "b", "c", "d", "e");
        Collections.shuffle(fragmentNames);

        manager.setWebFragments(fragmentNames.stream().map(InternalWebXmlManagerTest::toWebFragment).toList());

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(3, orderedFragments.size());
        assertAll(() -> assertEquals("a", orderedFragments.get(0).getFragmentName()),
                () -> assertEquals("c", orderedFragments.get(1).getFragmentName()),
                () -> assertEquals("e", orderedFragments.get(2).getFragmentName()));
    }

    @Test
    void testAbsoluteOrdering2() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        webXml.setAbsoluteOrdering(List.of("a", "c", "a", "e"));

        List<String> fragmentNames = Arrays.asList("a", "b", "c", "d", "e");
        Collections.shuffle(fragmentNames);

        manager.setWebFragments(fragmentNames.stream().map(InternalWebXmlManagerTest::toWebFragment).toList());

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(3, orderedFragments.size());
        assertAll(() -> assertEquals("a", orderedFragments.get(0).getFragmentName()),
                () -> assertEquals("c", orderedFragments.get(1).getFragmentName()),
                () -> assertEquals("e", orderedFragments.get(2).getFragmentName()));
    }

    @Test
    void testAbsoluteOrdering3() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        webXml.setAbsoluteOrdering(List.of("a", OTHERS_TAG, "c"));

        List<String> fragmentNames = Arrays.asList("a", "b", "c", "d", "e");
        Collections.shuffle(fragmentNames);

        manager.setWebFragments(fragmentNames.stream().map(InternalWebXmlManagerTest::toWebFragment).toList());

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(5, orderedFragments.size());
        assertAll(() -> assertEquals("a", orderedFragments.get(0).getFragmentName()),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("c"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("d"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("e"::equals)),
                () -> assertEquals("c", orderedFragments.get(4).getFragmentName()));
    }

    @Test
    void testAbsoluteOrdering4() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        webXml.setAbsoluteOrdering(List.of(OTHERS_TAG, "a", "c", "d"));

        List<String> fragmentNames = Arrays.asList("a", "b", "c", "d", "e");
        Collections.shuffle(fragmentNames);

        manager.setWebFragments(fragmentNames.stream().map(InternalWebXmlManagerTest::toWebFragment).toList());

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(5, orderedFragments.size());
        assertAll(() -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("b"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("e"::equals)),
                () -> assertEquals("a", orderedFragments.get(2).getFragmentName()),
                () -> assertEquals("c", orderedFragments.get(3).getFragmentName()),
                () -> assertEquals("d", orderedFragments.get(4).getFragmentName()));
    }

    @Test
    void testAbsoluteOrdering5() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        webXml.setAbsoluteOrdering(List.of("a", "c", "d", OTHERS_TAG));

        List<String> fragmentNames = Arrays.asList("a", "b", "c", "d", "e");
        Collections.shuffle(fragmentNames);

        manager.setWebFragments(fragmentNames.stream().map(InternalWebXmlManagerTest::toWebFragment).toList());

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(5, orderedFragments.size());
        assertAll(() -> assertEquals("a", orderedFragments.get(0).getFragmentName()),
                () -> assertEquals("c", orderedFragments.get(1).getFragmentName()),
                () -> assertEquals("d", orderedFragments.get(2).getFragmentName()),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("b"::equals)),
                () -> assertTrue(orderedFragments.stream().map(WebXml::getFragmentName).anyMatch("e"::equals)));
    }

    @Test
    void testRelativeOrdering() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        WebXml fragmentA = toWebFragment("a");
        fragmentA.setRelativeOrdering(new WebXml.RelativeOrder(null, List.of(OTHERS_TAG, "c")));

        WebXml fragmentB = toWebFragment("b");
        fragmentB.setRelativeOrdering(new WebXml.RelativeOrder(List.of(OTHERS_TAG), null));

        WebXml fragmentC = toWebFragment("c");
        fragmentC.setRelativeOrdering(new WebXml.RelativeOrder(null, List.of(OTHERS_TAG)));

        WebXml fragmentD = toWebFragment("d");
        WebXml fragmentE = toWebFragment("e");

        WebXml fragmentF = toWebFragment("f");
        fragmentF.setRelativeOrdering(new WebXml.RelativeOrder(List.of(OTHERS_TAG, "b"), null));

        manager.setWebFragments(Arrays.asList(fragmentA, fragmentB, fragmentC, fragmentD, fragmentE, fragmentF));

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(6, orderedFragments.size());
        assertAll(() -> assertEquals("f", orderedFragments.get(0).getFragmentName()),
                () -> assertEquals("b", orderedFragments.get(1).getFragmentName()),
                () -> assertEquals("d", orderedFragments.get(2).getFragmentName()),
                () -> assertEquals("e", orderedFragments.get(3).getFragmentName()),
                () -> assertEquals("c", orderedFragments.get(4).getFragmentName()),
                () -> assertEquals("a", orderedFragments.get(5).getFragmentName()));
    }

    @Test
    void testRelativeOrdering2() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        WebXml fragmentNoID = new WebXml();
        fragmentNoID.setRelativeOrdering(new WebXml.RelativeOrder(List.of("c"), List.of(OTHERS_TAG)));

        WebXml fragmentB = toWebFragment("b");
        fragmentB.setRelativeOrdering(new WebXml.RelativeOrder(List.of(OTHERS_TAG), null));

        WebXml fragmentC = toWebFragment("c");

        WebXml fragmentD = toWebFragment("d");
        fragmentD.setRelativeOrdering(new WebXml.RelativeOrder(null, List.of(OTHERS_TAG)));

        WebXml fragmentE = toWebFragment("e");
        fragmentE.setRelativeOrdering(new WebXml.RelativeOrder(List.of(OTHERS_TAG), null));

        WebXml fragmentF = toWebFragment("f");

        manager.setWebFragments(Arrays.asList(fragmentNoID, fragmentB, fragmentC, fragmentD, fragmentE, fragmentF));

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(6, orderedFragments.size());

        List<String> validOrderings = List.of(
            "befnullcd",
            "befnulldc",
            "ebfnullcd",
            "ebfnulldc",
            "ebfdnullc",
            "ebfdnulld" // the spec example repeats the d fragment
        );

        assertTrue(validOrderings.contains(orderedFragments.stream().map(WebXml::getFragmentName).collect(Collectors.joining(""))));
    }

    @Test
    void testRelativeOrdering3() {
        InternalWebXmlManager manager = new InternalWebXmlManager();
        WebXml webXml = new WebXml();
        manager.setWebXml(webXml);

        WebXml fragmentA = toWebFragment("a");
        fragmentA.setRelativeOrdering(new WebXml.RelativeOrder(null, List.of("b")));

        WebXml fragmentB = toWebFragment("b");

        WebXml fragmentC = toWebFragment("c");
        fragmentC.setRelativeOrdering(new WebXml.RelativeOrder(List.of(OTHERS_TAG), null));

        WebXml fragmentD = toWebFragment("d");

        manager.setWebFragments(Arrays.asList(fragmentA, fragmentB, fragmentC, fragmentD));

        List<WebXml> orderedFragments = manager.getOrderedFragments();
        assertEquals(4, orderedFragments.size());

        List<String> validOrderings = List.of(
                "cbda",
                "cdba",
                "cbad"
        );

        assertTrue(validOrderings.contains(orderedFragments.stream().map(WebXml::getFragmentName).collect(Collectors.joining(""))));
    }
}

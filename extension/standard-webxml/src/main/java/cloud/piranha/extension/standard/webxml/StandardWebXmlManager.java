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
package cloud.piranha.extension.standard.webxml;

import cloud.piranha.core.api.WebXml;
import static cloud.piranha.core.api.WebXml.OTHERS_TAG;
import cloud.piranha.core.api.WebXmlManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The web.xml manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class StandardWebXmlManager implements WebXmlManager {

    /**
     * Stores the initial web.xml
     */
    private WebXml initialWebXml;

    /**
     * Stores the web fragments.
     */
    private List<WebXml> webFragments = new ArrayList<>();

    /**
     * Stores the web.xml.
     */
    private WebXml webXml;

    /**
     * {@return the initial web.xml}
     */
    public WebXml getInitialWebXml() {
        return initialWebXml;
    }

    /**
     * Set the initial web.xml.
     *
     * @param initialWebXml the initial web.xml.
     */
    public void setInitialWebXml(WebXml initialWebXml) {
        this.initialWebXml = initialWebXml;
    }

    /**
     * {@return the web fragments}
     */
    public List<WebXml> getWebFragments() {
        return webFragments;
    }

    /**
     * Set the web fragments.
     *
     * @param webFragments the web fragments.
     */
    public void setWebFragments(List<WebXml> webFragments) {
        this.webFragments = webFragments;
    }

    @Override
    public WebXml getWebXml() {
        return webXml;
    }

    /**
     * Set the web.xml.
     *
     * @param webXml the web.xml.
     */
    public void setWebXml(WebXml webXml) {
        this.webXml = webXml;
    }

    /**
     * {@return the ordered fragments}
     */
    public List<WebXml> getOrderedFragments() {
        if (webXml == null)
            return Collections.emptyList();
        if (webXml.getAbsoluteOrdering() != null) {
            return processAbsoluteOrdering();
        }
        return processRelativeOrdering();
    }

    private List<WebXml> processAbsoluteOrdering() {
        List<String> absoluteOrdering = webXml.getAbsoluteOrdering().stream().distinct().toList();
        if (absoluteOrdering.isEmpty())
            return Collections.emptyList();

        if (!absoluteOrdering.contains(OTHERS_TAG)) {
            return getWebFragments()
                    .stream()
                    .filter(x -> absoluteOrdering.contains(x.getFragmentName()))
                    .sorted(Comparator.comparingInt(x -> absoluteOrdering.indexOf(x.getFragmentName())))
                    .toList();
        }

        int indexOfOthersTag = absoluteOrdering.indexOf(OTHERS_TAG);

        List<String> fragmentsBeforeOthers = absoluteOrdering.stream()
            .filter(x -> absoluteOrdering.indexOf(x) < indexOfOthersTag)
            .toList();

        List<String> fragmentsAfterOthers = absoluteOrdering.stream()
            .filter(x -> absoluteOrdering.indexOf(x) > indexOfOthersTag)
            .toList();

        List<String> othersFragments = getWebFragments()
                .stream()
                .map(WebXml::getFragmentName)
                .filter(x -> !fragmentsAfterOthers.contains(x) && !fragmentsBeforeOthers.contains(x))
                .toList();

        List<WebXml> orderedFragments = new ArrayList<>();
        for (String fragmentName : absoluteOrdering) {
            if (OTHERS_TAG.equals(fragmentName)) {
                orderedFragments.addAll(toWebXml(othersFragments));
                continue;
            }
            findWebFragment(fragmentName).ifPresent(orderedFragments::add);
        }
        return orderedFragments;
    }

    private List<WebXml> toWebXml(List<String> fragmentNames) {
        return getWebFragments().stream().filter(x -> fragmentNames.contains(x.getFragmentName())).toList();
    }

    private Optional<WebXml> findWebFragment(String fragmentName) {
        return getWebFragments().stream().filter(x -> fragmentName.equals(x.getFragmentName())).findFirst();
    }

    private List<WebXml> processRelativeOrdering() {
        if (webFragments.stream().noneMatch(x -> x.getRelativeOrdering() != null)) {
            return webFragments;
        }

        List<WebXml> sorted = new ArrayList<>(webFragments);
        Set<WebXml> others = new LinkedHashSet<>();
        Set<WebXml> beforeOthers = new LinkedHashSet<>();
        Set<WebXml> afterOthers = new LinkedHashSet<>();

        // Separate the fragments that are before/after others and the others
        webFragments.forEach(webFragment -> {
            WebXml.RelativeOrder relativeOrdering = webFragment.getRelativeOrdering();
            if (relativeOrdering == null) {
                others.add(webFragment);
                return;
            }
            List<String> after = relativeOrdering.getAfter();
            List<String> before = relativeOrdering.getBefore();
            if (after.contains(OTHERS_TAG)) {
                afterOthers.add(webFragment);
                return;
            }
            if (before.contains(OTHERS_TAG)) {
                beforeOthers.add(webFragment);
                return;
            }
            others.add(webFragment);
        });

        TopologicalSort<WebXml> topologicalSort = new TopologicalSort<>();

        // Add the dependencies of each fragment
        Set<WebXml> referenced = new LinkedHashSet<>();
        for (WebXml webFragment : webFragments) {
            if (webFragment.getRelativeOrdering() != null) {
                WebXml.RelativeOrder relativeOrdering = webFragment.getRelativeOrdering();

                for (String afterName : relativeOrdering.getAfter()) {
                    findWebFragment(afterName).ifPresent(fragment -> {
                        topologicalSort.addDependency(webFragment, fragment);
                        referenced.add(fragment);
                    });
                }

                for (String beforeName : relativeOrdering.getBefore()) {
                    findWebFragment(beforeName).ifPresent(fragment -> {
                        topologicalSort.addDependency(fragment, webFragment);
                        referenced.add(fragment);
                    });
                }

                if (relativeOrdering.getBefore().contains(OTHERS_TAG)) {
                    Consumer<WebXml> addBefore = other -> {
                        if (!referenced.contains(other)) {
                            topologicalSort.addDependency(other, webFragment);
                        }
                    };
                    others.forEach(addBefore);
                    afterOthers.forEach(addBefore);
                }
                if (relativeOrdering.getAfter().contains(OTHERS_TAG)) {
                    Consumer<WebXml> addAfter = other -> {
                        if (!referenced.contains(other)) {
                            topologicalSort.addDependency(webFragment, other);
                        }
                    };
                    beforeOthers.forEach(addAfter);
                    others.forEach(addAfter);
                }
            }
            referenced.clear();
        }

        return topologicalSort.sort(sorted);
    }

    private static class TopologicalSort<T> {

        /**
         * Stores the dependencies.
         */
        private final Map<T, Set<T>> dependencies = new HashMap<>();

        public void addDependency(T dependant, T dependency) {
            Set<T> set = dependencies.computeIfAbsent(dependant, k -> new LinkedHashSet<>());
            Collections.addAll(set, dependency);
        }
        public List<T> sort(List<T> list) {
            Set<T> visited = new HashSet<>();
            List<T> sorted = new ArrayList<>();

            for (T item : list) {
                visit(item, visited, sorted);
            }
            return sorted;
        }

        private void visit(T item, Set<T> visited, List<T> sorted) {
            if (!visited.contains(item)) {
                visited.add(item);
                Set<T> edges = this.dependencies.get(item);
                if (edges != null)
                    edges.forEach(dependency -> visit(dependency, visited, sorted));
                sorted.add(item);
            } else if (!sorted.contains(item))
                throw new IllegalStateException("Invalid dependency cyclic");
        }
    }

}

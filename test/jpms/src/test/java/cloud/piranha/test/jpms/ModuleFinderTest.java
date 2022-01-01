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
package cloud.piranha.test.jpms;

import cloud.piranha.core.impl.DefaultModuleFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cloud.piranha.resource.api.Resource;
import cloud.piranha.resource.shrinkwrap.ShrinkWrapResource;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleReference;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleFinderTest {

    private Resource createResource(JavaArchive jar, String name) {
        return new ShrinkWrapResource("", jar, name);
    }

    @Test
    void testFindAutomaticModuleWithoutClasses() {
        Resource module1 = createResource(create(JavaArchive.class), "module1");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module1)).findAll();
        assertNotNull(moduleReferences);
        assertEquals(1, moduleReferences.size());
        String moduleName = moduleReferences.stream()
            .findFirst()
            .map(ModuleReference::descriptor)
            .map(ModuleDescriptor::name)
            .orElseThrow();
        assertEquals("module1", moduleName);
    }

    @Test
    void testFindAutomaticModuleWithClass() {
        Resource module1 = createResource(create(JavaArchive.class).addClass(package1.Library.class), "module1");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module1)).findAll();
        assertNotNull(moduleReferences);
        assertEquals(1, moduleReferences.size());
        ModuleReference moduleReference = moduleReferences.stream().findFirst().orElseThrow();
        ModuleDescriptor descriptor = moduleReference.descriptor();
        assertEquals("module1", descriptor.name());
        Set<String> packages = descriptor.packages();
        assertEquals(1, packages.size());
        assertTrue(packages.contains("package1"));
    }

    @Test
    void testFindMultiPackagesSingleModule() {
        JavaArchive jar = create(JavaArchive.class)
                                .addClass(package1.Library.class)
                                .addClass(package2.Listener.class)
                                .addClass(package3.Filter.class);
        Resource module1 = createResource(jar, "module1");

        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module1)).findAll();
        assertNotNull(moduleReferences);
        assertEquals(1, moduleReferences.size());
        ModuleReference moduleReference = moduleReferences.stream().findFirst().orElseThrow();
        ModuleDescriptor descriptor = moduleReference.descriptor();
        assertEquals("module1", descriptor.name());
        Set<String> packages = descriptor.packages();
        assertEquals(3, packages.size());
        assertAll(() -> assertTrue(packages.contains("package1")),
                () -> assertTrue(packages.contains("package2")),
                () -> assertTrue(packages.contains("package3")));
    }

    @Test
    void testSplitPackagesTwoJars() {
        JavaArchive jar1 = create(JavaArchive.class).addClass(package1.Library.class);
        JavaArchive jar2 = create(JavaArchive.class).addClass(package1.Servlet.class);
        Resource module1 = createResource(jar1, "module1");
        Resource module2 = createResource(jar2, "module2");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module1, module2)).findAll();
        assertNotNull(moduleReferences);
        assertEquals(0, moduleReferences.size());
    }

    @Test
    void testSplitPackagesThreeJars() {
        JavaArchive jar1 = create(JavaArchive.class).addClass(package1.Library.class).addClass(package3.Library.class);
        JavaArchive jar2 = create(JavaArchive.class).addClass(package1.Utils.class).addClass(package2.Listener.class);
        JavaArchive jar3 = create(JavaArchive.class).addClass(package2.Library.class).addClass(package3.Filter.class);
        Resource module1 = createResource(jar1, "module1");
        Resource module2 = createResource(jar2, "module2");
        Resource module3 = createResource(jar3, "module3");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module1, module2, module3)).findAll();
        assertNotNull(moduleReferences);
        assertEquals(0, moduleReferences.size());
    }


    @Test
    void testSplitPackagesAndRegular() {
        JavaArchive jar1 = create(JavaArchive.class).addClass(package1.Library.class).addClass(package1.Servlet.class);
        JavaArchive jar2 = create(JavaArchive.class).addClass(package2.Listener.class).addClass(package2.Library.class);
        JavaArchive jar3 = create(JavaArchive.class).addClass(package1.Utils.class).addClass(package3.Filter.class);
        Resource module1 = createResource(jar1, "module1");
        Resource module2 = createResource(jar2, "module2");
        Resource module3 = createResource(jar3, "module3");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module1, module2, module3)).findAll();
        assertNotNull(moduleReferences);
        assertEquals(1, moduleReferences.size());
        assertEquals(1, moduleReferences.size());
        moduleReferences.stream().findFirst().ifPresentOrElse(x -> assertEquals("module2", x.descriptor().name()), Assertions::fail);
    }

    @ParameterizedTest
    @CsvSource({
        "module.jar,module",
        "module-test-foo.jar,module.test.foo",
        "module-version4.jar,module.version4",
        "module-foo2-bar.jar,module.foo2.bar",
        "module1-test.jar,module1.test"
    })
    void testAutomaticModuleNamesWithoutVersion(String jarName, String moduleName) {
        Resource module = createResource(create(JavaArchive.class), jarName);
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module)).findAll();
        ModuleDescriptor moduleDescriptor = moduleReferences.stream().findFirst().map(ModuleReference::descriptor).orElseThrow();
        assertEquals(moduleName, moduleDescriptor.name());
        assertTrue(moduleDescriptor.version().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
        "module-4.0.0.jar,module,4.0.0",
        "module-test-foo-2.jar,module.test.foo,2",
        "module-test-bar-1.0-SNAPSHOT.jar,module.test.bar,1.0-SNAPSHOT",
        "module-test-baz-7.3.Final.jar,module.test.baz,7.3.Final"
    })
    void testAutomaticModuleNamesWithVersion(String jarName, String moduleName, String version) {
        Resource module = createResource(create(JavaArchive.class), jarName);
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module)).findAll();
        ModuleDescriptor moduleDescriptor = moduleReferences.stream().findFirst().map(ModuleReference::descriptor).orElseThrow();
        assertEquals(moduleName, moduleDescriptor.name());
        assertEquals(version, moduleDescriptor.version().map(ModuleDescriptor.Version::toString).orElseThrow());
    }

    @Test
    void testServices() {
        JavaArchive archive = create(JavaArchive.class).addAsServiceProvider(package1.Service.class, package3.DefaultService.class);
        Resource module = createResource(archive, "module");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module)).findAll();
        ModuleDescriptor moduleDescriptor = moduleReferences.stream().findFirst().map(ModuleReference::descriptor).orElseThrow();
        assertEquals("module", moduleDescriptor.name());
        Set<ModuleDescriptor.Provides> provides = moduleDescriptor.provides();
        assertEquals(1, provides.size());
        ModuleDescriptor.Provides provide = provides.stream().findFirst().orElseThrow();
        assertEquals(package1.Service.class.getName(), provide.service());
        assertEquals(1, provide.providers().size());
        assertTrue(provide.providers().contains(package3.DefaultService.class.getName()));
    }

    @Test
    void testInvalidServices() {
        JavaArchive archive = create(JavaArchive.class).addAsServiceProvider("package1.Service", "invalid-name");
        Resource module = createResource(archive, "module");
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module)).findAll();
        assertEquals(0, moduleReferences.size());
    }

    @ParameterizedTest
    @CsvSource({
        "jar-old-name,module.foo.bar",
        "enum-new-package,valid.module"
    })
    void testModuleNameFromManifest(String jarName, String automaticModuleName) {
        String manifestContent =
        """
        Manifest-Version: 1.0
        Automatic-Module-Name: %s
        """.formatted(automaticModuleName);

        ByteArrayAsset manifest = new ByteArrayAsset(manifestContent.getBytes(StandardCharsets.UTF_8));
        JavaArchive archive = create(JavaArchive.class).addAsManifestResource(manifest, "MANIFEST.MF");
        Resource module = createResource(archive, jarName);
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module)).findAll();
        assertEquals(1, moduleReferences.size());
        String moduleName = moduleReferences.stream()
                .findFirst()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .orElseThrow();
        assertEquals(automaticModuleName, moduleName);
    }

    @ParameterizedTest
    @CsvSource({
        "jar-old-name,jar.new.name",
        "enum-new-package,enum.new.package"
    })
    void testInvalidModuleNameFromManifest(String jarName, String automaticModuleName) {
        String manifestContent =
        """
        Manifest-Version: 1.0
        Automatic-Module-Name: %s
        """.formatted(automaticModuleName);

        ByteArrayAsset manifest = new ByteArrayAsset(manifestContent.getBytes(StandardCharsets.UTF_8));
        JavaArchive archive = create(JavaArchive.class).addAsManifestResource(manifest, "MANIFEST.MF");
        Resource module = createResource(archive, jarName);
        Set<ModuleReference> moduleReferences = new DefaultModuleFinder(List.of(module)).findAll();
        assertEquals(0, moduleReferences.size());
    }

}

/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of the copyright holder nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
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
 *
 */

package cloud.piranha.resource;

import cloud.piranha.resource.api.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class DefaultModuleFinder implements ModuleFinder {

    /**
     * Stores the attribute Automatic-Module-Name
     */
    private static final Attributes.Name AUTOMATIC_MODULE_NAME
            = new Attributes.Name("Automatic-Module-Name");

    /**
     * Stores the dash version pattern
     */
    private static final Pattern DASH_VERSION = Pattern.compile("-(\\d+(\\.|$))");

    /**
     * Stores the dash version pattern
     */
    private static final Pattern NON_ALPHANUM = Pattern.compile("[^A-Za-z0-9]");

    /**
     * Stores the repeating dots pattern
     */
    private static final Pattern REPEATING_DOTS = Pattern.compile("(\\.)(\\1)+");

    /**
     * Stores the leading dots pattern
     */
    private static final Pattern LEADING_DOTS = Pattern.compile("^\\.");

    /**
     * Stores the trailing dots pattern
     */
    private static final Pattern TRAILING_DOTS = Pattern.compile("\\.$");

    /**
     * Stores the cache for the modules already resolved
     */
    private final Map<String, ModuleReference> cachedModuleReferences = new HashMap<>();

    /**
     * Store the resources
     */
    private final List<Resource> resources;

    /**
     * Stores the searched flag
     */
    private boolean searched = false;

    /**
     * Constructor
     * @param resources the resources
     */
    public DefaultModuleFinder(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public Optional<ModuleReference> find(String name) {

        ModuleReference moduleReference = cachedModuleReferences.get(name);
        if (moduleReference != null) {
            return Optional.of(moduleReference);
        }

        searchAllModules();

        return Optional.ofNullable(cachedModuleReferences.get(name));
    }

    private ModuleDescriptor moduleDescriptorFromResource(Resource resource) {
        ModuleDescriptor moduleInfo = moduleInfo(resource);
        if (moduleInfo != null)
            return moduleInfo;

        String moduleName = deriveAutomaticModuleNameFromManifest(resource);
        String name = resource.getName().replace(".jar", "");

        String versionString = null;
        // find first occurrence of -${NUMBER}. or -${NUMBER}$
        Matcher matcher = DASH_VERSION.matcher(name);
        if (matcher.find()) {
            int start = matcher.start();

            // attempt to parse the tail as a version string
            try {
                String tail = name.substring(start + 1);
                ModuleDescriptor.Version.parse(tail);
                versionString = tail;
            } catch (IllegalArgumentException ignore) { }

            name = name.substring(0, start);
        }

        moduleName = normalizeModuleName(moduleName != null ? moduleName : cleanModuleName(name));

        System.out.println("Module " + moduleName + ((versionString != null)? "@" + versionString : "") + " from " + resource.getName());
        ModuleDescriptor.Builder builder = ModuleDescriptor.newAutomaticModule(moduleName);

        if (versionString != null)
            builder.version(versionString);

        Set<String> packages = packages(resource);
        System.out.println(packages);

        builder.packages(packages);

        addProviders(builder, resource);

        return builder.build();
    }

    private void addProviders(ModuleDescriptor.Builder builder, Resource resource) {
        Set<String> providers = resource.getAllLocations()
                .filter(x -> x.startsWith("/META-INF/services/") && !x.endsWith("/"))
                .collect(toSet());

        for (String providerFile : providers) {
            InputStream inputStream = resource.getResourceAsStream(providerFile);
            if (inputStream == null)
                continue;
            List<String> p = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .filter(x -> !x.startsWith("#"))
                    .filter(x -> !x.isEmpty())
                    .collect(Collectors.toList());
            if (!p.isEmpty()) {
                builder.provides(providerFile.substring("/META-INF/services/".length()), p);
            }
        }
    }

    private Set<String> packages(Resource resource) {
        return resource.getAllLocations()
            .filter(x -> x.endsWith(".class"))
            .filter(x -> !x.startsWith("/META-INF"))
            .map(x -> x.substring(1))
            .map(this::toPackageName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());
    }

    private String normalizeModuleName(String module) {
        StringBuilder result = new StringBuilder();
        for (String part : module.split("\\.")) {
            if (!Character.isJavaIdentifierStart(part.charAt(0))) {
                result.append("$").append(part);
            } else {
                result.append(part);
            }
            result.append(".");
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }

    private Optional<String> toPackageName(String name) {
        int index = name.lastIndexOf("/");
        if (index > 0) {
            return Optional.of(name.substring(0,
                    index).replace('/', '.'));
        } else {
            return Optional.empty();
        }
    }

    private ModuleDescriptor moduleInfo(Resource resource) {
        // Need to parse MRJARs
        try (InputStream moduleInfo = resource.getResourceAsStream("module-info.class")) {
            if (moduleInfo != null) {
                // We have a module-info
                return ModuleDescriptor.read(moduleInfo);
            }
        } catch (IOException e) {
        }
        return null;
    }

    private String deriveAutomaticModuleNameFromManifest(Resource resource) {
        try (InputStream manifestInputStream = resource.getResourceAsStream("META-INF/MANIFEST.MF")) {
            if (manifestInputStream == null)
                return null;
            Manifest man = new Manifest(manifestInputStream);
            Attributes attrs = man.getMainAttributes();
            if (attrs != null) {
                return attrs.getValue(AUTOMATIC_MODULE_NAME);
            }

        } catch (IOException e) {
        }

        return null;
    }

    private void searchAllModules() {
        if (searched)
            return;
        searched = true;
        Map<String, Map<Resource, ModuleDescriptor>> packageToExporter = new HashMap<>();

        for (Resource resource : resources) {
            try {
                boolean shouldAdd = true;
                ModuleDescriptor moduleDescriptor = moduleDescriptorFromResource(resource);
                for (String aPackage : moduleDescriptor.packages()) {
                    Map<Resource, ModuleDescriptor> resolvedModule = packageToExporter.put(aPackage, Map.of(resource, moduleDescriptor));
                    if (resolvedModule != null) {
                        shouldAdd = false;
                        Resource previousResource = resolvedModule.keySet().iterator().next();
                        ModuleDescriptor previousModuleDescriptor = resolvedModule.values().iterator().next();
                        System.out.printf("------------------ Both module %s (%s) and %s (%s) exports package %s%nThey will belong to the unnamed module%n", moduleDescriptor.name(), resource.getName(), previousModuleDescriptor.name(), previousResource.getName(), aPackage);
                        cachedModuleReferences.remove(previousModuleDescriptor.name());
                    }
                }
                if (shouldAdd)
                    cachedModuleReferences.put(moduleDescriptor.name(), new DefaultModuleReference(moduleDescriptor, URI.create("resource://" + resource.getName()), resource));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        List<ModuleDescriptor> x = createModuleFromSplitPackages(moduleDescriptor, previousModuleDescriptor);
//
//        cachedModuleReferences.put(moduleDescriptor.name(), new DefaultModuleReference(x.get(0), URI.create("resource://" + resource.getName()), resource));
//
//        cachedModuleReferences.replace(previousModuleDescriptor.name(), new DefaultModuleReference(x.get(1), URI.create("resource://" + previousResource.getName()), previousResource));
//
//        cachedModuleReferences.put(x.get(2).name(), new DefaultModuleReference(x.get(2), URI.create("resource://" + resource.getName()), Resource.compose(previousResource, resource)));

    }

    private List<ModuleDescriptor> createModuleFromSplitPackages(ModuleDescriptor moduleDescriptor, ModuleDescriptor put) {

        HashSet<String> newPackages = new HashSet<>(moduleDescriptor.packages());
        newPackages.addAll(put.packages());
        ModuleDescriptor mergedModules = ModuleDescriptor.newAutomaticModule(moduleDescriptor.name() + ".merged." + put.name())
                .packages(newPackages).build();

        return List.of(ModuleDescriptor.newAutomaticModule(moduleDescriptor.name()).build(),
                    ModuleDescriptor.newAutomaticModule(put.name()).build(),
                    mergedModules);
    }


    @Override
    public Set<ModuleReference> findAll() {
        searchAllModules();
        return Set.copyOf(cachedModuleReferences.values());
    }

    /**
     * Clean up candidate module name derived from a JAR file name.
     */
    private static String cleanModuleName(String mn) {
        // replace non-alphanumeric
        mn = NON_ALPHANUM.matcher(mn).replaceAll(".");

        // collapse repeating dots
        mn = REPEATING_DOTS.matcher(mn).replaceAll(".");

        // drop leading dots
        if (!mn.isEmpty() && mn.charAt(0) == '.')
            mn = LEADING_DOTS.matcher(mn).replaceAll("");

        // drop trailing dots
        int len = mn.length();
        if (len > 0 && mn.charAt(len-1) == '.')
            mn = TRAILING_DOTS.matcher(mn).replaceAll("");

        return mn;
    }

}

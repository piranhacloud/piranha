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

package cloud.piranha.core.impl;

import cloud.piranha.core.api.ModuleLayerProcessor;

import java.lang.System.Logger;

import static java.lang.System.Logger.Level.WARNING;

/**
 * The module layer processor
 * @author Thiago Henrique Hupner
 */
public enum DefaultModuleLayerProcessor implements ModuleLayerProcessor {

    /**
     * Singleton instance
     */
    INSTANCE;
    /**
     * Stores the add opens property
     * <p>
     * The format is: module.source/package=module.target (, module.source/package=module.target)*
     * </p>
     */
    private static final String ADD_OPENS = "cloud.piranha.modular.add-opens";

    /**
     * Stores the add opens property
     * <p>
     * The format is: module.source/package=module.target (, module.source/package=module.target)*
     * </p>
     */
    private static final String ADD_EXPORTS = "cloud.piranha.modular.add-exports";

    /**
     * Stores the add opens property
     * <p>
     * The format is: module.source=module.target (, module.source=module.target)*
     * </p>
     */
    private static final String ADD_READS = "cloud.piranha.modular.add-reads";

    /**
     * Stores the logger
     */
    private static final Logger LOGGER = System.getLogger(DefaultModuleLayerProcessor.class.getName());

    @Override
    public void processModuleLayerOptions(ModuleLayer.Controller controller) {
        ModuleLayer moduleLayer = controller.layer();

        String opens = System.getProperty(ADD_OPENS);
        if (opens != null) {
            addExportsOrOpens(opens, moduleLayer, controller, true);
        }

        String exports = System.getProperty(ADD_EXPORTS);
        if (exports != null) {
            addExportsOrOpens(exports, moduleLayer, controller, false);
        }

        String reads = System.getProperty(ADD_READS);
        if (reads != null) {
            addReads(reads, moduleLayer, controller);
        }

    }

    private static void logModuleNotFound(String option, String module) {
        LOGGER.log(WARNING, () -> "Ignoring option " + option + " because module " + module + " was not found");
    }

    private static void addReads(String property, ModuleLayer moduleLayer, ModuleLayer.Controller controller) {
        for (String readsOption : property.trim().split(",")) {
            // module.source=module.target
            String[] parts = readsOption.trim().split("[=]", 2);
            String sourceName = parts[0].trim();
            String targetName = parts[1].trim();
            Module source = moduleLayer.findModule(sourceName).orElse(null);
            Module target = moduleLayer.findModule(targetName).orElse(null);
            if (source == null || target == null) {
                logModuleNotFound(readsOption, source == null ? sourceName : targetName);
                continue;
            }

            try {
                // When the source module is inside the application layer
                controller.addReads(source, target);
            } catch (IllegalArgumentException ignored) {
                // When the source module is in the boot layer
                source.addReads(target);
            }
        }
    }

    private static void addExportsOrOpens(String property, ModuleLayer moduleLayer, ModuleLayer.Controller controller, boolean opens) {
        for (String option : property.trim().split(",")) {
            // module.source/package=module.target
            String[] parts = option.trim().split("[/=]", 3);
            String sourceName = parts[0].trim();
            String aPackage = parts[1].trim();
            String targetName = parts[2].trim();
            Module source = moduleLayer.findModule(sourceName).orElse(null);
            Module target = moduleLayer.findModule(targetName).orElse(null);
            if (source == null || target == null) {
                logModuleNotFound(option, source == null ? sourceName : targetName);
                continue;
            }

            try {
                // When the source module is inside the application layer
                if (opens) {
                    controller.addOpens(source, aPackage, target);
                } else {
                    controller.addExports(source, aPackage, target);
                }
            } catch (IllegalArgumentException ignored) {
                // When the source module is in the boot layer
                if (opens) {
                    source.addOpens(aPackage, target);
                } else {
                    source.addExports(aPackage, target);
                }
            }
        }
    }

}

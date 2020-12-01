/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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

package cloud.piranha.resource.transformer.eclipse;

import cloud.piranha.resource.api.Resource;
import org.eclipse.transformer.TransformProperties;
import org.eclipse.transformer.action.impl.ActionImpl;
import org.eclipse.transformer.action.impl.ClassActionImpl;
import org.eclipse.transformer.action.impl.ContainerActionImpl;
import org.eclipse.transformer.action.impl.InputBufferImpl;
import org.eclipse.transformer.action.impl.JarActionImpl;
import org.eclipse.transformer.action.impl.SelectionRuleImpl;
import org.eclipse.transformer.action.impl.ServiceLoaderConfigActionImpl;
import org.eclipse.transformer.action.impl.SignatureRuleImpl;
import org.eclipse.transformer.jakarta.JakartaTransformer;
import org.slf4j.impl.JDK14LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Wrapper that runs the Eclipse Transformer on every resource
 * @author Thiago Henrique Hupner
 */
public class EclipseTransformerResourceWrapper implements Resource {

    /**
     * Stores the invert flag
     */
    private static final boolean INVERT_RENAMES = Boolean.parseBoolean(System.getProperty("cloud.piranha.resource.transformer.eclipse.invert", "true"));

    /**
     * Stores the logger
     */
    private static final Logger LOGGER = Logger.getLogger(EclipseTransformerResourceWrapper.class.getPackageName());

    /**
     * Stores the SL4J logger
     */
    private static final org.slf4j.Logger SL4J_LOGGER = new JDK14LoggerFactory().getLogger(EclipseTransformerResourceWrapper.class.getPackageName());

    /**
     * Stores the wrapped resource
     */
    private final Resource resource;

    /**
     * Stores the transformer action
     */
    private ContainerActionImpl action;

    /**
     * Constructor
     *
     * @param resource the original resource to be wrapped
     */
    public EclipseTransformerResourceWrapper(Resource resource) {
        this.resource = Objects.requireNonNull(resource);
        try {
            action = new JarActionImpl(SL4J_LOGGER, false, false, new InputBufferImpl(),
                    new SelectionRuleImpl(SL4J_LOGGER, Collections.emptySet(), Collections.emptySet()),
                    new SignatureRuleImpl(SL4J_LOGGER, getRenames(), null, null, null, null, Collections.emptyMap()));
            action.addUsing(ClassActionImpl::new);
            action.addUsing(ServiceLoaderConfigActionImpl::new);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to create the transform action", e);
        }
    }

    /**
     * Loads the default renames from the Eclipse Transformer
     */
    private static Map<String, String> getRenames() throws IOException {
        String transformerResourceName = JakartaTransformer.class.getPackage()
                .getName()
                .replace('.', '/');

        String renamesResourceName = '/' + transformerResourceName + '/' + JakartaTransformer.DEFAULT_RENAMES_REFERENCE;

        try (InputStream resource = JakartaTransformer.class.getResourceAsStream(renamesResourceName);
             Reader renamesReader = new InputStreamReader(resource)) {

            Properties renameProperties = new Properties();
            renameProperties.load(renamesReader);
            Map<String, String> map = new HashMap<>();

            for (Map.Entry<Object, Object> entry : renameProperties.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                map.put(key.toString(), value.toString());
            }

            if (INVERT_RENAMES)
                map = TransformProperties.invert(map);

            return Collections.unmodifiableMap(map);
        }
    }

    @Override
    public URL getResource(String location) {
        return resource.getResource(location);
    }

    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream resourceAsStream = resource.getResourceAsStream(location);
        if (resourceAsStream == null) {
            return null;
        }

        if (action == null) {
            return resourceAsStream;
        }

        try {
            action.setInputBuffer(new byte[0]);

            ActionImpl acceptedAction = this.action.acceptAction(location);

            // Resource not accepted by any action
            if (acceptedAction == null) {
                return resourceAsStream;
            }

            return acceptedAction.apply(location, resourceAsStream).stream;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e, () -> "Occurred an error getting the resource " + location + " as stream");
            return null;
        }
    }

    @Override
    public Stream<String> getAllLocations() {
        return resource.getAllLocations();
    }
}

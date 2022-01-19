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
package cloud.piranha.test.arquillian;

import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApplicationBean {

    /**
     * Example showing injection of a default property
     */
    @Inject
    @ConfigProperty(name = "default.property", defaultValue = "Default Value")
    private String defaultProperty;

    /**
     * Example showing reading a config property from the META-INF/microprofile-config.properties file
     */
    @Inject
    @ConfigProperty(name = "file.property")
    private String fileProperty;

    /**
     * Example showing reading a config property from the META-INF/microprofile-config.properties file
     */
    @Inject
    @ConfigProperty(name = "application.property")
    private String applicationProperty;

    /**
     * Example injection of an optional value that's not required to be present
     */
    @Inject
    @ConfigProperty(name = "application.optionalProperty")
    private Optional<String> optionalApplicationProperty;

    /**
     *
     * @return defaultProperty
     */
    public String getDefaultProperty() {
        return defaultProperty;
    }

    /**
     *
     * @return fileProperty
     */
    public String getFileProperty() {
        return fileProperty;
    }

    /**
     *
     * @return applicationProperty
     */
    public String getApplicationProperty() {
        return applicationProperty;
    }

    /**
     *
     * @return optionalApplicationProperty
     */
    public Optional<String> getOptionalApplicationProperty() {
        return optionalApplicationProperty;
    }


}

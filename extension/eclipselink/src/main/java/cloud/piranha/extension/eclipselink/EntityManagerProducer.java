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
package cloud.piranha.extension.eclipselink;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceProperty;

/**
 * This producer takes care of providing a bean to inject for the <code>PersistenceContext</code> annotation.
 *
 * @author Arjan Tijms
 *
 */
public class EntityManagerProducer {

    /**
     *
     * @param injectionPoint the injectionPoint
     * @return EntityManager
     */
    @Produces
    public EntityManager produce(InjectionPoint injectionPoint) {
        PersistenceContext persistenceContext = injectionPoint.getAnnotated().getAnnotation(PersistenceContext.class);

        return new PiranhaEntityManager(
                    persistenceContext.unitName(),
                    persistenceContext.type(),
                    persistenceContext.synchronization(),
                    propertiesToMap(persistenceContext.properties()));

    }

    private Map<Object, Object> propertiesToMap(PersistenceProperty[] properties) {
        return Arrays.stream(properties)
                     .collect(toMap(
                        persistenceProperty -> persistenceProperty.name(),
                        persistenceProperty -> persistenceProperty.value()));
    }

}

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

import static jakarta.persistence.spi.PersistenceUnitTransactionType.JTA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.persistence.internal.jpa.deployment.JPAInitializer;
import org.eclipse.persistence.internal.jpa.deployment.PersistenceUnitProcessor;
import org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo;
import org.eclipse.persistence.jpa.Archive;
import org.eclipse.persistence.jpa.PersistenceProvider;

import cloud.piranha.core.api.AnnotationManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Converter;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.MappedSuperclass;

/**
* This bean takes care of creating an EntityManagerFactory using EclipseLink specific APIs.
*
* @author arjan
*
*/
@ApplicationScoped
public class EntityManagerFactoryCreator {

    /**
     * Stores previously created EntityManagerFactories per unit name.
     */
    private Map<String, EntityManagerFactory> entityManagerFactories = new ConcurrentHashMap<>();

    /**
     * The AnnotationManager used to lookup entity classes etc
     */
    private AnnotationManager annotationManager;

    /**
     * Gets the AnnotationManager used to lookup entity classes etc
     * @return
     */
    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    /**
     * Sets the AnnotationManager used to lookup entity classes etc
     * @param annotationManager
     */
    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    /**
     * Gets the <code>EntityManagerFactory</code> corresponding to the unit name. Created if needed.
     * @param unitName name of the persistence unit
     * @return <code>EntityManagerFactory</code> corresponding to the unit name
     */
    public EntityManagerFactory get(String unitName) {
        return entityManagerFactories.computeIfAbsent(unitName, e -> create(e));
    }

    private EntityManagerFactory create(String unitName) {
        Map<Object, Object> properties = new HashMap<>();

        // Use the EclipseLink provider directly, since we're specifically integrating EclipseLink here
        PersistenceProvider provider = new PersistenceProvider();

        JPAInitializer persistenceInitializer = provider.getInitializer(unitName, properties);

        // Get a persistence unit by name first, and if it can't be found see if there's a default one to obtain.
        // Note that persistence unit info essentially represents a parsed persistence.xml.
        SEPersistenceUnitInfo persistenceUnitInfo = persistenceInitializer.findPersistenceUnitInfo(unitName, properties);
        if (persistenceUnitInfo == null && "".equals(unitName)) {
            persistenceUnitInfo = getDefaultPersistenceUnit(persistenceInitializer, properties);
        }

        if (persistenceUnitInfo == null) {
            throw new IllegalStateException("No persistence unit found for [" + unitName + "]");
        }

        persistenceUnitInfo.setTransactionType(JTA);

        // SEPersistenceUnitInfo defaults to exclude unlisted true. We can't yet distinguish between the user setting
        // this and the class defaulting to it. For now scanned classes are always added.

        persistenceUnitInfo.getManagedClassNames().addAll(
            annotationManager.getAnnotations(Entity.class, Embeddable.class, Converter.class, MappedSuperclass.class)
                             .stream()
                             .map(e -> e.getTargetType().getName())
                             .toList());

        // Use GlassFish JNDI names for getting the transaction manager. Eventually this should
        // be standardised.
        properties.put("eclipselink.target-server", "Glassfish");

        EntityManagerFactory entityManagerFactory = provider.createContainerEntityManagerFactory(persistenceUnitInfo, properties);

        if (entityManagerFactory == null) {
            throw new IllegalStateException("Cannot create EntityManagerFactory with unitName " + unitName);
        }

        return entityManagerFactory;
    }

    private SEPersistenceUnitInfo getDefaultPersistenceUnit(JPAInitializer persistenceInitializer, Map<Object, Object> properties) {
        Set<String> persistenceUnitNames = getPersistenceUnitNames(persistenceInitializer);

        if (persistenceUnitNames.size() != 1) {
            return null;
        }

        String unitName = persistenceUnitNames.iterator().next();
        return persistenceInitializer.findPersistenceUnitInfo(unitName, properties);
    }


    private Set<String> getPersistenceUnitNames(JPAInitializer persistenceInitializer) {
        Set<String> persistenceUnitNames = new HashSet<>();

        Set<Archive> archives = PersistenceUnitProcessor.findPersistenceArchives(persistenceInitializer.getInitializationClassLoader());

        for (Archive archive : archives) {
            Iterator<SEPersistenceUnitInfo> persistenceUnits = PersistenceUnitProcessor.getPersistenceUnits(archive, persistenceInitializer.getInitializationClassLoader()).iterator();
            while (persistenceUnits.hasNext()) {
                SEPersistenceUnitInfo persistenceUnitInfoFromArchive = persistenceUnits.next();

                if (persistenceInitializer.isPersistenceProviderSupported(persistenceUnitInfoFromArchive.getPersistenceProviderClassName())) {
                    persistenceUnitNames.add(persistenceUnitInfoFromArchive.getPersistenceUnitName());
                }
            }
        }

        return persistenceUnitNames;
    }

}

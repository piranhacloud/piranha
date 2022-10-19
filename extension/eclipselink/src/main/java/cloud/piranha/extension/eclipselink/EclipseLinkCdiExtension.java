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

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.inject.spi.AnnotatedMember;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceContext;

/**
 * CDI Extension that among others makes <code>PersistenceContext</code> injectable via CDI.
 *
 * @author Arjan Tijms
 *
 */
public class EclipseLinkCdiExtension implements Extension {

    /**
     *
     * @param beforeBean
     * @param beanManager
     */
    public void register(@Observes BeforeBeanDiscovery beforeBean, BeanManager beanManager) {
        addAnnotatedTypes(beforeBean, beanManager,
            EntityManagerProducer.class,
            EntityManagerFactoryCreator.class,
            TxEntityManagerHolder.class,
            NonTxEntityManagerHolder.class);
    }

    /**
     *
     * @param <T>
     * @param pat
     */
    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        pat.configureAnnotatedType()
           .filterFields(e -> shouldInjectionAnnotationBeAdded(e))
           .forEach(e -> e.add(InjectLiteral.INSTANCE));

        pat.configureAnnotatedType()
            .filterMethods(m -> shouldInjectionAnnotationBeAdded(m))
            .forEach(m -> m.add(InjectLiteral.INSTANCE));
    }

    private static <X> boolean shouldInjectionAnnotationBeAdded(AnnotatedMember<? super X> field) {
        return !field.isAnnotationPresent(Inject.class) && field.isAnnotationPresent(PersistenceContext.class);
    }

    private static void addAnnotatedTypes(BeforeBeanDiscovery beforeBean, BeanManager beanManager, Class<?>... types) {
        for (Class<?> type : types) {
            beforeBean.addAnnotatedType(beanManager.createAnnotatedType(type), "EclipseLinkCdiExtension " + type.getName());
        }
    }

}

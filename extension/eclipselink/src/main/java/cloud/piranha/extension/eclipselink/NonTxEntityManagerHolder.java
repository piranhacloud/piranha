/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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

import java.io.Serializable;
import java.util.function.Supplier;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;

/**
 * Bean to store the entity manager during a request.
 *
 * <p>
 * This should always hold the non-transactional entity manager, as the
 * transactional one should be hold in a transaction scoped bean.
 *
 * @author Arjan Tijms
 *
 */
@RequestScoped
public class NonTxEntityManagerHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The entity manager that we store for the duration of a request
     */
    private transient EntityManager entityManager;

    /**
     * Gets the entity manager or computes and stores it if not yet available.
     *
     * @param entityManagerSupplier the supplier to get the entity manager from
     * @return the new or previously stored entity manager
     */
    public EntityManager computeIfAbsent(Supplier<EntityManager> entityManagerSupplier) {
        if (entityManager == null) {
            entityManager = entityManagerSupplier.get();
        }

        return entityManager;

    }

}

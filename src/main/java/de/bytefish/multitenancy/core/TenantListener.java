// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.core;

import de.bytefish.multitenancy.model.Tenant;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class TenantListener {

    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(TenantAware entity) {
        Tenant tenant = entity.getTenant();

        if(tenant == null) {
            tenant = new Tenant();

            entity.setTenant(tenant);
        }

        final String tenantName = ThreadLocalStorage.getTenantName();

        tenant.setTenantName(tenantName);
    }
}

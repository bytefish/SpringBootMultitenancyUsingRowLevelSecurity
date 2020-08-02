package de.bytefish.multitenancy.core;

import de.bytefish.multitenancy.model.Tenant;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

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

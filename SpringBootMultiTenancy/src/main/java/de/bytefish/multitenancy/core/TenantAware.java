package de.bytefish.multitenancy.core;

import de.bytefish.multitenancy.model.Tenant;

public interface TenantAware {

    Tenant getTenant();

    void setTenant(Tenant tenant);
}

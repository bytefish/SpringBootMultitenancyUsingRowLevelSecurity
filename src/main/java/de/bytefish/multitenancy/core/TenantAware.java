// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.core;

import de.bytefish.multitenancy.model.Tenant;

public interface TenantAware {

    Tenant getTenant();

    void setTenant(Tenant tenant);
}

// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application")
public class ApplicationConfiguration {

    private final List<TenantConfiguration> tenants;

    public ApplicationConfiguration(List<TenantConfiguration> tenants) {
        this.tenants = tenants;
    }

    public List<TenantConfiguration> getTenants() {
        return tenants;
    }
}

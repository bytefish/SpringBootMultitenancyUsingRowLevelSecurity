package de.bytefish.multitenancy.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Tenant {

    @Column(name = "tenant_name")
    private String tenantName;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}

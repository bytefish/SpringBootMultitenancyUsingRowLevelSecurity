// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.model;

import de.bytefish.multitenancy.core.TenantAware;
import de.bytefish.multitenancy.core.TenantListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(schema = "multitenant", name = "customer_address")
@EntityListeners(TenantListener.class)
public class CustomerAddress implements TenantAware {

    @Embeddable
    public static class Id implements Serializable {

        @Column(name = "customer_id", nullable = false)
        private Long customerId;

        @Column(name = "address_id", nullable = false)
        private Long addressId;

        private Id() {

        }

        public Id(Long customerId, Long addressId) {
            this.customerId = customerId;
            this.addressId = addressId;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public Long getAddressId() {
            return addressId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id that = (Id) o;
            return Objects.equals(customerId, that.customerId) &&
                    Objects.equals(addressId, that.addressId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, addressId);
        }
    }

    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address address;

    @Embedded
    private Tenant tenant;

    private CustomerAddress() {
    }

    public CustomerAddress(Customer customer, Address address) {
        this.id = new Id(customer.getId(), address.getId());
        this.customer = customer;
        this.address = address;
    }

    public Id getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
       this.tenant = tenant;
    }

}

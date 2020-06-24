// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.model;

import javax.persistence.*;

@Entity
@Table(schema = "sample", name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "tenant_name")
    private String tenantName;

    protected Customer() {
    }

    public Customer(Long id, String firstName, String lastName, String tenantName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tenantName = tenantName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTenantName() {
        return tenantName;
    }
}
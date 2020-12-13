// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.repositories;

import de.bytefish.multitenancy.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {

    @Async
    @Query("select c from Customer c left join fetch c.addresses a left join fetch a.address")
    CompletableFuture<List<Customer>> findAllAsync();

    // Do a JOIN FETCH to prevent Hibernate from doing N+1 Queries...
    @Query("select c from Customer c left join fetch c.addresses a left join fetch a.address where c.id = :id")
    Optional<Customer> findById(@Param("id") Long id);

    // Do a JOIN FETCH to prevent Hibernate from doing N+1 Queries...
    @Query("select c from Customer c left join fetch c.addresses a left join fetch a.address")
    List<Customer> findAll();
}

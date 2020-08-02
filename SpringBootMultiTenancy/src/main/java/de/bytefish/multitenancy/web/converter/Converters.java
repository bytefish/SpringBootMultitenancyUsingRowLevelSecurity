// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.web.converter;

import de.bytefish.multitenancy.model.Address;
import de.bytefish.multitenancy.model.Customer;
import de.bytefish.multitenancy.model.CustomerAddress;
import de.bytefish.multitenancy.web.model.AddressDto;
import de.bytefish.multitenancy.web.model.CustomerDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Converters {

    private Converters() {

    }

    public static CustomerDto convert(Customer source) {
        if(source == null) {
            return null;
        }

        List<AddressDto> addresses = null;

        if(source.getAddresses() != null) {
            addresses = source.getAddresses()
                    .stream()
                    .map(x -> convert(x.getAddress()))
                    .collect(Collectors.toList());
        }

        return new CustomerDto(source.getId(), source.getFirstName(), source.getLastName(), addresses);
    }

    public static AddressDto convert(Address source) {
        if(source == null) {
            return null;
        }

        return new AddressDto(source.getId(), source.getName(), source.getStreet(), source.getPostalcode(), source.getCity(), source.getCountry());
    }

    public static Customer convert(CustomerDto source) {
        if(source == null) {
            return null;
        }

        Customer customer = new Customer();

        customer.setFirstName(source.getFirstName());
        customer.setLastName(source.getLastName());

        return customer;
    }


    public static List<Address> convert(List<AddressDto> source) {

        if(source == null) {
            return null;
        }

        return source.stream()
                .map(x -> convert(x))
                .collect(Collectors.toList());
    }


    public static Address convert(AddressDto source) {

        if(source == null) {
            return null;
        }

        Address address = new Address();

        address.setId(source.getId());
        address.setName(source.getName());
        address.setStreet(source.getStreet());
        address.setPostalcode(source.getPostalcode());
        address.setCity(source.getCity());
        address.setCountry(source.getCountry());

        return address;
    }

    public static List<CustomerDto> convert(Iterable<Customer> customers) {
        return StreamSupport.stream(customers.spliterator(), false)
                .map(Converters::convert)
                .collect(Collectors.toList());
    }
}

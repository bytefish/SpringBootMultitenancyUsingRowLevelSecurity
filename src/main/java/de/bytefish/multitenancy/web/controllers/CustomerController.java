// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.multitenancy.web.controllers;

import de.bytefish.multitenancy.model.Address;
import de.bytefish.multitenancy.model.Customer;
import de.bytefish.multitenancy.model.CustomerAddress;
import de.bytefish.multitenancy.repositories.IAddressRepository;
import de.bytefish.multitenancy.repositories.ICustomerAddressRepository;
import de.bytefish.multitenancy.repositories.ICustomerRepository;
import de.bytefish.multitenancy.web.converter.Converters;
import de.bytefish.multitenancy.web.model.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    private final ICustomerRepository customerRepository;
    private final IAddressRepository addressRepository;
    private final ICustomerAddressRepository customerAddressRepository;

    @Autowired
    public CustomerController( IAddressRepository addressRepository, ICustomerRepository customerRepository, ICustomerAddressRepository customerAddressRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
    }

    @GetMapping("/customers")
    public List<CustomerDto> getAll() {
        Iterable<Customer> customers = customerRepository.findAll();

        return Converters.convert(customers);
    }

    @GetMapping("/customers/{id}")
    public CustomerDto get(@PathVariable("id") long id) {
        Customer customer = customerRepository
                .findById(id)
                .orElse(null);

        return Converters.convert(customer);
    }

    @GetMapping("/async/customers")
    public List<CustomerDto> getAllAsync() throws ExecutionException, InterruptedException {
        return customerRepository.findAllAsync()
                .thenApply(x -> Converters.convert(x))
                .get();
    }

    @PostMapping("/customers")
    public CustomerDto post(@RequestBody CustomerDto customerDto) {

        // Save the Customer:
        Customer customer = Converters.convert(customerDto);

        customerRepository.save(customer);

        // Create and Assign Addresses:
        if(customerDto.getAddresses() != null) {

            // First insert the Address:
            List<Address> addresses = Converters.convert(customerDto.getAddresses());

            addresses.forEach(addressRepository::save);

            // Then associate them:
            List<CustomerAddress> customerAddresses = addresses.stream()
                .map(address -> new CustomerAddress(customer, address))
                .collect(Collectors.toList());

            customerAddresses.forEach(customerAddressRepository::save);

            // And set in the Customer:
            customer.setAddresses(customerAddresses);
        }

        // Return the DTO:
        return Converters.convert(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void delete(@PathVariable("id") long id) {
        customerRepository.deleteById(id);
    }

}

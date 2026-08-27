package com.banking.repository;

import com.banking.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCustomerRepository
        implements CustomerRepository {

    private final Map<String, Customer> customers =
            new HashMap<>();

    @Override
    public void save(Customer customer) {

        customers.put(
                customer.getCustomerId(),
                customer
        );
    }

    @Override
    public Customer findById(String customerId) {

        return customers.get(customerId);
    }

    @Override
    public boolean exists(String customerId) {

        return customers.containsKey(customerId);
    }
}
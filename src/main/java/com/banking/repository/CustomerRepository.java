package com.banking.repository;

import com.banking.model.Customer;

public interface CustomerRepository {

    void save(Customer customer);

    Customer findById(String customerId);

    boolean exists(String customerId);
}
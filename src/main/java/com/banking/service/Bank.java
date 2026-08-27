package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private final Map<String, Customer> customers;
    private final Map<String, Account> accounts;

    public Bank() {
        customers = new HashMap<>();
        accounts = new HashMap<>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Customer findCustomer(String customerId) {
        return customers.get(customerId);
    }

    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}
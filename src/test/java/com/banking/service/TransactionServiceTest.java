package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;
import org.junit.jupiter.api.Test;

import com.banking.repository.InMemoryTransactionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionServiceTest {

    @Test
    void shouldTransferMoney() {

        Customer customer1 =
                new Customer(
                        "C001",
                        "Harsh",
                        "harsh@example.com"
                );

        Customer customer2 =
                new Customer(
                        "C002",
                        "Rahul",
                        "rahul@example.com"
                );

        Account from =
                new SavingsAccount(
                        "SA1001",
                        customer1
                );

        Account to =
                new SavingsAccount(
                        "SA1002",
                        customer2
                );

        TransactionService service =
                new TransactionService(
                        new InMemoryTransactionRepository()
                );

        service.deposit(from, 5000);

        service.transfer(
                from,
                to,
                1000
        );

        assertEquals(
                4000,
                from.getBalance()
        );

        assertEquals(
                1000,
                to.getBalance()
        );
    }

    @Test
    void shouldRecordTransactions() {

        Customer customer =
                new Customer(
                        "C001",
                        "Harsh",
                        "harsh@example.com"
                );

        Account account =
                new SavingsAccount(
                        "SA1001",
                        customer
                );

        TransactionService service =
                new TransactionService(
                        new InMemoryTransactionRepository()
                );

        service.deposit(account, 5000);
        service.withdraw(account, 1000);

        assertEquals(
                2,
                service.getTransactions("SA1001").size()
        );
    }
}
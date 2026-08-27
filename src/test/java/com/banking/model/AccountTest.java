package com.banking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.banking.exception.InsufficientBalanceException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void shouldDepositMoney() {

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

        account.deposit(5000);

        assertEquals(
                5000,
                account.getBalance()
        );
    }

    @Test
    void shouldWithdrawMoney() {

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

        account.deposit(5000);
        account.withdraw(1000);

        assertEquals(
                4000,
                account.getBalance()
        );
    }

    @Test
    void shouldRejectWithdrawalBelowMinimumBalance() {

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

        account.deposit(1000);

        assertThrows(
                InsufficientBalanceException.class,
                () -> account.withdraw(600)
        );
    }
}
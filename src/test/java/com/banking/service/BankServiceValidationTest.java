package com.banking.service;

import com.banking.exception.InvalidAmountException;
import com.banking.repository.InMemoryAccountRepository;
import com.banking.repository.InMemoryCustomerRepository;
import com.banking.repository.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServiceValidationTest {

    @Test
    void shouldRejectNegativeDeposit() {

        BankService bankService =
                createBankService();

        assertThrows(
                InvalidAmountException.class,
                () -> bankService.deposit(
                        "SA1001",
                        -100
                )
        );
    }

    @Test
    void shouldRejectZeroDeposit() {

        BankService bankService =
                createBankService();

        assertThrows(
                InvalidAmountException.class,
                () -> bankService.deposit(
                        "SA1001",
                        0
                )
        );
    }

    private BankService createBankService() {

        return new BankService(
                new InMemoryAccountRepository(),
                new InMemoryCustomerRepository(),
                new TransactionService(
                        new InMemoryTransactionRepository()
                )
        );
    }
}
package com.banking.repository;

import com.banking.model.Account;

public interface BankingTransactionRepository {

    void transfer(
            Account from,
            Account to,
            double amount
    );
}
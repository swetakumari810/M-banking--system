package com.banking.repository;

import com.banking.model.Account;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository
        implements AccountRepository {

    private final Map<String, Account> accounts =
            new HashMap<>();

    @Override
    public void save(Account account) {

        accounts.put(
                account.getAccountNumber(),
                account
        );
    }

    @Override
    public void update(Account account) {

        accounts.put(
                account.getAccountNumber(),
                account
        );
    }

    @Override
    public Account findByAccountNumber(
            String accountNumber) {

        return accounts.get(accountNumber);
    }

    @Override
    public boolean exists(String accountNumber) {

        return accounts.containsKey(accountNumber);
    }
}
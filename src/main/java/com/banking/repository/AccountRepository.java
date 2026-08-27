package com.banking.repository;

import com.banking.model.Account;

public interface AccountRepository {

    void save(Account account);

    void update(Account account);

    Account findByAccountNumber(String accountNumber);

    boolean exists(String accountNumber);
}
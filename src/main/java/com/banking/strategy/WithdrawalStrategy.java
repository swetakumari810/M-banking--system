package com.banking.strategy;

import com.banking.model.Account;

public interface WithdrawalStrategy {

    void withdraw(Account account, double amount);
}
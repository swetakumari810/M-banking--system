package com.banking.strategy;

import com.banking.model.Account;

import com.banking.exception.InsufficientBalanceException;

public class CurrentWithdrawalStrategy
        implements WithdrawalStrategy {

    private static final double OVERDRAFT_LIMIT = 5000.0;

    @Override
    public void withdraw(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        if (account.getBalance() - amount < -OVERDRAFT_LIMIT) {
            throw new InsufficientBalanceException(
                    "Overdraft limit exceeded"
            );
        }

        account.decreaseBalance(amount);
    }
}
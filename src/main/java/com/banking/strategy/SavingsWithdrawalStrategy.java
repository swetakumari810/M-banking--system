package com.banking.strategy;

import com.banking.model.Account;

import com.banking.exception.InsufficientBalanceException;

public class SavingsWithdrawalStrategy
        implements WithdrawalStrategy {

    private static final double MINIMUM_BALANCE = 500.0;

    @Override
    public void withdraw(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        if (account.getBalance() - amount < MINIMUM_BALANCE) {
            throw new InsufficientBalanceException(
                    "Minimum balance of ₹500 must be maintained"
            );
        }

        account.decreaseBalance(amount);
    }
}
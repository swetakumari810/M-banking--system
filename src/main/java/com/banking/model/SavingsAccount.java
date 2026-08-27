package com.banking.model;

import com.banking.strategy.SavingsWithdrawalStrategy;

public class SavingsAccount extends Account {

    public SavingsAccount(
            String accountNumber,
            Customer customer) {

        super(
                accountNumber,
                customer,
                new SavingsWithdrawalStrategy()
        );
    }

    @Override
    public AccountType getAccountType() {

        return AccountType.SAVINGS;
    }
}
package com.banking.model;

import com.banking.strategy.CurrentWithdrawalStrategy;

public class CurrentAccount extends Account {

    public CurrentAccount(
            String accountNumber,
            Customer customer) {

        super(
                accountNumber,
                customer,
                new CurrentWithdrawalStrategy()
        );
    }

    @Override
    public AccountType getAccountType() {

        return AccountType.CURRENT;
    }
}
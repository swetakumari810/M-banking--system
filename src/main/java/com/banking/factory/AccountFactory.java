package com.banking.factory;

import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.CurrentAccount;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;

public class AccountFactory {

    public static Account createAccount(
            AccountType type,
            String accountNumber,
            Customer customer) {

        return switch (type) {

            case SAVINGS ->
                    new SavingsAccount(
                            accountNumber,
                            customer
                    );

            case CURRENT ->
                    new CurrentAccount(
                            accountNumber,
                            customer
                    );
        };
    }
}
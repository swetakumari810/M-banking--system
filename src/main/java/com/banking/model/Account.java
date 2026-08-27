package com.banking.model;

import com.banking.strategy.WithdrawalStrategy;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Account {

    private final String accountNumber;
    private final Customer customer;

    protected double balance;

    private final WithdrawalStrategy withdrawalStrategy;

    private final ReentrantLock lock =
            new ReentrantLock();


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    protected Account(
            String accountNumber,
            Customer customer,
            WithdrawalStrategy withdrawalStrategy) {

        if (accountNumber == null ||
                accountNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Account number cannot be empty"
            );
        }

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (withdrawalStrategy == null) {

            throw new IllegalArgumentException(
                    "Withdrawal strategy cannot be null"
            );
        }

        this.accountNumber = accountNumber;
        this.customer = customer;
        this.withdrawalStrategy =
                withdrawalStrategy;

        this.balance = 0.0;
    }


    // ==========================================
    // GETTERS
    // ==========================================

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {

        lock.lock();

        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }


    // ==========================================
    // LOCK
    // ==========================================

    public ReentrantLock getLock() {
        return lock;
    }


    // ==========================================
    // DEPOSIT
    // ==========================================

    public void deposit(double amount) {

        if (amount <= 0) {

            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero"
            );
        }

        lock.lock();

        try {

            balance += amount;

        } finally {

            lock.unlock();
        }
    }


    // ==========================================
    // WITHDRAW
    // ==========================================

    public void withdraw(double amount) {

        if (amount <= 0) {

            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        /*
         * Delegate withdrawal rules to the
         * appropriate strategy.
         */
        withdrawalStrategy.withdraw(
                this,
                amount
        );
    }


    // ==========================================
    // INCREASE BALANCE
    // ==========================================

    public void increaseBalance(double amount) {

        lock.lock();

        try {

            balance += amount;

        } finally {

            lock.unlock();
        }
    }


    // ==========================================
    // DECREASE BALANCE
    // ==========================================

    public void decreaseBalance(double amount) {

        lock.lock();

        try {

            balance -= amount;

        } finally {

            lock.unlock();
        }
    }


    // ==========================================
    // RESTORE BALANCE
    // ==========================================

    public void restoreBalance(
            double newBalance) {

        lock.lock();

        try {

            balance = newBalance;

        } finally {

            lock.unlock();
        }
    }


    // ==========================================
    // ACCOUNT TYPE
    // ==========================================

    public abstract AccountType getAccountType();
}
package com.banking.service;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.CustomerNotFoundException;
import com.banking.factory.AccountFactory;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.Customer;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banking.exception.AccountAlreadyExistsException;
import com.banking.exception.CustomerAlreadyExistsException;
import com.banking.exception.InvalidAmountException;

public class BankService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;

    private static final Logger logger =
            LoggerFactory.getLogger(BankService.class);

    public BankService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            TransactionService transactionService) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionService = transactionService;
    }

    public void registerCustomer(Customer customer) {

        logger.info(
                "Registering customer: {}",
                customer.getCustomerId()
        );

        if (customerRepository.exists(
                customer.getCustomerId())) {

            logger.warn(
                    "Customer already exists: {}",
                    customer.getCustomerId()
            );

            throw new CustomerAlreadyExistsException(
                    "Customer already exists: "
                            + customer.getCustomerId()
            );
        }

        customerRepository.save(customer);

        logger.info(
                "Customer registered successfully: {}",
                customer.getCustomerId()
        );
    }

    public Account openAccount(
            AccountType type,
            String accountNumber,
            String customerId) {

        if (accountRepository.exists(accountNumber)) {
            throw new AccountAlreadyExistsException(
                    "Account already exists: " + accountNumber
            );
        }

        Customer customer =
                customerRepository.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found: "
                            + customerId
            );
        }

        Account account =
                AccountFactory.createAccount(
                        type,
                        accountNumber,
                        customer
                );

        accountRepository.save(account);

        return account;
    }

    public void deposit(
            String accountNumber,
            double amount) {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero"
            );
        }

        logger.info(
                "Deposit requested: account={}, amount={}",
                accountNumber,
                amount
        );

        Account account = getAccount(accountNumber);

        transactionService.deposit(
                account,
                amount
        );

        accountRepository.update(account);

        logger.info(
                "Deposit completed: account={}, amount={}",
                accountNumber,
                amount
        );
    }

    public void withdraw(
            String accountNumber,
            double amount) {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        logger.info(
                "Withdrawal requested: account={}, amount={}",
                accountNumber,
                amount
        );

        Account account = getAccount(accountNumber);

        transactionService.withdraw(
                account,
                amount
        );

        accountRepository.update(account);

        logger.info(
                "Withdrawal completed: account={}, amount={}",
                accountNumber,
                amount
        );
    }

    public void transfer(
            String fromAccountNumber,
            String toAccountNumber,
            double amount) {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Transfer amount must be greater than zero"
            );
        }

        logger.info(
                "Transfer requested: {} -> {}, amount={}",
                fromAccountNumber,
                toAccountNumber,
                amount
        );

        Account from =
                getAccount(fromAccountNumber);

        Account to =
                getAccount(toAccountNumber);

        transactionService.transfer(
                from,
                to,
                amount
        );

        logger.info(
                "Transfer completed: {} -> {}, amount={}",
                fromAccountNumber,
                toAccountNumber,
                amount
        );
    }

    public Account getAccount(String accountNumber) {

        Account account =
                accountRepository.findByAccountNumber(
                        accountNumber
                );

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: "
                            + accountNumber
            );
        }

        return accountRepository.findByAccountNumber(
                accountNumber
        );
    }

    public Customer getCustomer(String customerId) {

        Customer customer =
                customerRepository.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(
                    "Customer not found: " + customerId
            );
        }

        return customer;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }
}
package com.banking.config;

import com.banking.repository.AccountRepository;
import com.banking.repository.BankingTransactionRepository;
import com.banking.repository.CustomerRepository;
import com.banking.repository.JdbcAccountRepository;
import com.banking.repository.JdbcBankingTransactionRepository;
import com.banking.repository.JdbcCustomerRepository;
import com.banking.repository.JdbcTransactionRepository;
import com.banking.repository.TransactionRepository;
import com.banking.service.BankService;
import com.banking.service.TransactionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankingConfig {

    // ==========================================
    // CUSTOMER REPOSITORY
    // ==========================================

    @Bean
    public CustomerRepository customerRepository() {

        return new JdbcCustomerRepository();
    }


    // ==========================================
    // ACCOUNT REPOSITORY
    // ==========================================

    @Bean
    public AccountRepository accountRepository(
            CustomerRepository customerRepository) {

        return new JdbcAccountRepository(
                customerRepository
        );
    }


    // ==========================================
    // TRANSACTION REPOSITORY
    // ==========================================

    @Bean
    public TransactionRepository transactionRepository() {

        return new JdbcTransactionRepository();
    }


    // ==========================================
    // BANKING TRANSACTION REPOSITORY
    // ==========================================

    @Bean
    public BankingTransactionRepository
    bankingTransactionRepository() {

        return new JdbcBankingTransactionRepository();
    }


    // ==========================================
    // TRANSACTION SERVICE
    // ==========================================

    @Bean
    public TransactionService transactionService(
            TransactionRepository transactionRepository,
            BankingTransactionRepository
                    bankingTransactionRepository) {

        return new TransactionService(
                transactionRepository,
                bankingTransactionRepository
        );
    }


    // ==========================================
    // BANK SERVICE
    // ==========================================

    @Bean
    public BankService bankService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            TransactionService transactionService) {

        return new BankService(
                accountRepository,
                customerRepository,
                transactionService
        );
    }
}
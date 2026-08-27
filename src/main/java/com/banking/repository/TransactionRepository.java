package com.banking.repository;

import com.banking.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Transaction> findByAccountNumber(String accountNumber);
}
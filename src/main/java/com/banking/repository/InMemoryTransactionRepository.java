package com.banking.repository;

import com.banking.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository
        implements TransactionRepository {

    private final List<Transaction> transactions =
            new ArrayList<>();

    @Override
    public void save(Transaction transaction) {

        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(
            String accountNumber) {

        return transactions.stream()
                .filter(transaction ->
                        accountNumber.equals(
                                transaction.getSourceAccount()
                        )
                                ||
                                accountNumber.equals(
                                        transaction.getDestinationAccount()
                                )
                )
                .toList();
    }
}
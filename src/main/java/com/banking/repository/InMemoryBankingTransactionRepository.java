package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

public class InMemoryBankingTransactionRepository
        implements BankingTransactionRepository {

    private final TransactionRepository transactionRepository;

    public InMemoryBankingTransactionRepository(
            TransactionRepository transactionRepository) {

        this.transactionRepository =
                transactionRepository;
    }

    @Override
    public void transfer(
            Account from,
            Account to,
            double amount) {

        // Perform the actual in-memory transfer
        from.withdraw(amount);
        to.deposit(amount);

        // Record the transaction
        Transaction transaction =
                new Transaction(
                        TransactionType.TRANSFER,
                        from.getAccountNumber(),
                        to.getAccountNumber(),
                        amount
                );

        transactionRepository.save(transaction);
    }
}
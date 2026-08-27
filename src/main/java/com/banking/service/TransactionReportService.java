package com.banking.service;

import com.banking.model.Transaction;
import com.banking.model.TransactionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionReportService {

    public double getTotalDeposits(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(transaction ->
                        transaction.getType()
                                == TransactionType.DEPOSIT)
                .mapToDouble(
                        Transaction::getAmount
                )
                .sum();
    }

    public double getTotalWithdrawals(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(transaction ->
                        transaction.getType()
                                == TransactionType.WITHDRAWAL)
                .mapToDouble(
                        Transaction::getAmount
                )
                .sum();
    }

    public double getTotalTransfers(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(transaction ->
                        transaction.getType()
                                == TransactionType.TRANSFER)
                .mapToDouble(
                        Transaction::getAmount
                )
                .sum();
    }

    public Map<TransactionType, Long>
    countTransactionsByType(
            List<Transaction> transactions) {

        return transactions.stream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::getType,
                                Collectors.counting()
                        )
                );
    }
}
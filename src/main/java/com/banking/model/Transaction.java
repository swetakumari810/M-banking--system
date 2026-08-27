package com.banking.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private final TransactionType type;
    private final String sourceAccount;
    private final String destinationAccount;
    private final double amount;
    private final LocalDateTime timestamp;

    // Constructor for deposit/withdrawal
    public Transaction(
            TransactionType type,
            String accountNumber,
            double amount) {

        this(
                type,
                accountNumber,
                null,
                amount
        );
    }

    // Constructor for transfer
    public Transaction(
            TransactionType type,
            String sourceAccount,
            String destinationAccount,
            double amount) {

        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(
            String transactionId,
            TransactionType type,
            String sourceAccount,
            String destinationAccount,
            double amount,
            LocalDateTime timestamp) {

        this.transactionId = transactionId;
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {

        if (type == TransactionType.TRANSFER) {
            return "Transaction{" +
                    "id='" + transactionId + '\'' +
                    ", type=" + type +
                    ", from='" + sourceAccount + '\'' +
                    ", to='" + destinationAccount + '\'' +
                    ", amount=" + amount +
                    ", timestamp=" + timestamp +
                    '}';
        }

        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", type=" + type +
                ", account='" + sourceAccount + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
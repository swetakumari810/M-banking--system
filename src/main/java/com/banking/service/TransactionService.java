package com.banking.service;

import com.banking.exception.InvalidAmountException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import com.banking.repository.BankingTransactionRepository;
import com.banking.repository.InMemoryBankingTransactionRepository;
import com.banking.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


//@Service
public class TransactionService {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final BankingTransactionRepository
            bankingTransactionRepository;


    // Production constructor
    public TransactionService(
            TransactionRepository transactionRepository,
            BankingTransactionRepository bankingTransactionRepository) {

        this.transactionRepository =
                transactionRepository;

        this.bankingTransactionRepository =
                bankingTransactionRepository;
    }


    // Constructor used by unit tests
    public TransactionService(
            TransactionRepository transactionRepository) {

        this.transactionRepository =
                transactionRepository;

        this.bankingTransactionRepository =
                new InMemoryBankingTransactionRepository(
                        transactionRepository
                );
    }


    // =========================
    // DEPOSIT
    // =========================

    public void deposit(
            Account account,
            double amount) {

        if (amount <= 0) {

            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero"
            );
        }

        logger.info(
                "Processing deposit: account={}, amount={}",
                account.getAccountNumber(),
                amount
        );

        account.deposit(amount);

        Transaction transaction =
                new Transaction(
                        TransactionType.DEPOSIT,
                        null,
                        account.getAccountNumber(),
                        amount
                );

        transactionRepository.save(transaction);

        logger.info(
                "Deposit completed: account={}, amount={}",
                account.getAccountNumber(),
                amount
        );
    }


    // =========================
    // WITHDRAW
    // =========================

    public void withdraw(
            Account account,
            double amount) {

        if (amount <= 0) {

            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        logger.info(
                "Processing withdrawal: account={}, amount={}",
                account.getAccountNumber(),
                amount
        );

        account.withdraw(amount);

        Transaction transaction =
                new Transaction(
                        TransactionType.WITHDRAWAL,
                        account.getAccountNumber(),
                        null,
                        amount
                );

        transactionRepository.save(transaction);

        logger.info(
                "Withdrawal completed: account={}, amount={}",
                account.getAccountNumber(),
                amount
        );
    }


    // =========================
    // TRANSFER
    // =========================

    public void transfer(
            Account from,
            Account to,
            double amount) {

        logger.info(
                "Processing transfer: {} -> {}, amount={}",
                from.getAccountNumber(),
                to.getAccountNumber(),
                amount
        );

        if (from == to) {

            throw new IllegalArgumentException(
                    "Source and destination accounts cannot be the same"
            );
        }

        if (amount <= 0) {

            throw new InvalidAmountException(
                    "Transfer amount must be greater than zero"
            );
        }

        /*
         * Always acquire locks in the same order.
         * This prevents deadlock when two transfers
         * happen in opposite directions.
         */
        Account firstLock;
        Account secondLock;

        if (from.getAccountNumber()
                .compareTo(to.getAccountNumber()) < 0) {

            firstLock = from;
            secondLock = to;

        } else {

            firstLock = to;
            secondLock = from;
        }


        firstLock.getLock().lock();

        try {

            secondLock.getLock().lock();

            try {

                /*
                 * The repository is responsible for the
                 * complete transfer operation.
                 *
                 * JDBC implementation:
                 * PostgreSQL transaction + FOR UPDATE
                 *
                 * In-memory implementation:
                 * updates Account objects directly
                 */
                bankingTransactionRepository.transfer(
                        from,
                        to,
                        amount
                );

                logger.info(
                        "Transfer completed: {} -> {}, amount={}",
                        from.getAccountNumber(),
                        to.getAccountNumber(),
                        amount
                );

            } catch (Exception e) {

                logger.error(
                        "Transfer failed: {} -> {}, amount={}",
                        from.getAccountNumber(),
                        to.getAccountNumber(),
                        amount,
                        e
                );

                throw e;

            } finally {

                secondLock.getLock().unlock();
            }

        } finally {

            firstLock.getLock().unlock();
        }
    }


    // =========================
    // GET TRANSACTIONS
    // =========================

    public List<Transaction> getTransactions(
            String accountNumber) {

        return transactionRepository
                .findByAccountNumber(accountNumber);
    }
}
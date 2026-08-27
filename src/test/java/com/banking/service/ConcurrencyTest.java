package com.banking.service;

import com.banking.database.DatabaseConnection;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;
import com.banking.repository.JdbcAccountRepository;
import com.banking.repository.JdbcBankingTransactionRepository;
import com.banking.repository.JdbcCustomerRepository;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcurrencyTest {

    @Test
    void testConcurrentTransfers() throws Exception {

        JdbcCustomerRepository customerRepository =
                new JdbcCustomerRepository();

        JdbcAccountRepository accountRepository =
                new JdbcAccountRepository(
                        customerRepository
                );

        JdbcBankingTransactionRepository
                transactionRepository =
                new JdbcBankingTransactionRepository();

        String customerId1 = "CONC_CUST_1";
        String customerId2 = "CONC_CUST_2";

        String accountNumber1 = "CONC1001";
        String accountNumber2 = "CONC1002";

        /*
         * Clean previous test data.
         */
        cleanDatabase(
                customerId1,
                customerId2,
                accountNumber1,
                accountNumber2
        );

        /*
         * Create customers.
         */
        Customer customer1 =
                new Customer(
                        customerId1,
                        "Concurrency User 1",
                        "concurrency1@example.com"
                );

        Customer customer2 =
                new Customer(
                        customerId2,
                        "Concurrency User 2",
                        "concurrency2@example.com"
                );

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        /*
         * Create accounts.
         */
        Account account1 =
                new SavingsAccount(
                        accountNumber1,
                        customer1
                );

        Account account2 =
                new SavingsAccount(
                        accountNumber2,
                        customer2
                );

        /*
         * Initial balances.
         */
        account1.restoreBalance(10000);
        account2.restoreBalance(10000);

        accountRepository.save(account1);
        accountRepository.save(account2);

        /*
         * Number of concurrent transfers.
         */
        int numberOfTransfers = 10;

        /*
         * Each transfer = ₹500.
         */
        double transferAmount = 500;

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        numberOfTransfers
                );

        /*
         * Makes all threads start approximately
         * at the same time.
         */
        CountDownLatch startLatch =
                new CountDownLatch(1);

        /*
         * Store possible exceptions.
         */
        List<Throwable> errors =
                new ArrayList<>();

        /*
         * Submit concurrent transfers.
         */
        for (int i = 0;
             i < numberOfTransfers;
             i++) {

            executor.submit(() -> {

                try {

                    /*
                     * Wait until all threads are ready.
                     */
                    startLatch.await();

                    /*
                     * Transfer:
                     *
                     * CONC1001
                     *      ↓ ₹500
                     * CONC1002
                     */
                    transactionRepository.transfer(
                            account1,
                            account2,
                            transferAmount
                    );

                } catch (Throwable e) {

                    synchronized (errors) {
                        errors.add(e);
                    }
                }
            });
        }

        /*
         * Start all workers.
         */
        startLatch.countDown();

        /*
         * Stop accepting new tasks.
         */
        executor.shutdown();

        /*
         * Wait for all transfers.
         */
        boolean finished =
                executor.awaitTermination(
                        30,
                        TimeUnit.SECONDS
                );

        /*
         * Make sure all threads finished.
         */
        assertEquals(
                true,
                finished,
                "Concurrent transfers did not finish"
        );

        /*
         * Make sure no transfer failed.
         */
        assertEquals(
                0,
                errors.size(),
                "One or more concurrent transfers failed: "
                        + errors
        );

        /*
         * Expected result:
         *
         * Account 1:
         *
         * 10000 - (10 × 500)
         * = 5000
         *
         * Account 2:
         *
         * 10000 + (10 × 500)
         * = 15000
         */
        Account finalAccount1 =
                accountRepository.findByAccountNumber(
                        accountNumber1
                );

        Account finalAccount2 =
                accountRepository.findByAccountNumber(
                        accountNumber2
                );

        assertEquals(
                5000,
                finalAccount1.getBalance(),
                0.001
        );

        assertEquals(
                15000,
                finalAccount2.getBalance(),
                0.001
        );

        /*
         * Cleanup.
         */
        cleanDatabase(
                customerId1,
                customerId2,
                accountNumber1,
                accountNumber2
        );
    }


    private void cleanDatabase(
            String customerId1,
            String customerId2,
            String accountNumber1,
            String accountNumber2)
            throws Exception {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            /*
             * Delete transactions first because
             * transactions may reference accounts.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 """
                                 DELETE FROM transactions
                                 WHERE source_account IN (?, ?)
                                    OR destination_account IN (?, ?)
                                 """
                         )) {

                statement.setString(
                        1,
                        accountNumber1
                );

                statement.setString(
                        2,
                        accountNumber2
                );

                statement.setString(
                        3,
                        accountNumber1
                );

                statement.setString(
                        4,
                        accountNumber2
                );

                statement.executeUpdate();
            }

            /*
             * Delete accounts.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 """
                                 DELETE FROM accounts
                                 WHERE account_number IN (?, ?)
                                 """
                         )) {

                statement.setString(
                        1,
                        accountNumber1
                );

                statement.setString(
                        2,
                        accountNumber2
                );

                statement.executeUpdate();
            }

            /*
             * Delete customers.
             */
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 """
                                 DELETE FROM customers
                                 WHERE customer_id IN (?, ?)
                                 """
                         )) {

                statement.setString(
                        1,
                        customerId1
                );

                statement.setString(
                        2,
                        customerId2
                );

                statement.executeUpdate();
            }
        }
    }
}
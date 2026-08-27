package com.banking.repository;

import com.banking.database.DatabaseConnection;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcBankingTransactionRepository
        implements BankingTransactionRepository {

    @Override
    public void transfer(
            Account from,
            Account to,
            double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero"
            );
        }

        if (from.getAccountNumber()
                .equals(to.getAccountNumber())) {

            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            /*
             * Lock source account row.
             *
             * FOR UPDATE prevents another transaction
             * from modifying this account until this
             * transaction finishes.
             */
            String firstAccount;
            String secondAccount;

            if (from.getAccountNumber()
                    .compareTo(to.getAccountNumber()) < 0) {

                firstAccount = from.getAccountNumber();
                secondAccount = to.getAccountNumber();

            } else {

                firstAccount = to.getAccountNumber();
                secondAccount = from.getAccountNumber();
            }

            AccountBalance first =
                    getAccountForUpdate(
                            connection,
                            firstAccount
                    );

            AccountBalance second =
                    getAccountForUpdate(
                            connection,
                            secondAccount
                    );

            double sourceBalance;

            double destinationBalance;

            if (from.getAccountNumber()
                    .equals(firstAccount)) {

                sourceBalance = first.balance;
                destinationBalance = second.balance;

            } else {

                sourceBalance = second.balance;
                destinationBalance = first.balance;
            }

            /*
             * Check balance while the source row is locked.
             */
            if (sourceBalance < amount) {

                throw new IllegalStateException(
                        "Insufficient balance. Available: "
                                + sourceBalance
                );
            }

            /*
             * Update source balance.
             */
            updateBalance(
                    connection,
                    from.getAccountNumber(),
                    sourceBalance - amount
            );

            /*
             * Update destination balance.
             */
            updateBalance(
                    connection,
                    to.getAccountNumber(),
                    destinationBalance + amount
            );

            /*
             * Save transaction history.
             */
            saveTransaction(
                    connection,
                    from,
                    to,
                    amount
            );

            /*
             * Everything succeeded.
             */
            connection.commit();

        } catch (Exception e) {

            if (connection != null) {

                try {
                    connection.rollback();

                } catch (SQLException rollbackException) {

                    rollbackException.printStackTrace();
                }
            }

            throw new RuntimeException(
                    "Transfer failed. Transaction rolled back.",
                    e
            );

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);
                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    /*
     * Locks the account row using PostgreSQL FOR UPDATE.
     */
    private AccountBalance getAccountForUpdate(
            Connection connection,
            String accountNumber)
            throws SQLException {

        String sql = """
                SELECT balance
                FROM accounts
                WHERE account_number = ?
                FOR UPDATE
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    accountNumber
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new IllegalArgumentException(
                            "Account not found: "
                                    + accountNumber
                    );
                }

                return new AccountBalance(
                        resultSet.getDouble("balance")
                );
            }
        }
    }


    /*
     * Updates the account balance.
     */
    private void updateBalance(
            Connection connection,
            String accountNumber,
            double balance)
            throws SQLException {

        String sql = """
                UPDATE accounts
                SET balance = ?
                WHERE account_number = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDouble(
                    1,
                    balance
            );

            statement.setString(
                    2,
                    accountNumber
            );

            int rows =
                    statement.executeUpdate();

            if (rows != 1) {

                throw new SQLException(
                        "Failed to update account: "
                                + accountNumber
                );
            }
        }
    }


    /*
     * Inserts transaction history.
     */
    private void saveTransaction(
            Connection connection,
            Account from,
            Account to,
            double amount)
            throws SQLException {

        String sql = """
                INSERT INTO transactions
                (
                    transaction_id,
                    transaction_type,
                    source_account,
                    destination_account,
                    amount,
                    transaction_time
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Transaction transaction =
                new Transaction(
                        TransactionType.TRANSFER,
                        from.getAccountNumber(),
                        to.getAccountNumber(),
                        amount
                );

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    transaction.getTransactionId()
            );

            statement.setString(
                    2,
                    transaction.getType().name()
            );

            statement.setString(
                    3,
                    transaction.getSourceAccount()
            );

            statement.setString(
                    4,
                    transaction.getDestinationAccount()
            );

            statement.setDouble(
                    5,
                    transaction.getAmount()
            );

            statement.setObject(
                    6,
                    transaction.getTimestamp()
            );

            statement.executeUpdate();
        }
    }


    /*
     * Small internal object containing
     * the current database balance.
     */
    private static class AccountBalance {

        private final double balance;

        private AccountBalance(double balance) {
            this.balance = balance;
        }
    }
}
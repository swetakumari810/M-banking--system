package com.banking.repository;

import com.banking.database.DatabaseConnection;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionRepository
        implements TransactionRepository {

    @Override
    public void save(Transaction transaction) {

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

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

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

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save transaction",
                    e
            );
        }
    }

    @Override
    public List<Transaction> findByAccountNumber(
            String accountNumber) {

        String sql = """
                SELECT
                    transaction_id,
                    transaction_type,
                    source_account,
                    destination_account,
                    amount,
                    transaction_time
                FROM transactions
                WHERE source_account = ?
                   OR destination_account = ?
                ORDER BY transaction_time DESC
                """;

        List<Transaction> transactions =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, accountNumber);
            statement.setString(2, accountNumber);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Transaction transaction =
                            new Transaction(
                                    resultSet.getString(
                                            "transaction_id"
                                    ),

                                    TransactionType.valueOf(
                                            resultSet.getString(
                                                    "transaction_type"
                                            )
                                    ),

                                    resultSet.getString(
                                            "source_account"
                                    ),

                                    resultSet.getString(
                                            "destination_account"
                                    ),

                                    resultSet.getDouble(
                                            "amount"
                                    ),

                                    resultSet.getTimestamp(
                                            "transaction_time"
                                    ).toLocalDateTime()
                            );

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve transactions",
                    e
            );
        }

        return transactions;
    }
}
package com.banking.repository;

import com.banking.database.DatabaseConnection;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.model.CurrentAccount;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAccountRepository
        implements AccountRepository {

    private final CustomerRepository customerRepository;

    public JdbcAccountRepository(
            CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    @Override
    public void save(Account account) {

        String sql = """
                INSERT INTO accounts
                (account_number, customer_id, account_type, balance)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    account.getAccountNumber()
            );

            statement.setString(
                    2,
                    account.getCustomer().getCustomerId()
            );

            statement.setString(
                    3,
                    getAccountType(account)
            );

            statement.setDouble(
                    4,
                    account.getBalance()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save account",
                    e
            );
        }
    }

    @Override
    public Account findByAccountNumber(
            String accountNumber) {

        String sql = """
                SELECT account_number,
                       customer_id,
                       account_type,
                       balance
                FROM accounts
                WHERE account_number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    String customerId =
                            resultSet.getString("customer_id");

                    Customer customer =
                            customerRepository.findById(
                                    customerId
                            );

                    if (customer == null) {
                        throw new RuntimeException(
                                "Customer not found: "
                                        + customerId
                        );
                    }

                    AccountType accountType =
                            AccountType.valueOf(
                                    resultSet.getString(
                                            "account_type"
                                    )
                            );

                    Account account =
                            createAccount(
                                    accountType,
                                    resultSet.getString(
                                            "account_number"
                                    ),
                                    customer
                            );

                    /*
                     * Restore balance from database.
                     */
                    double balance =
                            resultSet.getDouble("balance");

                    account.restoreBalance(balance);

                    return account;
                }

            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find account",
                    e
            );
        }

        return null;
    }

    @Override
    public boolean exists(String accountNumber) {

        String sql = """
                SELECT 1
                FROM accounts
                WHERE account_number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to check account",
                    e
            );
        }
    }

    private String getAccountType(Account account) {

        if (account instanceof SavingsAccount) {
            return "SAVINGS";
        }

        if (account instanceof CurrentAccount) {
            return "CURRENT";
        }

        throw new IllegalArgumentException(
                "Unknown account type"
        );
    }

    private Account createAccount(
            AccountType type,
            String accountNumber,
            Customer customer) {

        return switch (type) {

            case SAVINGS ->
                    new SavingsAccount(
                            accountNumber,
                            customer
                    );

            case CURRENT ->
                    new CurrentAccount(
                            accountNumber,
                            customer
                    );
        };
    }

    @Override
    public void update(Account account) {

        String sql = """
            UPDATE accounts
            SET balance = ?
            WHERE account_number = ?
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDouble(
                    1,
                    account.getBalance()
            );

            statement.setString(
                    2,
                    account.getAccountNumber()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update account",
                    e
            );
        }
    }
}
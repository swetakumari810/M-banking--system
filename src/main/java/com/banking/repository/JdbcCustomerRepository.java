package com.banking.repository;

import com.banking.database.DatabaseConnection;
import com.banking.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCustomerRepository
        implements CustomerRepository {

    @Override
    public void save(Customer customer) {

        String sql = """
                INSERT INTO customers
                (customer_id, name, email)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    customer.getCustomerId()
            );

            statement.setString(
                    2,
                    customer.getName()
            );

            statement.setString(
                    3,
                    customer.getEmail()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save customer",
                    e
            );
        }
    }

    @Override
    public Customer findById(String customerId) {

        String sql = """
                SELECT customer_id, name, email
                FROM customers
                WHERE customer_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, customerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Customer(
                            resultSet.getString(
                                    "customer_id"
                            ),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find customer",
                    e
            );
        }

        return null;
    }

    @Override
    public boolean exists(String customerId) {

        return findById(customerId) != null;
    }
}
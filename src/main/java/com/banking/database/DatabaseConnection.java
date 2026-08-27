//package com.banking.database;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnection {
//
//    private static final String URL =
//            getRequiredEnvironmentVariable(
//                    "DB_URL"
//            );
//
//    private static final String USER =
//            getRequiredEnvironmentVariable(
//                    "DB_USER"
//            );
//
//    private static final String PASSWORD =
//            getRequiredEnvironmentVariable(
//                    "DB_PASSWORD"
//            );
//
//    private DatabaseConnection() {
//    }
//
//    public static Connection getConnection()
//            throws SQLException {
//
//        return DriverManager.getConnection(
//                URL,
//                USER,
//                PASSWORD
//        );
//    }
//
//    private static String getRequiredEnvironmentVariable(
//            String name) {
//
//        String value = System.getenv(name);
//
//        if (value == null || value.isBlank()) {
//
//            throw new IllegalStateException(
//                    "Environment variable '" +
//                            name +
//                            "' is not configured"
//            );
//        }
//
//        return value;
//    }
//}


package com.banking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            getRequiredConfiguration("DB_URL");

    private static final String USER =
            getRequiredConfiguration("DB_USER");

    private static final String PASSWORD =
            getRequiredConfiguration("DB_PASSWORD");

    private DatabaseConnection() {
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    private static String getConfiguration(
            String name,
            String defaultValue) {

        // 1. Check Java system property
        String systemProperty =
                System.getProperty(name);

        if (systemProperty != null
                && !systemProperty.isBlank()) {

            return systemProperty;
        }

        // 2. Check environment variable
        String environmentVariable =
                System.getenv(name);

        if (environmentVariable != null
                && !environmentVariable.isBlank()) {

            return environmentVariable;
        }

        // 3. Use default
        return defaultValue;
    }

    private static String getRequiredConfiguration(
            String name) {

        String systemProperty =
                System.getProperty(name);

        if (systemProperty != null
                && !systemProperty.isBlank()) {

            return systemProperty;
        }

        String environmentVariable =
                System.getenv(name);

        if (environmentVariable != null
                && !environmentVariable.isBlank()) {

            return environmentVariable;
        }

        throw new IllegalStateException(
                "Configuration '" + name
                        + "' is not configured"
        );
    }
}
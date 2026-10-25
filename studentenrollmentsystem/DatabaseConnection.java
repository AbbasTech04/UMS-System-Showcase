package project1.studentenrollmentsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides centralized access to the MySQL database used by the
 * University Management System.
 *
 * <p>This utility class stores the database connection configuration
 * and exposes a static method for establishing database connections.
 * It cannot be instantiated.</p>
 *
 * <p>Database configuration includes the JDBC URL, username, and
 * password. These values should be externalized for production
 * deployments rather than hardcoded in the source code.</p>
 *
 * @author Abbas Mohammed
 * @version 1.0
 * @since 1.0
 */
public class DatabaseConnection {

    private static final Logger LOGGER =
            Logger.getLogger(DatabaseConnection.class.getName());

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_enrollment_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "mysql1";
    private static final String PASSWORD = "sql0123";

    /**
     * Attempts to load the MySQL JDBC driver when this class is initialized.
     *
     * <p>If the driver class cannot be found, the error is logged.
     * Subsequent connection attempts may fail if the driver is unavailable.</p>
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "MySQL JDBC driver not found. Ensure that MySQL Connector/J "
                            + "is included in the project dependencies.",
                    e
            );
        }
    }

    /**
     * Prevents instantiation of this utility class.
     */
    private DatabaseConnection() {
        // Utility class; instantiation is not supported.
    }

    /**
     * Establishes a new connection to the configured MySQL database.
     *
     * <p>The caller is responsible for closing the returned connection
     * after use, preferably with a try-with-resources statement.</p>
     *
     * @return an open connection to the configured database
     * @throws SQLException if a database connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

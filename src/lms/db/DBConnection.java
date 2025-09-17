package lms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database file will be created in project root directory
    private static final String DB_PATH = System.getProperty("user.dir") + "/library.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    static {
        try {
            // Load driver (optional in modern JDBC, but good for clarity)
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Add sqlite-jdbc.jar to your classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Expose the DB path for printing/logging
    public static String getDbPath() {
        return DB_PATH;
    }
}

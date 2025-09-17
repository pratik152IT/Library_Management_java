package lms.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBInit {
    public static void init() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create tables
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT DEFAULT 'MEMBER'" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "author TEXT," +
                    "available INTEGER DEFAULT 1" +
                    ");");

            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "book_id INTEGER," +
                    "issue_date TEXT," +   // ISO date format
                    "due_date TEXT," +
                    "return_date TEXT," +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id)," +
                    "FOREIGN KEY(book_id) REFERENCES books(book_id)" +
                    ");");

            // Create a default admin if not exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM users WHERE role='ADMIN';");
            int cnt = 0;
            if (rs.next()) cnt = rs.getInt("cnt");
            if (cnt == 0) {
                stmt.execute("INSERT INTO users(username,password,role) VALUES('admin','admin123','ADMIN');");
                System.out.println("Default admin created: username=admin password=admin123");
            }

           
            System.out.println("Database initialized at: " + DBConnection.getDbPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

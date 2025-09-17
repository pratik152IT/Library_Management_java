package lms.services;

import lms.db.DBConnection;
import java.sql.*;
import java.time.LocalDate;

public class TransactionService {

    // issue a book
    public void issueBook(int userId, int bookId) {
        try (Connection conn = DBConnection.getConnection()) {
            // mark book as borrowed
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions(user_id, book_id, issue_date, due_date, returned) VALUES(?,?,?,?,0)"
            );
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setString(3, LocalDate.now().toString());
            ps.setString(4, LocalDate.now().plusDays(14).toString()); // 2 weeks due
            ps.executeUpdate();
            System.out.println("Book issued!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // return a book
    public void returnBook(int bookId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE transactions SET returned=1 WHERE book_id=? AND returned=0"
            );
            ps.setInt(1, bookId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book returned!");
            } else {
                System.out.println("Book not found or already returned.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // list user transactions
    public void listUserTransactions(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM transactions WHERE user_id=?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(
                    "BookID=" + rs.getInt("book_id") +
                    ", Issue=" + rs.getString("issue_date") +
                    ", Due=" + rs.getString("due_date") +
                    ", Returned=" + rs.getInt("returned")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // check overdue reminders
    public void checkDueReminders() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT * FROM transactions WHERE returned=0 AND due_date < date('now')"
            );
            while (rs.next()) {
                System.out.println("Reminder: Book " + rs.getInt("book_id") +
                                   " is overdue for User " + rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

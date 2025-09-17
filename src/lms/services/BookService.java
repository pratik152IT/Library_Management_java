package lms.services;

import lms.db.DBConnection;
import lms.models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public boolean addBook(String title, String author) {
        String sql = "INSERT INTO books(title, author, available) VALUES(?,?,1);";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> searchBooks(String keyword) {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?;";
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book b = new Book(rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("available") == 1);
                    list.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Book> listAllBooks() {
        String sql = "SELECT * FROM books;";
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book b = new Book(rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("available") == 1);
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

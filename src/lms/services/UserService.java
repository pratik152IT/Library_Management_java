package lms.services;

import lms.db.DBConnection;
import lms.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {

    public User login(String username, String password) {
        String query = "SELECT user_id, username, role FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password, String role) {
        String sql = "INSERT INTO users(username,password,role) VALUES(?,?,?);";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role == null ? "MEMBER" : role);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique"))
                System.out.println("Username already exists. Choose another.");
            else
                e.printStackTrace();
            return false;
        }
    }
}


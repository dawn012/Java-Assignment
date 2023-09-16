package Validator;

import Connect.DatabaseUtils;
import User.User;
import Login.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginValidator {

    public static User login(String username, String password) {
        Login newLogin = new Login();



        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseUtils.getConnection();

            String sql = "SELECT * FROM User WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String dbUsername = resultSet.getString("username");
                String dbPassword = resultSet.getString("password");
                String accStatus = resultSet.getString("accStatus");
                String userType = resultSet.getString("userType");

                // password validate
                if (dbPassword.equals(password)) {



                    user.setUserId(userId);
                    user.setUsername(dbUsername);
                    user.setAccStatus(accStatus);
                    user.setUserType(userType);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // if not existed and wrong password return null
        return null;

    }

    public static boolean isUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseUtils.getConnection();

            String sql = "SELECT username FROM User WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            resultSet = stmt.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

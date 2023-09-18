package Validator;

import Connect.DatabaseUtils;
import Login.Login;
import User.Admin;
import User.Customer;
import User.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginValidator {
    public static ArrayList<User> userList = new ArrayList<>();

    public static void getUsersFromDatabase() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseUtils.getConnection();

            String sql = "SELECT * FROM User";
            stmt = conn.prepareStatement(sql);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String gender = resultSet.getString("gender");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String userType = resultSet.getString("userType");
                String DOB = resultSet.getString("DOB");
                String phoneNo = resultSet.getString("phoneNo");
                String accStatus = resultSet.getString("accStatus");

                User user;
                if ("cust".equals(userType)) {
                    user = new Customer(userId, new Login(username, password), email, DOB, userType, accStatus);
                } else {
                    user = new Admin(new Login(username, password), email, DOB, userType, userId, gender, phoneNo);
                }
                userList.add(user);
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
    }

    public static User findUserByUsername(String username) {
        User foundUser = null;
        for (User user : LoginValidator.userList) {
            if (user.getLogin().getUsername().equals(username)) {
                foundUser = user;
                break;
            }
        }
        return foundUser;
    }

    public static String getUserStatusByUsername(String username) {
        for (User user : LoginValidator.userList) {
            if (user.getLogin().getUsername().equals(username)) {
                if (user instanceof Customer) {
                    Customer customer = (Customer) user;
                    return customer.getAccStatus();
                }
            }
        }
        return null;
    }

    public static boolean validatePassword(User user, String password) {
        return user.getLogin().getPassword().equals(password);
    }

    public static boolean canRetryLogin(int passwordAttempts) {
        return passwordAttempts < 3;
    }

    public static void freezeAccount(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtils.getConnection();
            String sql = "UPDATE User SET accStatus = 'inactive' WHERE userID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }
    public static void printUserList(ArrayList<User> userList) {
        for (User user : userList) {
            System.out.println(user);
            System.out.println("---------------------------------------");
        }
    }

}

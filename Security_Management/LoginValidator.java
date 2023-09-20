package Security_Management;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Security_Management.Admin.getAllUsers;

public class LoginValidator {


    public static boolean validatePassword(User user, String password) {
        return user.getLogin().getPassword().equals(password);
    }

    public static boolean canRetryLogin(int passwordAttempts) {
        return passwordAttempts < 3;
    }

    public static void freezeAccount(User user) {
        try {
            String sql = "UPDATE User SET accStatus = 'inactive' WHERE userID = ?";
            DatabaseUtils.updateQuery(sql, user.getUserId());

            System.out.println("Account frozen successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to freeze account.");
        }
    }

}

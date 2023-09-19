package Security_Management;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Security_Management.Admin.getAllUsers;

public class LoginValidator {
    //public static ArrayList<User> userList = new ArrayList<>();

    public static ArrayList<User> getUsersFromDatabase() {
        ArrayList<User> userList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM User";
            ResultSet resultSet = DatabaseUtils.selectQueryById("*", "User", null, null);

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
                    user = new Admin(new Login(username, password), email, DOB, userType, gender, phoneNo, userId);
                }
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }


    public static User findUserByUsername(String username) {
        ArrayList<User> userList = getAllUsers();
        User foundUser = null;
        for (User user : userList) {
            if (user.getLogin().getUsername().equals(username)) {
                foundUser = user;
                break;
            }
        }
        return foundUser;
    }

    public static User findUserById(int userId) {
        User foundUser = null;
        ArrayList<User> userList = getAllUsers();
        for (User user : userList) {
            if (user.getUserId() == userId) {
                foundUser = user;
                break;
            }
        }
        return foundUser;
    }

    public static String getUserStatusByUsername(String username) {
        ArrayList<User> userList = getAllUsers();

        for (User user : userList) {
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
        try {
            String sql = "UPDATE User SET accStatus = 'inactive' WHERE userID = ?";
            DatabaseUtils.updateQuery(sql, user.getUserId());

            System.out.println("Account frozen successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to freeze account.");
        }
    }

    public static void printUserList(ArrayList<User> userList) {
        for (User user : userList) {
            System.out.println(user);
            System.out.println("---------------------------------------");
        }
    }

}

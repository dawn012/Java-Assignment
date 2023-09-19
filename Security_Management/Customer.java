package Security_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Customer extends User {
    private int custId;
    private String accStatus;

    public Customer() {
        this.setLogin(new Login());
    }

    public Customer(String accStatus) {
        this.accStatus = "active";

    }

    public Customer(int custId, Login login, String email, String DOB, String usertype, String accStatus) {
        super(login, email, DOB, usertype);
        this.custId = custId;
        this.accStatus = accStatus;
    }

    public static User getUserByUsername(ArrayList<User> userList, String username) {
        for (User user : userList) {
            if (user.getLogin().getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public void add() {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO User (username, password, email, userType, DOB, accStatus) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] params = {getLogin().getUsername(), getLogin().getPassword(), getEmail(), "cust", getDOB(), "active"};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nUser has been successfully registered!\nWelcome " + getLogin().getUsername() + " !");
        } else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public static void forgetPassword() throws SQLException {
        ArrayList<User> userList = Admin.getAllUsers();
        Scanner input = new Scanner(System.in);
        int choice = -1;
        boolean passwordChanged = false;

        while (true) {
            System.out.println("Please Enter Your Username (0 - Back): ");
            String username = input.nextLine();

            if (username.equals("0")) {
                break;
            }

            System.out.println("Please Enter your Email: ");
            String email = input.nextLine();

            passwordChanged = resetCustPassword(userList, username, email);

            if (passwordChanged) {
                System.out.println("Password has been changed successfully.");
                break;
            } else {
                System.out.println("Invalid username or email. Password reset failed.");
            }

            try {
                System.out.println("(0 - Back | 1 - Try Again): ");
                choice = input.nextInt();
                input.nextLine();

                if (choice == 0) {
                    break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        }
    }

    public static boolean resetCustPassword(ArrayList<User> customerList, String username, String email) throws SQLException {
        User customer = null;
        for (User cust : customerList) {
            if (cust.getLogin().getUsername().equals(username) && cust.getEmail().equals(email)) {
                customer = cust;
                break;
            }
        }

        if (customer != null) {
            String newPassword = generateRandomPassword();

            customer.getLogin().updatePasswordToDatabase(newPassword, customer.getUserId());

            System.out.println("Your new password is: " + newPassword);
            System.out.println("Please change your password after login.");


            return true;
        } else {
            return false;
        }
    }

    public boolean resetCustPassword(ArrayList<User> customerList, String username) throws SQLException {
        Scanner input = new Scanner(System.in);
        User customer = null;

        for (User cust : customerList) {
            if (cust.getLogin().getUsername().equals(username)) {
                customer = cust;
                break;
            }
        }

        if (customer != null) {
            System.out.print("Enter new password: ");
            String newPassword = RegisterValidator.validatePassword(input);

            customer.getLogin().updatePasswordToDatabase(newPassword, customer.getUserId());

            System.out.println("Your new password is: " + newPassword);
            System.out.println("Password updated successfully.");

            return true;
        } else {
            System.out.println("User not found. Password reset failed.");
            return false;
        }
    }

    private static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newPassword = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            newPassword.append(characters.charAt(index));
        }

        return newPassword.toString();
    }

    public void updateCustomerInfo() throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        try {
            String updateSql = "UPDATE User SET username = ?, email = ?, DOB = ? WHERE userID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, getLogin().getUsername());
            updateStmt.setString(2, getEmail());
            updateStmt.setString(3, getDOB());
            updateStmt.setInt(4, getCustId());

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Customer information updated successfully!");
            } else {
                System.out.println("Failed to update customer information.");
            }

            updateStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    @Override
    public int getUserId() {
        return custId;
    }

    public String getAccStatus() {
        return accStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Account Status: ").append(getAccStatus()).append("\n");
        return sb.toString();
    }

}

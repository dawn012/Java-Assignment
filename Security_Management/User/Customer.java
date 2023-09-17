package User;

import Connect.DatabaseUtils;
import Login.Login;
import Validator.RegisterValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Customer extends User{
    private int custId;
    private String accStatus;
    private String email;
    private String dob;

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

    public void viewProfile() {
        try {
            Connection conn = DatabaseUtils.getConnection();
            String sql = "SELECT * FROM User WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, getCustId());

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("DOB");
                String userType = resultSet.getString("userType");
                String accStatus = resultSet.getString("accStatus");


                System.out.println("User ID: " + userId);
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Date of Birth: " + dob);
                System.out.println("User Type: " + userType);
                System.out.println("Account Status: " + accStatus);
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", accStatus='" + accStatus + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                "} " + super.toString();
    }

    public void add() {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO User (username, password, email, userType, DOB, accStatus) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] params = {getLogin().getUsername(), getLogin().getPassword(), getEmail(), "cust", getDOB(),"active"};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nUser has been successfully registered!\nWelcome "+ getLogin().getUsername() +" !");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public void modifyCustInfo(Scanner scanner, Customer customer) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        boolean isEditing = true;

        while (isEditing) {
            System.out.println("\nModify Customer Information:");
            System.out.println("1. Username: " + customer.getLogin().getUsername());
            System.out.println("2. Email: " + customer.getEmail());
            System.out.println("3. Date of Birth: " + customer.getDOB());
            System.out.println("0. Editing completed");

            System.out.print("Please select the information you want to modify: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    isEditing = false;
                    customer.updateCustomerInfo(conn);
                    break;
                case 1:
                    System.out.print("Enter new username: ");
                    String newUsername = RegisterValidator.validateUsername(scanner);
                    customer.getLogin().setUsername(newUsername);
                    System.out.println("Username updated to: " + newUsername);
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    String newEmail = RegisterValidator.validateEmail(scanner);
                    customer.setEmail(newEmail);
                    System.out.println("Email updated to: " + newEmail);
                    break;
                case 3:
                    System.out.print("Enter new date of birth (dd-MM-yyyy): ");
                    String newDOB = RegisterValidator.validateDateOfBirth(scanner);
                    customer.setDOB(newDOB);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
    public void updateCustomerInfo(Connection conn) {
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



    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    @Override
    public int getUserId() {
        return custId;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }
}

package Security_Management;

import java.sql.*;
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

            passwordChanged = Login.resetCustPassword(userList, username, email,true);

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

    public static User registerUser(Scanner input) {
        User newUser = new Customer();

        System.out.println("Please Enter Username: ");
        String username = RegisterValidator.validateUsername(input);
        newUser.getLogin().setUsername(username);

        System.out.println("Please Enter Password: ");
        String password = RegisterValidator.validatePassword(input);
        newUser.getLogin().setPassword(password);

        System.out.println("Please Confirm Your Password: ");
        String confirmPassword = RegisterValidator.validatePasswordConfirmation(input, password);

        System.out.println("Please Enter Your email: ");
        String email = RegisterValidator.validateEmail(input);
        ((Customer)newUser).setEmail(email);

        input.nextLine();

        System.out.println("Please Enter Date of Birth(01-01-1990): ");
        String dob = RegisterValidator.validateDateOfBirth(input);
        newUser.setDOB(dob);

        newUser.add();

        return newUser;
    }

    public void viewAllCustomers() {
        ArrayList<User> custList = getCustomerDataFromDatabase();
        System.out.println(custList);

        System.out.println("\nAll Customers:\n");
        System.out.println(String.format("%-10s %-15s %-30s %-15s %-15s", "User ID", "Username", "Email", "Date of Birth", "Account Status"));
        System.out.println("----------------------------------------------------------------------------------------------");

        for (User customer : custList) {
            int userId = customer.getUserId();
            String username = customer.getLogin().getUsername();
            String email = customer.getEmail();
            String dob = customer.getDOB();
            String accStatus = ((Customer) customer).getAccStatus();

            System.out.println(String.format("%-10d %-15s %-30s %-15s %-15s", userId, username, email, dob, accStatus));
        }
    }

    private ArrayList<User> getCustomerDataFromDatabase() {
        ArrayList<User> custList = new ArrayList<>();

        try {
            Connection conn = DatabaseUtils.getConnection();
            String sql = "SELECT * FROM User WHERE userType = 'cust'";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("DOB");
                String accStatus = resultSet.getString("accStatus");

                Customer customer = new Customer();
                customer.setCustId(userId);
                customer.getLogin().setUsername(username);
                customer.setEmail(email);
                customer.setDOB(dob);
                customer.setAccStatus(accStatus);

                custList.add(customer);
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return custList;
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

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Account Status: ").append(getAccStatus()).append("\n");
        return sb.toString();
    }

}

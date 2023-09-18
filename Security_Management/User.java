package Security_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static Security_Management.Admin.getAllUsers;

public abstract class User{

    private Login login;
    private String email;
    private String DOB;
    private String userType;


    public abstract void add();

    public User(Login login, String email, String DOB, String userType) {
        this.login = login != null ? login : new Login();
        this.email = email;
        this.DOB = DOB;
        this.userType = userType;
    }

    public User() {
    }

    public void viewProfile(Customer customer) {
        int userId = customer.getCustId();
        String username = customer.getLogin().getUsername();
        String email = customer.getEmail();
        String dob = customer.getDOB();
        String userType = getUserType();
        String accStatus = customer.getAccStatus();

        System.out.println("\nUser ID: " + userId);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Date of Birth: " + dob);
        System.out.println("User Type: " + userType);
        System.out.println("Account Status: " + accStatus);
    }
    public void viewProfile(Admin admin) {
        int adminId = admin.getAdminId();
        String username = admin.getLogin().getUsername();
        String email = admin.getEmail();
        String dob = admin.getDOB();
        String userType = admin.getUserType();
        String gender = admin.getGender();
        String phoneNo = admin.getPhoneNo();

        System.out.println("\nAdmin ID: " + adminId);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Date of Birth: " + dob);
        System.out.println("User Type: " + userType);
        System.out.println("Gender: " + gender);
        System.out.println("Phone Number: " + phoneNo);
    }

    public void updateUserById(User user) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        if (user instanceof Admin) {
            ((Admin) user).updateAdminInfo(conn);
        } else if (user instanceof Customer) {
            ((Customer) user).updateCustomerInfo(conn);
        }
    }

    public static User findUserById(int userId) {
        LoginValidator.userList = getAllUsers();
        User foundUser = null;
        for (User user : LoginValidator.userList) {
            if (user.getUserId() == userId) {
                foundUser = user;
                break;
            }
        }
        System.out.println(LoginValidator.userList);
        return foundUser;
    }

    public void modifyProfile(Scanner scanner, Admin admin) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        boolean isEditing = true;

        while (isEditing) {
            int choice = 0;
            boolean error = true;

            do {
                try {
                    System.out.println("\nModify Admin Information:");
                    System.out.println("1. Username     : " + admin.getLogin().getUsername());
                    System.out.println("2. Email        : " + admin.getEmail());
                    System.out.println("3. Date of Birth: " + admin.getDOB());
                    System.out.println("4. Gender       : " + admin.getGender());
                    System.out.println("5. Phone Number : " + admin.getPhoneNo());
                    System.out.println("0. Editing completed");
                    System.out.print("Please select your operation: ");

                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice >= 0 && choice <= 5) {
                        error = false;
                    } else {
                        System.out.println("Invalid Choice! Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    scanner.nextLine();
                }
            } while (error);

            switch (choice) {
                case 0:
                    isEditing = false;
                    admin.updateAdminInfo(conn);
                    break;
                case 1:
                    System.out.print("Enter new username: ");
                    String newUsername = RegisterValidator.validateUsername(scanner);
                    admin.getLogin().setUsername(newUsername);
                    System.out.println("Username updated to: " + newUsername);
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    String newEmail = RegisterValidator.validateEmail(scanner);
                    admin.setEmail(newEmail);
                    System.out.println("Email updated to: " + newEmail);
                    break;
                case 3:
                    System.out.print("Enter new date of birth (dd-MM-yyyy): ");
                    String newDOB = RegisterValidator.validateDateOfBirth(scanner);
                    admin.setDOB(newDOB);
                    System.out.println("Date of birth updated to: " + newDOB);
                    break;
                case 4:
                    System.out.print("Enter new gender: ");
                    String newGender = RegisterValidator.validateGender(scanner);
                    admin.setGender(newGender);
                    System.out.println("Gender updated to: " + newGender);
                    break;
                case 5:
                    System.out.print("Enter new phone number: ");
                    String newPhoneNo = RegisterValidator.validatePhoneNumber(scanner);
                    admin.setPhoneNo(newPhoneNo);
                    System.out.println("Phone number updated to: " + newPhoneNo);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    public void modifyProfile(Scanner scanner, Customer customer) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        boolean isEditing = true;

        while (isEditing) {
            int choice = 0;
            boolean error = true;

            do {
                try {
                    System.out.println("\nModify Profile Information:");
                    System.out.println("1. Username     : " + customer.getLogin().getUsername());
                    System.out.println("2. Email        : " + customer.getEmail());
                    System.out.println("3. Date of Birth: " + customer.getDOB());
                    System.out.println("0. Editing completed");
                    System.out.print("Please select your operation: ");

                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice >= 0 && choice <= 3) {
                        error = false;
                    } else {
                        System.out.println("Invalid Choice! Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    scanner.nextLine();
                }
            } while (error);

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
                    System.out.println("Date of birth updated to: " + newDOB);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
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

    public static void updatePasswordToDatabase(User customer, String newPassword, Connection conn) {
        try {
            String updateSql = "UPDATE User SET password = ? WHERE userID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newPassword);
            updateStmt.setInt(2, customer.getUserId());

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Password updated successfully!");
            } else {
                System.out.println("Failed to update password.");
            }

            updateStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "login=" + login +
                ", email='" + email + '\'' +
                ", DOB='" + DOB + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    public int getUserId() {
        return 0;
    }

    public Login getLogin() {
        if (login == null) {
            login = new Login();
        }
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String usertype) {
        this.userType = usertype;
    }

}

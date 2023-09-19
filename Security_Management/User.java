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
    /*public void viewProfile(User user) {
        System.out.println(user.toString());
    }*/


    public void updateUserInfo() throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        String updateSql;
        PreparedStatement updateStmt;

        try {
            if (this instanceof Admin) {
                updateSql = "UPDATE User SET username = ?, email = ?, DOB = ?, gender = ?, phoneNo = ? WHERE userID = ?";
                updateStmt = conn.prepareStatement(updateSql);

                Admin admin = (Admin) this;
                updateStmt.setString(1, admin.getLogin().getUsername());
                updateStmt.setString(2, admin.getEmail());
                updateStmt.setString(3, admin.getDOB());
                updateStmt.setString(4, admin.getGender());
                updateStmt.setString(5, admin.getPhoneNo());
                updateStmt.setInt(6, admin.getAdminId());
            } else if (this instanceof Customer) {
                updateSql = "UPDATE User SET username = ?, email = ?, DOB = ? WHERE userID = ?";
                updateStmt = conn.prepareStatement(updateSql);

                Customer customer = (Customer) this;
                updateStmt.setString(1, customer.getLogin().getUsername());
                updateStmt.setString(2, customer.getEmail());
                updateStmt.setString(3, customer.getDOB());
                updateStmt.setInt(4, customer.getCustId());
            } else {
                System.out.println("Unsupported user type.");
                return;
            }

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User information updated successfully!");
            } else {
                System.out.println("Failed to update user information.");
            }

            updateStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    //auto detect cust or admin object to modify info
    public void modifyUserInfo(Scanner scanner, User user) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        boolean isEditing = true;

        while (isEditing) {
            int choice = 0;
            boolean error = true;

            do {
                try {
                    System.out.println("\nModify User Information (ID: " + user.getUserId() + "):");
                    System.out.println("1. Username        : " + user.getLogin().getUsername());
                    System.out.println("2. Email           : " + user.getEmail());
                    System.out.println("3. Date of Birth   : " + user.getDOB());

                    if (user instanceof Admin) {
                        System.out.println("4. Password    : " + user.getLogin().getPassword());
                        System.out.println("5. Gender      : " + ((Admin) user).getGender());
                        System.out.println("6. Phone Number: " + ((Admin) user).getPhoneNo());
                    }

                    System.out.println("0. Editing completed");
                    System.out.print("Please select your operation: ");

                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice >= 0 && choice <= 6) {
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
                    user.updateUserInfo();
                    break;
                case 1:
                    System.out.print("Enter new username: ");
                    String newUsername = RegisterValidator.validateUsername(scanner);
                    user.getLogin().setUsername(newUsername);
                    System.out.println("Username updated to: " + newUsername);
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    String newEmail = RegisterValidator.validateEmail(scanner);
                    user.setEmail(newEmail);
                    System.out.println("Email updated to: " + newEmail);
                    break;
                case 3:
                    System.out.print("Enter new date of birth (dd-MM-yyyy): ");
                    String newDOB = RegisterValidator.validateDateOfBirth(scanner);
                    user.setDOB(newDOB);
                    System.out.println("Date of birth updated to: " + newDOB);
                    break;
                case 4:
                    System.out.print("Enter new password: ");
                    String newPassword = RegisterValidator.validatePassword(scanner);
                    user.getLogin().setPassword(newPassword);
                    System.out.println("Password updated.");
                    break;
                case 5:
                    if (user instanceof Admin) {
                        System.out.print("Enter new gender: ");
                        String newGender = RegisterValidator.validateGender(scanner);
                        ((Admin) user).setGender(newGender);
                        System.out.println("Gender updated to: " + newGender);
                    } else {
                        System.out.println("Invalid choice. Please select a valid option.");
                    }
                    break;
                case 6:
                    if (user instanceof Admin) {
                        System.out.print("Enter new phone number: ");
                        String newPhoneNo = RegisterValidator.validatePhoneNumber(scanner);
                        ((Admin) user).setPhoneNo(newPhoneNo);
                        System.out.println("Phone number updated to: " + newPhoneNo);

                    } else {
                        System.out.println("Invalid choice. Please select a valid option.");
                    }
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

    public int getUserId() {
        if (this instanceof Customer) {
            return ((Customer) this).getCustId();
        } else if (this instanceof Admin) {
            return ((Admin) this).getAdminId();
        } else {
            return -1;
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User ID       : ").append(getUserId()).append("\n");
        sb.append("Username      : ").append(getLogin().getUsername()).append("\n");
        sb.append("Email         : ").append(getEmail()).append("\n");
        sb.append("Date of Birth : ").append(getDOB()).append("\n");
        sb.append("User Type     : ").append(getUserType()).append("\n");
        return sb.toString();
    }


}

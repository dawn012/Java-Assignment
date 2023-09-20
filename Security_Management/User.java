package Security_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static Security_Management.Admin.getAllUsers;


public abstract class User{

    private Login login;
    private String email;
    private String DOB;
    private String userType;
    private String gender;
    private String phoneNo;

    public User() {
    }

    public User(Login login, String email, String DOB, String userType, String gender, String phoneNo) {
        this.login = login != null ? login : new Login();
        this.email = email;
        this.DOB = DOB;
        this.userType = userType;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public User(Login login, String email, String DOB, String userType) {
        this.login = login;
        this.email = email;
        this.DOB = DOB;
        this.userType = userType;
    }


    public abstract void add();


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

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();

        try {
            ResultSet resultSet = DatabaseUtils.selectQuery("*", "User", null,null);

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
                    user = new Customer(userId, new Login(username, password), email, DOB, userType, accStatus, gender,phoneNo);
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
    public void updateUserInfo() throws SQLException {
        Connection conn = null;
        PreparedStatement updateStmt = null;

        try {
            conn = DatabaseUtils.getConnection();
            String updateSql;

            if (this instanceof Admin) {
                updateSql = "UPDATE User SET username = ?, email = ?, DOB = ?, gender = ?, phoneNo = ? WHERE userID = ?";
            } else if (this instanceof Customer) {
                updateSql = "UPDATE User SET username = ?, email = ?, DOB = ? WHERE userID = ?";
            } else {
                System.out.println("Unsupported user type.");
                return;
            }

            updateStmt = conn.prepareStatement(updateSql);
            setUserUpdateParams(updateStmt);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User information updated successfully!");
            } else {
                System.out.println("Failed to update user information.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (updateStmt != null) updateStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUserUpdateParams(PreparedStatement stmt) throws SQLException {
        if (this instanceof Admin) {
            Admin admin = (Admin) this;
            stmt.setString(1, admin.getLogin().getUsername());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getDOB());
            stmt.setString(4, admin.getGender());
            stmt.setString(5, admin.getPhoneNo());
            stmt.setInt(6, admin.getAdminId());
        } else if (this instanceof Customer) {
            Customer customer = (Customer) this;
            stmt.setString(1, customer.getLogin().getUsername());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getDOB());
            stmt.setInt(4, customer.getCustId());
        }
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
                    System.out.println("4. Gender          : " + user.getGender());
                    System.out.println("5. Phone Number    : " + user.getPhoneNo());

                    if (user instanceof Admin) {
                        System.out.println("6. Password        : " + user.getLogin().getPassword());

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
                    if (user instanceof Admin) {
                        System.out.print("Enter new gender: ");
                        String newGender = RegisterValidator.validateGender(scanner);
                         user.setGender(newGender);
                        System.out.println("Gender updated to: " + newGender);
                    } else if (user instanceof Customer) {
                        System.out.print("Enter new gender: ");
                        String newGender = RegisterValidator.validateGender(scanner);
                         user.setGender(newGender);
                        System.out.println("Gender updated to: " + newGender);
                    } else {
                        System.out.println("Invalid choice. Please select a valid option.");
                    }
                    break;
                case 5:
                    if (user instanceof Admin) {
                        System.out.print("Enter new phone number: ");
                        String newPhoneNo = RegisterValidator.validatePhoneNumber(scanner);
                        user.setPhoneNo(newPhoneNo);
                        System.out.println("Phone number updated to: " + newPhoneNo);
                    } else if (user instanceof Customer) {
                        System.out.print("Enter new phone number: ");
                        String newPhoneNo = RegisterValidator.validatePhoneNumber(scanner);
                        user.setPhoneNo(newPhoneNo);
                        System.out.println("Phone number updated to: " + newPhoneNo);
                    } else {
                        System.out.println("Invalid choice. Please select a valid option.");
                    }
                    break;
                case 6:

                        System.out.print("Enter new password: ");
                        String newPass = RegisterValidator.validatePassword(scanner);
                        user.getLogin().setPassword(newPass);
                        System.out.println("Phone number updated to: " + newPass);

                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nUser ID       : ").append(getUserId()).append("\n");
        sb.append("Username      : ").append(getLogin().getUsername()).append("\n");
        sb.append("Email         : ").append(getEmail()).append("\n");
        sb.append("Date of Birth : ").append(getDOB()).append("\n");
        sb.append("Gender        : ").append(getGender()).append("\n");
        sb.append("Phone Number  : ").append(getPhoneNo()).append("\n");
        sb.append("User Type     : ").append(getUserType()).append("\n");
        return sb.toString();
    }
}

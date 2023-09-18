package Security_Management;



import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin extends User {

    private int adminId;
    private String gender;
    private String phoneNo;


    public Admin(Login login, String email, String DOB, String userType, int adminId, String gender, String phoneNo) {
        super(login, email, DOB, userType);
        this.adminId = adminId;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public Admin() {

    }

    public void add() {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO User (username, gender, password, email, userType, phoneNo, DOB, accStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {getLogin().getUsername(), getGender(), getLogin().getPassword(), getEmail(), "admin", getPhoneNo(), getDOB(), "active"};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nAdministrator registration was successful, good luck with your work!\nWelcome "+ getLogin().getUsername() +" !");
        } else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public static Admin createAdmin(Scanner input) {
        Admin newAdmin = new Admin();

        System.out.println("Please Enter Username: ");
        String username = RegisterValidator.validateUsername(input);
        newAdmin.getLogin().setUsername(username);

        System.out.println("Please Enter Gender: ");
        String gender = RegisterValidator.validateGender(input);
        newAdmin.setGender(gender);

        System.out.println("Please Enter Password: ");
        String password = RegisterValidator.validatePassword(input);
        newAdmin.getLogin().setPassword(password);

        System.out.println("Please Confirm Your Password: ");
        String confirmPassword = RegisterValidator.validatePasswordConfirmation(input, password);

        System.out.println("Please Enter Your Email: ");
        String email = RegisterValidator.validateEmail(input);
        newAdmin.setEmail(email);

        input.nextLine();

        System.out.println("Please Enter Date of Birth (01-01-1990): ");
        String dob = RegisterValidator.validateDateOfBirth(input);
        newAdmin.setDOB(dob);

        System.out.println("Please Enter Your Phone Number (format: 01X-XXXX-XXXX): ");
        String phoneNo = RegisterValidator.validatePhoneNumber(input);
        newAdmin.setPhoneNo(phoneNo);

        newAdmin.add();
        return newAdmin;
    }


    public void updateAdminInfo(Connection conn) throws SQLException {
        try {
            String updateSql = "UPDATE User SET username = ?, email = ?, DOB = ?, gender = ?, phoneNo = ? WHERE userID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);

            updateStmt.setString(1, getLogin().getUsername());
            updateStmt.setString(2, getEmail());
            updateStmt.setString(3, getDOB());
            updateStmt.setString(4, getGender());
            updateStmt.setString(5, getPhoneNo());
            updateStmt.setInt(6, getAdminId());

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Admin information updated successfully.");
            } else {
                System.out.println("Failed to update admin information.");
            }

            updateStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
                    System.out.println("1. Username: " + user.getLogin().getUsername());
                    System.out.println("2. Email: " + user.getEmail());
                    System.out.println("3. Date of Birth: " + user.getDOB());
                    System.out.println("4. Gender: " + getGender());
                    System.out.println("5. Phone Number: " + getPhoneNo());
                    System.out.println("6. Password: " + user.getLogin().getPassword());
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
                    user.updateUserById(user);
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
                    System.out.print("Enter new gender: ");
                    String newGender = RegisterValidator.validateGender(scanner);
                    setGender(newGender);
                    System.out.println("Gender updated to: " + newGender);
                    break;
                case 5:
                    System.out.print("Enter new phone number: ");
                    String newPhoneNo = RegisterValidator.validatePhoneNumber(scanner);
                    setPhoneNo(newPhoneNo);
                    System.out.println("Phone number updated to: " + newPhoneNo);
                    break;
                case 6:
                    System.out.print("Enter new password: ");
                    String newPassword = RegisterValidator.validatePassword(scanner);
                    user.getLogin().setPassword(newPassword);
                    System.out.println("Password updated.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }


    public static ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();

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

        return userList;
    }

    public static void printArrayList(ArrayList<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    public void manageAccountStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the User ID (0 to Cancel): ");
        int userId;
        while (true) {

            if (scanner.hasNextInt()) {
                userId = scanner.nextInt();
                if (userId == 0) {
                    System.out.println("Operation canceled.");
                    return;
                } else if (userId > 0) {
                    break;
                } else {
                    System.out.print("Invalid input.");
                    scanner.nextLine();
                }
            } else {
                System.out.print("Invalid input. Please enter a valid User ID (0 to Cancel): ");
                scanner.next();
            }
        }

        int action;
        while (true) {
            System.out.print("Choose an action (1 for Unblock, 2 for Block, 0 to Cancel): ");
            if (scanner.hasNextInt()) {
                action = scanner.nextInt();
                if (action == 0) {
                    System.out.println("Operation canceled.");
                    return;
                } else if (action == 1 || action == 2) {
                    break;
                } else {
                    System.out.print("Invalid input. Please choose a valid action (1 for Unblock, 2 for Block, 0 to Cancel): ");
                    scanner.nextLine();
                }
            } else {
                System.out.print("Invalid input. Please choose a valid action (1 for Unblock, 2 for Block, 0 to Cancel): ");
                scanner.next();
            }
        }

        String newStatus = (action == 1) ? "active" : "inactive";

        try {
            Connection conn = DatabaseUtils.getConnection();

            String updateSql = "UPDATE User SET accStatus = ? WHERE userID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, userId);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User account with ID " + userId + " has been " + (newStatus.equals("active") ? "unblocked." : "blocked."));
            } else {
                System.out.println("Failed to update user account status.");
            }

            updateStmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewAllUsers() {

        try {
            Connection conn = DatabaseUtils.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM User";
            ResultSet resultSet = stmt.executeQuery(sql);

            System.out.println(String.format("%-5s %-15s %-10s %-25s %-15s %-12s %-20s %-15s", "ID", "Username", "Gender", "Email", "User Type", "Date of Birth", "Phone Number", "Account Status"));
            System.out.println("-----------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String gender = resultSet.getString("gender");
                String email = resultSet.getString("email");
                String userType = resultSet.getString("userType");
                String DOB = resultSet.getString("DOB");
                String phoneNo = resultSet.getString("phoneNo");
                String accStatus = resultSet.getString("accStatus");


                System.out.println(String.format("%-5d %-15s %-10s %-25s %-15s %-12s %-20s %-15s", userId, username, gender, email, userType, DOB, phoneNo, accStatus));
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void viewAllCustomers() {
        try {
            Connection conn = DatabaseUtils.getConnection();
            String sql = "SELECT * FROM User WHERE userType = 'cust'";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            System.out.println("All Customers:\n");

            System.out.println(String.format("%-10s %-15s %-30s %-15s %-15s", "User ID", "Username", "Email", "Date of Birth", "Account Status"));
            System.out.println("----------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("DOB");
                String accStatus = resultSet.getString("accStatus");

                System.out.println(String.format("%-10d %-15s %-30s %-15s %-15s", userId, username, email, dob, accStatus));
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewAllAdmins() {
        try {
            Connection conn = DatabaseUtils.getConnection();
            String sql = "SELECT * FROM User WHERE userType = 'admin'";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            System.out.println("All Admins:\n");


            System.out.println(String.format("%-10s %-15s %-30s %-15s %-10s %-15s", "User ID", "Username", "Email", "Date of Birth", "Gender", "Phone Number"));
            System.out.println("------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("DOB");
                String gender = resultSet.getString("gender");
                String phoneNo = resultSet.getString("phoneNo");


                System.out.println(String.format("%-10d %-15s %-30s %-15s %-10s %-15s", userId, username, email, dob, gender, phoneNo));
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteUserById(Scanner scanner) {
        System.out.print("Please enter the ID of the user you want to delete: ");
        int userId = scanner.nextInt();

        // 添加确认删除的选项
        System.out.print("Are you sure you want to delete this user? (Y/N): ");
        scanner.nextLine(); // 消耗换行符

        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y")) {
            try {
                Connection conn = DatabaseUtils.getConnection();
                String sql = "DELETE FROM User WHERE userID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User with ID " + userId + " has been deleted successfully.");
                } else {
                    System.out.println("User with ID " + userId + " not found or deletion failed.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (confirmation.equals("n")) {
            System.out.println("Deletion canceled. User has not been deleted.");
        } else {
            System.out.println("Invalid input. Deletion canceled. User has not been deleted.");
        }
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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", gender='" + gender + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                "} " + super.toString();
    }
    @Override
    public int getUserId() {
        return adminId;
    }
}

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
            System.out.println("\nAdministrator registration was successful, good luck with your work!\nWelcome " + getLogin().getUsername() + " !");
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


    public static ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();

        try {
            ResultSet resultSet = DatabaseUtils.selectQueryById("*", "User", null);

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
            String updateSql = "UPDATE User SET accStatus = ? WHERE userID = ?";
            int rowsUpdated = DatabaseUtils.updateQuery(updateSql, newStatus, userId);

            if (rowsUpdated > 0) {
                System.out.println("User account with ID " + userId + " has been " + (newStatus.equals("active") ? "unblocked." : "blocked."));
            } else {
                System.out.println("Failed to update user account status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllAdmins() {
        ArrayList<User> adminList = getAdminDataFromDatabase();
        System.out.println(adminList);

        System.out.println("\nAll Admins:\n");

        System.out.println(String.format("%-10s %-15s %-30s %-15s %-10s %-15s", "User ID", "Username", "Email", "Date of Birth", "Gender", "Phone Number"));
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        for (User admin : adminList) {
            int userId = admin.getUserId();
            String username = admin.getLogin().getUsername();
            String email = admin.getEmail();
            String dob = admin.getDOB();
            String gender = ((Admin) admin).getGender();
            String phoneNo = ((Admin) admin).getPhoneNo();

            System.out.println(String.format("%-10d %-15s %-30s %-15s %-10s %-15s", userId, username, email, dob, gender, phoneNo));
        }
    }

    private ArrayList<User> getAdminDataFromDatabase() {
        ArrayList<User> adminList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM User WHERE userType = 'admin'";
            ResultSet resultSet = DatabaseUtils.selectQueryById("*", "User", "userType = 'admin'");

            while (resultSet.next()) {
                int userId = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("DOB");
                String gender = resultSet.getString("gender");
                String phoneNo = resultSet.getString("phoneNo");

                Admin admin = new Admin();
                admin.setAdminId(userId);
                admin.getLogin().setUsername(username);
                admin.setEmail(email);
                admin.setDOB(dob);
                admin.setGender(gender);
                admin.setPhoneNo(phoneNo);

                adminList.add(admin);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adminList;
    }




    public void deleteUserById(Scanner scanner) {
        System.out.print("Please enter the ID of the user you want to delete: ");
        int userId = scanner.nextInt();

        System.out.print("Are you sure you want to delete this user? (Y/N): ");
        scanner.nextLine();

        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y")) {
            try {
                String sql = "DELETE FROM User WHERE userID = ?";
                int rowsAffected = DatabaseUtils.deleteQueryById("User", "accStatus", "userID", userId);

                if (rowsAffected > 0) {
                    System.out.println("User with ID " + userId + " has been deleted successfully.");
                } else {
                    System.out.println("User with ID " + userId + " not found or deletion failed.");
                }
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
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Gender        : ").append(getGender()).append("\n");
        sb.append("Phone Number  : ").append(getPhoneNo()).append("\n");
        return sb.toString();
    }

    @Override
    public int getUserId() {
        return adminId;
    }
}

package Security_Management;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static Security_Management.LoginValidator.*;
import static Security_Management.User.*;
import Database.DatabaseUtils;

public class Login {

    private String username;
    private String password;
    public Login() {
    }

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User loginMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int passwordAttempts = 0;
        boolean accountFrozen = false;

        while (true) {
            if (accountFrozen) {
                return null;
            }

            System.out.print("Please Enter Username: ");
            String username = scanner.nextLine();

            User foundUser = findUserByUsername(username);

            String userStatus = getUserStatusByUsername(username);

            if (foundUser == null) {
                System.out.println("User Does Not Exist, Please Check The Username.");
            } else if ("inactive".equals(userStatus)) {
                System.out.println("Your Account Has Been Frozen, Please Contact Support.");

            } else {
                int result = handlePasswordInput(foundUser, passwordAttempts);
                if (result == 0) {

                    return foundUser;

                } else if (result == -1) {
                    return null;
                } else {
                    passwordAttempts = result;
                    if (passwordAttempts >= 3) {
                        accountFrozen = true;
                        freezeAccount(foundUser);
                    }
                }
            }
        }
    }

    public static int handlePasswordInput(User foundUser, int passwordAttempts) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Please Enter Password: ");
            String password = scanner.nextLine();

            if (validatePassword(foundUser, password)) {
                //performPostLoginActions(foundUser);
                return 0;
            } else {
                passwordAttempts++;
                if (!canRetryLogin(passwordAttempts)) {
                    if (shouldRetryPassword()) {
                        System.out.print("Please Enter Password Again: ");
                        String retryPassword = scanner.nextLine();

                        if (validatePassword(foundUser, retryPassword)) {
                            //performPostLoginActions(foundUser);
                            return 0;
                        } else {
                            System.out.println("Password Incorrect. Your Account Has Been Frozen, Please Contact Support.");
                            freezeAccount(foundUser);
                            return passwordAttempts;
                        }
                    } else {
                        System.out.println("Login Failed, Thank You.");
                        return -1;
                    }
                } else {
                    System.out.println("Password Incorrect, Please Re-enter.\n");
                }
            }
        }
    }

    private static boolean shouldRetryPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("The Number Of Password Entry Failures Has Reached 3 Times, and Your Account will be Frozen if You Fail Again.\nDo You Want to Retry Entering Password? (Y/N): ");
        String continueChoice = scanner.nextLine();
        return continueChoice.equalsIgnoreCase("Y");
    }
    
    public static boolean resetCustPassword(ArrayList<User> customerList, String username, String email, boolean generateRandom) throws SQLException {
        User customer = null;
        for (User cust : customerList) {
            if (cust.getLogin().getUsername().equals(username) && (email == null || cust.getEmail().equals(email))) {
                customer = cust;
                break;
            }
        }

        if (customer != null) {
            String newPassword;
            if (generateRandom) {
                newPassword = generateRandomPassword();
            } else {
                Scanner input = new Scanner(System.in);
                System.out.print("Enter new password: ");
                newPassword = RegisterValidator.validatePassword(input);
            }

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

    public void updatePasswordToDatabase(String newPassword, int userId) throws SQLException {
        String updateSql = "UPDATE User SET password = ? WHERE userID = ?";

        try {
            int rowsUpdated = DatabaseUtils.updateQuery(updateSql, newPassword, userId);

            if (rowsUpdated > 0) {
                System.out.println("Password updated successfully!");
            } else {
                System.out.println("Failed to update password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package Security_Management;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static Security_Management.LoginValidator.*;

public class Login {

    private String username;
    private String password;

    public User getUserByUsername(ArrayList<User> userList, String username) {
        for (User user : userList) {
            if (user.getLogin().getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static User login() throws SQLException {
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

    private static int handlePasswordInput(User foundUser, int passwordAttempts) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Please Enter Password: ");
            String password = scanner.nextLine();

            if (validatePassword(foundUser, password)) {
                performPostLoginActions(foundUser);
                return 0;
            } else {
                passwordAttempts++;
                if (!canRetryLogin(passwordAttempts)) {
                    if (shouldRetryPassword()) {
                        System.out.print("Please Enter Password Again: ");
                        String retryPassword = scanner.nextLine();

                        if (validatePassword(foundUser, retryPassword)) {
                            performPostLoginActions(foundUser);
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

    private static void performPostLoginActions(User foundUser) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Login Successful!\nWelcome "+foundUser.getLogin().getUsername()+" !");
        User user = Customer.getUserByUsername(userList, foundUser.getLogin().getUsername());

        if (user != null) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                int choice;

                do {
                    System.out.println("\nMenu:");
                    System.out.println("1. View Profile");
                    System.out.println("2. Edit Profile");
                    System.out.println("3. ");
                    System.out.println("4. Modify Profile");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");
                    choice = input.nextInt();
                    input.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.println("You selected Option 1.");
                            customer.viewProfile(customer);
                            break;
                        case 2:
                            System.out.println("You selected Option 2.");
                            customer.modifyCustInfo(input,customer);
                            break;
                        case 3:
                            System.out.println("You selected Option 3.");

                            break;
                        case 4:
                            System.out.println("You selected Option 4.");
                            break;
                        case 5:
                            System.out.println("Exiting the program.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please select a valid option.");
                    }

                } while (choice != 5);

                customer.modifyCustInfo(input,customer);
                System.out.println(customer);

            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                System.out.println("Found Admin: " + admin.getLogin().getUsername());
                System.out.println(admin);

                admin.modifyAdminInfo(input,admin);
                System.out.println(admin);
            }
        } else {
            System.out.println("User with username " + foundUser.getLogin().getUsername() + " not found.");
        }

        /*if ("admin".equals(foundUser.getUserType())) {
            // admin method

        } else if ("cust".equals(foundUser.getUserType())) {
            // customer method

        }*/
    }



    public Login() {
    }

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
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

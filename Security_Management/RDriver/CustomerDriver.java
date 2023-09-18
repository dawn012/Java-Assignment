package RDriver;

import User.Customer;
import User.User;
import Validator.LoginValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static Login.Login.login;
import static User.User.registerUser;

public class CustomerDriver {
    public static void main(String[] args) throws SQLException {
        ArrayList<User> userList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        User customer = new Customer();
        int choice;

        do {
            System.out.println("Menu:");
            System.out.println("1. User Registration");
            System.out.println("2. Login");
            System.out.println("3. Edit Customer Information");
            System.out.println("4. Modify Profile");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("You selected Option 1.");
                    registerUser(scanner);
                    break;
                case 2:
                    System.out.println("You selected Option 2.");
                    LoginValidator.getUsersFromDatabase();
                    login();
                case 3:
                    System.out.println("You selected Option 3.");
                  //  ((Customer) customer).modifyCustInfo(scanner,customer);
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

        scanner.close();
    }

}

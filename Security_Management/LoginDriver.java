package Security_Management;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


import static Security_Management.Login.loginMenu;
import static Security_Management.User.registerUser;

public class LoginDriver {

    public static void main(String[] args) throws SQLException {

        Scanner input = new Scanner(System.in);
        LoginValidator.getUsersFromDatabase();
        ArrayList<User> userList = LoginValidator.getUserList();
        //LoginValidator.printUserList(userList);


        int choice;
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("0. Exit");
            try {
                System.out.print("Enter your choice: ");
                choice = input.nextInt();
                input.nextLine();

                switch (choice) {

                    case 1:
                        System.out.println("You selected Option 1.");
                        registerUser(input);
                        break;
                    case 2:
                        System.out.println("You selected Option 2.");
                        loginMenu();
                        break;
                    case 3:
                        System.out.println("You selected Option 3.");
                        Customer.forgetPassword();
                        break;
                    case 0:
                        System.out.println("Exiting the program.");

                        return;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        }

    }

}

package Security_Management;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminDriver {

    public static void main(String[] args) {
        ArrayList<User> userList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        User admin = new Admin();
        int choice;


        do {
            printMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("You selected Option 1.");
                    Admin.createAdmin(scanner);
                    break;
                case 2:
                    System.out.println("You selected Option 2.");
                    boolean submenuActive = true;
                    while (submenuActive) {
                        System.out.println("Submenu Options:");
                        System.out.println("1. View All Users");
                        System.out.println("2. View All Customer");
                        System.out.println("3. View All Admin");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        int submenuChoice = scanner.nextInt();
                        switch (submenuChoice) {
                            case 1:
                                System.out.println("You selected Submenu Option 1.");
                                ((Admin) admin).viewAllUsers();
                                break;
                            case 2:
                                System.out.println("You selected Submenu Option 2.");
                                ((Admin) admin).viewAllCustomers();
                                break;
                            case 3:
                                System.out.println("You selected Submenu Option 3.");
                                ((Admin) admin).viewAllAdmins();
                                break;
                            case 0:
                                System.out.println("Returning to Main Menu.");
                                submenuActive = false; // 退出子菜单
                                break;
                            default:
                                System.out.println("Invalid option. Please choose again.");
                        }
                    }
                    break;

                case 3:
                    System.out.println("You selected Option 3.");
                    ((Admin) admin).manageAccountStatus();
                    break;
                case 4:
                    System.out.println("You selected Option 4.");
                    ((Admin) admin).viewAllUsers();
                    ((Admin) admin).deleteUserById(scanner);
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

    public static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Administrator Registration");
        System.out.println("2. View Users Information");
        System.out.println("3. Manage Account Status");
        System.out.println("4. Remove Users");
        System.out.println("5. Exit");
    }
}

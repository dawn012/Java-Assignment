package RDriver;

import User.Admin;
import User.Customer;
import User.User;

import java.util.ArrayList;
import java.util.Scanner;
public class RDriver {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        ArrayList<User> userList = Admin.getAllUsers();
        String usernameToFind = "admin";

        User user = Customer.getUserByUsername(userList, usernameToFind);


        if (user != null) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                System.out.println("Found Customer: " + customer.getLogin().getUsername());
                System.out.println(customer);

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
            System.out.println("User with username " + usernameToFind + " not found.");
        }


    }
}

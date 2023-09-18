package Security_Management;

import java.util.ArrayList;
import java.util.Scanner;
public class RDriver {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        ArrayList<User> userList = Admin.getAllUsers();
        String usernameToFind = "World";
        //String usernameToFind2 = "ggggg";


        User user = Customer.getUserByUsername(userList, usernameToFind);
        //  User user2 = Customer.getUserByUsername(userList, usernameToFind2);
        //User user3 = User.findUserById(25);
        // System.out.println(user3);



        if (user != null) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                System.out.println("Found Customer: " + customer.getLogin().getUsername());
                System.out.println(customer);
                customer.forgetPassword();
                //customer.resetCustPassword(userList,"World","ss@mm.com");
                // customer.modifyProfile(input,customer);
                System.out.println(customer);

            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                System.out.println("Found Admin: " + admin.getLogin().getUsername());
                System.out.println(admin);
                //   admin.modifyUserInfo(input,user2);

                admin.modifyProfile(input,admin);
                System.out.println(admin);
            }
        } else {
            System.out.println("User with username " + usernameToFind + " not found.");
        }


    }
}


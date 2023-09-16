package Driver;

import User.Admin;
import User.Customer;
import User.User;
import Validator.RegisterValidator;

import java.util.Scanner;

public class Driver {

    public static void main(String[] args) throws Exception {

        User newCust = new Customer();
        Admin newAdmin = new Admin();
        Scanner input = new Scanner(System.in);

        /*System.out.println("Please Enter Username: ");
        String username = RegisterValidator.validateUsername(input);
        newCust.getLogin().setUsername(username);

        System.out.println("Please Enter Password: ");
        String password = RegisterValidator.validatePassword(input);
        newCust.getLogin().setPassword(password);

        System.out.println("Please Confirm Your Password: ");
        String confirmPassword = RegisterValidator.validatePasswordConfirmation(input, password);

        System.out.println("Please Enter Your email: ");
        String email = RegisterValidator.validateEmail(input);
        newCust.setEmail(email);

        System.out.println("Please Enter Date of Birth(01-01-1990): ");
        String dob = RegisterValidator.validateDateOfBirth(input);
        newCust.setDOB(dob);

        newCust.add();*/


        System.out.println("Please Enter Date of Birth(01-01-1990): ");
        String dob = RegisterValidator.validateDateOfBirth(input);
        newAdmin.setDOB(dob);

        System.out.println("Please Enter Username: ");
        String username = RegisterValidator.validateUsername(input);
        newAdmin.getLogin().setUsername(username);

        System.out.println("Please Enter gender: ");
        String gender = RegisterValidator.validateGender(input);
        newAdmin.setGender(gender);

        System.out.println("Please Enter Password: ");
        String password = RegisterValidator.validatePassword(input);
        newAdmin.getLogin().setPassword(password);

        System.out.println("Please Confirm Your Password: ");
        String confirmPassword = RegisterValidator.validatePasswordConfirmation(input, password);

        System.out.println("Please Enter Your email: ");
        String email = RegisterValidator.validateEmail(input);
        newAdmin.setEmail(email);

        if (newAdmin instanceof Admin) {
            Admin admin = (Admin) newAdmin;
            System.out.println("Please Enter Your Phone Number (format: 01X-XXXX-XXXX): ");
            String phoneNo = RegisterValidator.validatePhoneNumber(input);
            admin.setPhoneNo(phoneNo);
        }

        newAdmin.add();





    }
}

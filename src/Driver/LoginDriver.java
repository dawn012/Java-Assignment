package Driver;

import Login.Login;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginDriver {

    public static void main(String[] args) throws SQLException {

        Login login = new Login();
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your username: ");
        String username = input.next();

        System.out.println("Please enter your password: ");
        String password = input.next();




    }
}

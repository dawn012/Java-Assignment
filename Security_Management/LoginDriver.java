package Security_Management;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


import static Security_Management.Login.loginMenu;

public class LoginDriver {

    public static void main(String[] args) throws SQLException {

        Scanner input = new Scanner(System.in);
        LoginValidator.getUsersFromDatabase();
        ArrayList<User> userList = LoginValidator.getUserList();

        LoginValidator.printUserList(userList);
        loginMenu();


    }



}

package RDriver;

import User.User;
import Validator.LoginValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static Login.Login.login;

public class LoginDriver {

    public static void main(String[] args) throws SQLException {

        Scanner input = new Scanner(System.in);
        LoginValidator.getUsersFromDatabase();
        ArrayList<User> userList = LoginValidator.getUserList();

        LoginValidator.printUserList(userList);
        login();


    }



}

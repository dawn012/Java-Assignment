package Security_Management;

import java.util.Scanner;

public abstract class User{

    private Login login;
    private String email;
    private String DOB;
    private String userType;


    public abstract void add();

    public User(Login login, String email, String DOB, String userType) {
        this.login = login != null ? login : new Login();
        this.email = email;
        this.DOB = DOB;
        this.userType = userType;
    }


    public User() {
    }

    public static User registerUser(Scanner input) {
        User newUser = new Customer();

        System.out.println("Please Enter Username: ");
        String username = RegisterValidator.validateUsername(input);
        newUser.getLogin().setUsername(username);

        System.out.println("Please Enter Password: ");
        String password = RegisterValidator.validatePassword(input);
        newUser.getLogin().setPassword(password);

        System.out.println("Please Confirm Your Password: ");
        String confirmPassword = RegisterValidator.validatePasswordConfirmation(input, password);

        System.out.println("Please Enter Your email: ");
        String email = RegisterValidator.validateEmail(input);
        ((Customer)newUser).setEmail(email);

        input.nextLine();

        System.out.println("Please Enter Date of Birth(01-01-1990): ");
        String dob = RegisterValidator.validateDateOfBirth(input);
        newUser.setDOB(dob);

        newUser.add();

        return newUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "login=" + login +
                ", email='" + email + '\'' +
                ", DOB='" + DOB + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
    public int getUserId() {
        return 0;
    }
    public Login getLogin() {
        if (login == null) {
            login = new Login();
        }
        return login;
    }


    public void setLogin(Login login) {
        this.login = login;
    }


    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String usertype) {
        this.userType = usertype;
    }

}

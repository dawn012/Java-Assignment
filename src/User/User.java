package User;

import Login.Login;

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

    public static User register(String username, String password, String email, String DOB) {
        Login login = new Login(username, password);
        Customer customer = new Customer("active");
        User newUser = customer;
        newUser.setLogin(login);
        newUser.setEmail(email);
        newUser.setDOB(DOB);
        return newUser;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setUserType(String usertype) {
        this.userType = usertype;
    }

}

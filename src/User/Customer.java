package User;

import Connect.DatabaseUtils;
import Login.Login;

import java.sql.SQLException;

public class Customer extends User{
    private int custId;
    private String accStatus;
    private String email;
    private String dob;

    public Customer() {
        this.setLogin(new Login());
    }

    public Customer(String accStatus) {
        this.accStatus = "active";

    }

    public void add() {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO User (username, password, email, userType, DOB, accStatus) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] params = {getLogin().getUsername(), getLogin().getPassword(), getEmail(), "cust", getDOB(),"active"};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nUser successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public Customer(int custId, Login login, String email, String DOB, String usertype, String accStatus) {
        super(login, email, DOB, usertype);
        this.custId = custId;
        this.accStatus = accStatus;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }


    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

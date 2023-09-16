package User;

import Connect.DatabaseUtils;

import java.sql.SQLException;

public class Admin extends User {

    private int adminId;
    private String gender;
    private String phoneNo;

    public void add() {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO User (username, gender, password, email, userType, phoneNo, DOB, accStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {getLogin().getUsername(), getGender(), getLogin().getPassword(), getEmail(), "admin", getPhoneNo(), getDOB(), "active"};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nUser successfully added...");
        } else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public Admin() {
    }

    public Admin(int adminId, String gender, String phoneNo) {
        this.adminId = adminId;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}

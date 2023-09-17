package Payment_Management;

import Database.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class TNG extends Payment {
    private String phoneNo;
    private String pinNo;

    public TNG() {
    }

    public TNG(int bookingId, double paymentAmount, String currency, String paymentMethod, String paymentDate, String paymentTime, String paymentStatus, String phoneNo, String pinNo) {
        super(bookingId, paymentAmount, currency, paymentMethod, paymentDate, paymentTime, paymentStatus);
        this.phoneNo = phoneNo;
        this.pinNo = pinNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPinNo() {
        return pinNo;
    }

    public void setPinNo(String pinNo) {
        this.pinNo = pinNo;
    }

    @Override
    public void pay() {
        String sql = "INSERT INTO TNG (PAYMENT_ID, PHONE_NO, PIN_NO) VALUES (?,?,?)";
        Object[] params = {paymentId, phoneNo, pinNo};

        try {
            int insert = DatabaseUtils.insertQuery(sql, params);

            if (insert == 1) {
                System.out.println("Insert tng successfully.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package Payment_Management;

import Database.DatabaseUtils;
import Driver.DateTime;
import Promotion_Management.Promotion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Payment {
    protected int paymentId;
    protected static int nextPaymentId;
    protected int bookingId;
    protected String paymentMethod;
    protected double paymentAmount;
    protected String currency;
    protected String paymentDate;
    protected String paymentTime;
    protected String paymentStatus;

    public Payment() {
    }

    public Payment(int bookingId, String paymentMethod, double paymentAmount, String currency, String paymentDate, String paymentTime, String paymentStatus) {
        this.paymentId = ++nextPaymentId;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.currency = currency;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.paymentStatus = paymentStatus;
    }

    public Payment(int paymentId, int bookingId, String paymentMethod, double paymentAmount, String currency, String paymentDate, String paymentTime, String paymentStatus) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.currency = currency;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    static {
        Object[] params = null;

        ResultSet rs;

        try {
            rs = DatabaseUtils.selectQueryById("COUNT(*)", "PAYMENT", null, params);

            // 获取整数结果
            if (rs.next()) {
                nextPaymentId = rs.getInt(1);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void pay();

    public void addPayment() {
        String sql = "INSERT INTO PAYMENT (BOOKING_ID, PAYMENT_METHOD, PAYMENT_AMOUNT, CURRENCY, PAYMENT_DATE, PAYMENT_TIME, PAYMENT_STATUS) VALUES (?,?,?,?,?,?,?)";
        Object[] params = {bookingId, paymentMethod, paymentAmount, currency, paymentDate, paymentTime, paymentStatus};

        try {
            DatabaseUtils.insertQuery(sql, params);

        } catch (SQLException e) {
            System.out.println("\nOops! Something went wrong. Please try again!");
            throw new RuntimeException(e);
        }
    }
}

package Promotion_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;
import Driver.DatabaseOperations;
import Driver.DateTime;
import Security_Management.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Promotion implements DatabaseOperations {
    private Customer customer;
    private int promotionId;
    private String description;
    private double discountValue;
    private double minSpend;
    private int perLimit;
    private DateTime startDate;
    private DateTime endDate;
    private int publishCount;
    private int receiveCount;
    private int promotionStatus;

    public Promotion() {
    }

    public Promotion(int promotionId, String description, double discountValue, double minSpend, int perLimit, DateTime startDate, DateTime endDate, int publishCount, int receiveCount, int promotionStatus) {
        this.promotionId = promotionId;
        this.description = description;
        this.discountValue = discountValue;
        this.minSpend = minSpend;
        this.perLimit = perLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.publishCount = publishCount;
        this.receiveCount = receiveCount;
        this.promotionStatus = promotionStatus;
    }

    public Promotion(Customer customer, int promotionId, String description, double discountValue, double minSpend, int perLimit, DateTime startDate, DateTime endDate, int publishCount, int receiveCount, int promotionStatus) {
        this.customer = customer;
        this.promotionId = promotionId;
        this.description = description;
        this.discountValue = discountValue;
        this.minSpend = minSpend;
        this.perLimit = perLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.publishCount = publishCount;
        this.receiveCount = receiveCount;
        this.promotionStatus = promotionStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(double minSpend) {
        this.minSpend = minSpend;
    }

    public int getPerLimit() {
        return perLimit;
    }

    public void setPerLimit(int perLimit) {
        this.perLimit = perLimit;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public int getPublishCount() {
        return publishCount;
    }

    public void setPublishCount(int publishCount) {
        this.publishCount = publishCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(int promotionStatus) {
        this.promotionStatus = promotionStatus;
    }

    static {
        String sql = "UPDATE PROMOTION_HISTORY SET PROMOTION_STATUS = ? " +
                "WHERE PROMOTION_STATUS = ? " +
                "AND PROMOTION_ID IN ( " +
                "SELECT PROMOTION_ID FROM PROMOTION WHERE END_DATE < ? " +
                ");";

        DateTime dateTime = new DateTime();
        Object[] params = {"EXPIRED", "UNUSED", dateTime.getCurrentDate()};

        try {
            DatabaseUtils.updateQuery(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean add() {
        String sql = "INSERT INTO PROMOTION (DESCRIPTION, DISCOUNT_VALUE, MIN_SPEND, PER_LIMIT, START_DATE, END_DATE, PUBLISH_COUNT, RECEIVE_COUNT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {description, discountValue, minSpend, perLimit, String.valueOf(startDate.getDate()), String.valueOf(endDate.getDate()), publishCount, receiveCount};

        try {
            int insert = DatabaseUtils.insertQuery(sql, params);

            if (insert == 1) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean modify() {
        String sql = "UPDATE PROMOTION SET DESCRIPTION = ?, DISCOUNT_VALUE = ?, MIN_SPEND = ?, PER_LIMIT = ?, START_DATE = ?, END_DATE = ?, PUBLISH_COUNT = ?, RECEIVE_COUNT = ? WHERE PROMOTION_ID = ?";
        Object[] params = {description, discountValue, minSpend, perLimit, String.valueOf(startDate.getDate()), String.valueOf(endDate.getDate()), publishCount, receiveCount, promotionId};

        try {
            int update = DatabaseUtils.updateQuery(sql, params);

            if (update == 1) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean delete() {
        Object[] params = {promotionId};

        int delete;

        try {
            delete = DatabaseUtils.deleteQueryById("PROMOTION", "PROMOTION_STATUS", "PROMOTION_ID", params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (delete == 1) {
            System.out.println("\nThe promotion has been deleted.");

            return true;
        } else {
            System.out.println("\nSomething went wrong...");
        }

        return false;
    }

    public void custClaimedPromotion() {
        String sql = "INSERT INTO PROMOTION_HISTORY (USER_ID, PROMOTION_ID, PROMOTION_STATUS) VALUES (?, ?, ?)";
        Object[] params = {customer.getCustId(), promotionId, "UNUSED"};

        try {
            // Try to insert new promotion code claimed by user
            DatabaseUtils.insertQuery(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateReceiveCount() {
        // To know current promotion remain claim times
        String sql = "UPDATE PROMOTION SET RECEIVE_COUNT = ? WHERE PROMOTION_ID = ?";
        Object[] params = {++receiveCount, promotionId};

        try {
            DatabaseUtils.updateQuery(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Promotion> validPromotionList() {
        ArrayList<Promotion> promotions = new ArrayList<>();

        DateTime currentDate = new DateTime();
        ResultSet rs;

        Object[] params = {currentDate.getCurrentDate(), 1, customer.getCustId()};

        try {
            rs = DatabaseUtils.selectQueryById("*", "PROMOTION", "? BETWEEN START_DATE AND END_DATE AND RECEIVE_COUNT < PUBLISH_COUNT AND PROMOTION_STATUS = ? AND PROMOTION_ID NOT IN (SELECT PROMOTION_ID FROM PROMOTION_HISTORY WHERE USER_ID = ?);", params);

            while(rs.next()) {
                Promotion promotion = new Promotion();

                promotion.setPromotionId(rs.getInt("PROMOTION_ID"));
                promotion.setDescription(rs.getString("DESCRIPTION"));
                promotion.setDiscountValue(rs.getDouble("DISCOUNT_VALUE"));
                promotion.setMinSpend(rs.getDouble("MIN_SPEND"));
                promotion.setPerLimit(rs.getInt("PER_LIMIT"));
                promotion.setStartDate(new DateTime(rs.getDate("START_DATE").toLocalDate()));
                promotion.setEndDate(new DateTime(rs.getDate("END_DATE").toLocalDate()));
                promotion.setPublishCount(rs.getInt("PUBLISH_COUNT"));
                promotion.setReceiveCount(rs.getInt("RECEIVE_COUNT"));

                promotions.add(promotion);
            }

            rs.close();

            return promotions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Promotion> ownPromotionList() {
        ArrayList<Promotion> promotions = new ArrayList<>();

        Object[] params = {customer.getCustId(), "UNUSED"};

        ResultSet rs;

        try {
            rs = DatabaseUtils.selectQueryById("*", "PROMOTION", "PROMOTION_ID IN (SELECT PROMOTION_ID FROM PROMOTION_HISTORY WHERE USER_ID = ? AND PROMOTION_STATUS = ?);", params);

            while(rs.next()) {
                Promotion promotion = new Promotion();

                promotion.setPromotionId(rs.getInt("PROMOTION_ID"));
                promotion.setDescription(rs.getString("DESCRIPTION"));
                promotion.setDiscountValue(rs.getDouble("DISCOUNT_VALUE"));
                promotion.setMinSpend(rs.getDouble("MIN_SPEND"));
                promotion.setPerLimit(rs.getInt("PER_LIMIT"));
                promotion.setStartDate(new DateTime(rs.getDate("START_DATE").toLocalDate()));
                promotion.setEndDate(new DateTime(rs.getDate("END_DATE").toLocalDate()));
                promotion.setPublishCount(rs.getInt("PUBLISH_COUNT"));
                promotion.setReceiveCount(rs.getInt("RECEIVE_COUNT"));
                promotion.setPromotionStatus(rs.getInt("PROMOTION_STATUS"));

                promotions.add(promotion);
            }

            rs.close();

            return promotions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int remainQty() {
        Object[] params = {customer.getCustId(), promotionId, "UNUSED"};

        ResultSet rs;

        try {
            rs = DatabaseUtils.selectQueryById("COUNT(*)", "PROMOTION_HISTORY", "USER_ID = ? AND PROMOTION_ID = ? AND PROMOTION_STATUS = ?", params);

            // 获取整数结果
            if (rs.next()) {
                System.out.println(rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public void custApplyPromotion(Booking booking) {
        String sql = "UPDATE PROMOTION_HISTORY " +
                "SET PROMOTION_STATUS = ?, " +
                "BOOKING_ID = ? " +
                "WHERE PROMOTION_ID = ? " +
                "AND USER_ID = ? " +
                "AND ID = ( " +
                    "SELECT ID " +
                    "FROM ( " +
                        "SELECT ID " +
                        "FROM PROMOTION_HISTORY " +
                        "WHERE PROMOTION_ID = ? " +
                        "AND USER_ID = ? " +
                        "AND PROMOTION_STATUS = ? " +
                        "LIMIT 1 " +
                    ") AS subquery " +
                ");";

        Object[] params = {"USED", booking.getBookingId(), promotionId, booking.getCustomer().getCustId(), promotionId, booking.getCustomer().getCustId(), "UNUSED"};

        try {
            DatabaseUtils.updateQuery(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String viewNewPromotionDetails() {
        return  "\n" + description
                + "\n1. Discount value : " + discountValue
                + "\n2. Minimum Spend : " + minSpend
                + "\n3. Start date : " + startDate.getDate()
                + "\n4. End date : " + endDate.getDate()
                + "\n5. Remain : " + (publishCount-receiveCount)
                + "\n\nYou can only use this promotion for " + perLimit + " time(s) only.\n";
    }

    public String viewOwnPromotionDetails() {
        return  "\n" + description
                + "\n1. Discount value : " + discountValue
                + "\n2. Minimum Spend : " + minSpend
                + "\n3. Start date : " + startDate.getDate()
                + "\n4. End date : " + endDate.getDate()
                + "\n5. Left : " + remainQty() + " time(s)";
    }

    @Override
    public String toString() {
        return  "\nPromotion ID : " + promotionId
                + "\n1. Description : " + description
                + "\n2. Discount value : " + discountValue
                + "\n3. Minimum Spend : " + minSpend
                + "\n4. Per Limit : " + perLimit
                + "\n5. Start date : " + startDate.getDate()
                + "\n6. End date : " + endDate.getDate()
                + "\n7. Publish count : " + publishCount
                + "\n8. Receive count : " + receiveCount;
    }
}


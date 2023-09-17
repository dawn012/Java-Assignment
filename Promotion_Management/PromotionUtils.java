package Promotion_Management;

import Database.DatabaseUtils;
import Driver.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PromotionUtils {
    public static ArrayList<Promotion> filteredPromotionList(LocalDate startDate, LocalDate endDate, int status) {
        ArrayList<Promotion> promotions = new ArrayList<>(); // 创建一个列表来存储 Promotion 对象

        ResultSet rs;
        Object[] params = {status};

        try {
            rs = DatabaseUtils.selectQueryById("*", "PROMOTION", "PROMOTION_STATUS = ?", params);

            while (rs.next()) {
                Promotion promotion = new Promotion();

                promotion.setPromotionId(rs.getInt("PROMOTION_ID"));
                promotion.setDescription(rs.getString("DESCRIPTION"));
                promotion.setDiscountValue(rs.getDouble("DISCOUNT_VALUE"));
                promotion.setMinSpend(rs.getDouble("MIN_SPEND"));
                promotion.setPerLimit(rs.getInt("PER_LIMIT"));
                promotion.setStartDate(new DateTime(rs.getDate("START_DATE").toLocalDate()));
                promotion.setEndDate(new DateTime(rs.getDate("START_DATE").toLocalDate()));
                promotion.setPublishCount(rs.getInt("PUBLISH_COUNT"));
                promotion.setReceiveCount(rs.getInt("RECEIVE_COUNT"));
                promotion.setPromotionStatus(rs.getInt("PROMOTION_STATUS"));

                promotions.add(promotion);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ArrayList<Promotion> filteredPromotions = new ArrayList<>();
        int count = 0;

        for (Promotion eachPromotion : promotions) {
            // All promotions
            if (startDate == null && endDate == null) {
                filteredPromotions.add(eachPromotion);
                count++;
                System.out.printf("%d.     %s\n", count, eachPromotion.getDescription());
            }

            else {
                if (eachPromotion.getStartDate().getDate().isAfter(startDate) && eachPromotion.getEndDate().getDate().isBefore(endDate)) {
                    filteredPromotions.add(eachPromotion);
                    count++;
                    System.out.printf("%d.     %s\n", count, eachPromotion.getDescription());
                }
            }
        }

        return filteredPromotions;
    }

    public static ArrayList<Promotion> validPromotionList(int custId, Scanner input) {
        ArrayList<Promotion> promotions = new ArrayList<>();

        DateTime currentDate = new DateTime();
        ResultSet rs;

        Object[] params = {currentDate.getCurrentDate(), custId};

        try {
            rs = DatabaseUtils.selectQueryById("*", "PROMOTION", "? BETWEEN START_DATE AND END_DATE AND RECEIVE_COUNT < PUBLISH_COUNT AND PROMOTION_ID NOT IN (SELECT PROMOTION_ID FROM PROMOTION_HISTORY WHERE USER_ID = ?);", params);

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
}

package Promotion_Management;

import Database.DatabaseUtils;
import Driver.DateTime;
import Security_Management.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PromotionUtils {
    public static ArrayList<Promotion> filteredPromotionList(LocalDate startDate, LocalDate endDate, int status, String action) {
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
                promotion.setEndDate(new DateTime(rs.getDate("END_DATE").toLocalDate()));
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
        boolean headerPrint = true;


        for (Promotion eachPromotion : promotions) {
            // All promotions
            if (startDate == null && endDate == null) {
                if (headerPrint) {
                    System.out.printf("\n\nSelect the promotion you want to %s: ", action);
                    headerPrint = false;
                }
                filteredPromotions.add(eachPromotion);
                count++;
                System.out.print("\n-----------------------------------------------------\n");
                System.out.printf("| %d | %-45s |", count, eachPromotion.getDescription());
            }

            else {
                if ((eachPromotion.getStartDate().getDate().isAfter(startDate) || eachPromotion.getStartDate().getDate().equals(startDate)) && (eachPromotion.getEndDate().getDate().isBefore(endDate) || eachPromotion.getEndDate().getDate().equals(endDate))){
                    filteredPromotions.add(eachPromotion);
                    count++;

                    if (headerPrint) {
                        System.out.printf("\n\nSelect the promotion you want to %s: ", action);
                        headerPrint = false;
                    }

                    System.out.print("\n-----------------------------------------------------\n");
                    System.out.printf("| %d | %-45s |", count, eachPromotion.getDescription());
                }
            }
        }

        if (count > 0) {
            System.out.print("\n-----------------------------------------------------\n");
        }

        return filteredPromotions;
    }
}

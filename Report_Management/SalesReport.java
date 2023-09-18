package Report_Management;

import Database.DatabaseUtils;
import Driver.DateTime;
import Payment_Management.Card;
import Payment_Management.Payment;
import Payment_Management.TNG;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class SalesReport extends Report {
    private DateTime searchDate;
    private double totalSales;
    private int totalOrders;

    public SalesReport() {
    }

    public SalesReport(String title, DateTime reportDate, String purpose, String conclusion, DateTime searchDate, double totalSales, int totalOrders) {
        super(title, reportDate, purpose, conclusion);
        this.searchDate = searchDate;
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        SalesReport salesReport = new SalesReport();

        salesReport.viewSalesReport(sc, salesReport);

        System.out.println(salesReport);
    }

    public ArrayList<Payment> allPayments() {
        ArrayList<Payment> payments = new ArrayList<>();

        String sql = " SELECT P.PAYMENT_ID, P.BOOKING_ID, P.PAYMENT_METHOD, P.PAYMENT_AMOUNT, P.CURRENCY, P.PAYMENT_DATE, P.PAYMENT_TIME, P.PAYMENT_STATUS, C.CARD_NO, C.EXPIRED_DATE, C.CVC, T.PHONE_NO, T.PIN_NO FROM PAYMENT P LEFT JOIN CARD C ON P.PAYMENT_ID = C.PAYMENT_ID LEFT JOIN TNG T ON P.PAYMENT_ID = T.PAYMENT_ID WHERE P.PAYMENT_STATUS = ?";
        Object[] params = {"PAID"};

        ResultSet rs;

        try {
            //rs = DatabaseUtils.selectQueryById("SELECT P.PAYMENT_ID, P.BOOKING_ID, P.PAYMENT_METHOD, P.PAYMENT_AMOUNT, P.CURRENCY, P.PAYMENT_DATE, P.PAYMENT_TIME, P.PAYMENT_STATUS, C.CARD_NO, C.EXPIRED_DATE, C.CVC, T.PHONE_NO, T.PIN_NO", "PAYMENT P LEFT JOIN CARD C ON P.PAYMENT_ID = C.PAYMENT_ID LEFT JOIN TNG T ON P.PAYMENT_ID = T.PAYMENT_ID", "P.PAYMENT_STATUS = ?", params);

            rs = DatabaseUtils.select(sql, params);

            while(rs.next()) {

                int paymentId = rs.getInt("PAYMENT_ID");
                int bookingId = rs.getInt("BOOKING_ID");
                String paymentMtd = rs.getString("PAYMENT_METHOD");
                double paymentAmount = rs.getDouble("PAYMENT_AMOUNT");
                String currency = rs.getString("CURRENCY");
                String paymentDate = rs.getString("PAYMENT_DATE");
                String paymentTime = rs.getString("PAYMENT_TIME");
                String paymentStatus = rs.getString("PAYMENT_STATUS");

                Payment payment;

                if (paymentMtd.equals("CARD")) {
                    String cardNo = rs.getString("CARD_NO");
                    String expiredDate = rs.getString("EXPIRED_DATE");
                    String cvc = rs.getString("CVC");

                    payment = new Card(paymentId, bookingId, paymentMtd, paymentAmount, currency, paymentDate, paymentTime, paymentStatus, cardNo, expiredDate, cvc);
                } else {
                    String phoneNo = rs.getString("PHONE_NO");
                    String pinNo = rs.getString("PIN_NO");

                    payment = new TNG(paymentId, bookingId, paymentMtd, paymentAmount, currency, paymentDate, paymentTime, paymentStatus, phoneNo, pinNo);
                }

                payments.add(payment);
            }

            rs.close();

            return payments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewSalesReport(Scanner sc, SalesReport salesReport) {
        System.out.println("\nSelect the type of sales report: ");
        System.out.println("1. Daily Sales Report");
        System.out.println("2. Monthly Sales Report");
        System.out.print("\nEnter your selection (0 - Back): ");
        String salesReportSelection = sc.nextLine().trim();

        switch (salesReportSelection) {
            case "0":
                break;
            case "1":
                viewDailySalesReport(sc, salesReport);
            default:
                System.out.println("Invalid input.");
        }
    }

    public void viewDailySalesReport(Scanner sc, SalesReport salesReport) {
        System.out.println("\nSelect the daily sales report: ");
        System.out.println("1. Yesterday Report");
        System.out.println("2. Custom day report");
        System.out.print("\nEnter your selection (0 - Back): ");
        String dailySalesSelection = sc.nextLine().trim();

        switch (dailySalesSelection) {
            case "0":
                break;
            case "2":
                System.out.print("Enter the date (yyyy-mm-dd): ");
                String viewDate = sc.nextLine().trim();

                int[] dateParts = DateTime.dateFormatValidator(viewDate);

                if (!(dateParts == null)) {
                    reportDate = new DateTime(dateParts[0], dateParts[1], dateParts[2]);

                    if (reportDate.isValidDate()) {
                        if (!(reportDate.getDate().equals(LocalDate.now()) || reportDate.getDate().isAfter(LocalDate.now()))) {

                            ArrayList<Payment> allPayments = allPayments();
                            double sale = 0;
                            int order = 0;

                            for (Payment payment : allPayments) {
                                if (payment.getPaymentDate().equals(viewDate)) {
                                    System.out.println(payment);
                                    sale += payment.getPaymentAmount();
                                    order++;
                                }
                            }

                            salesReport.totalSales = sale;
                            salesReport.totalOrders = order;

                        }

                        else {
                            System.out.println("Your new start date must before the end date.\n");
                        }
                    }

                    else {
                        System.out.println("Please enter valid date range.\n");
                    }
                }


                break;
            default:
                System.out.println("Invalid input.");
        }
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("$0.00");
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("-------------------------------------------------\n");
        reportBuilder.append("           Daily Sales Report - ").append(reportDate.getDate()).append("\n");
        reportBuilder.append("-------------------------------------------------\n");
        reportBuilder.append("\nTotal Sales: ").append(decimalFormat.format(totalSales));
        reportBuilder.append("\nTotal Orders: ").append(totalOrders);
        reportBuilder.append("\n-------------------------------------------------");
        reportBuilder.append("\nAverage Sales: ").append(totalSales / totalOrders);
        reportBuilder.append("\nMost Payment Method:\n");
        // Add code here to find and display the most active customers

        reportBuilder.append("-------------------------------------------------\n");

        return super.toString() + reportBuilder;
    }



}

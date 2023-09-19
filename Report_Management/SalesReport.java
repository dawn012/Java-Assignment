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
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;

public class SalesReport extends Report {
    private double totalSales;
    private int totalOrders;
    private String mostPaymentMtd;

    public SalesReport() {
    }

    public SalesReport(double totalSales, int totalOrders, String mostPaymentMtd) {
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.mostPaymentMtd = mostPaymentMtd;
    }

    public SalesReport(String title, DateTime reportDate, String conclusion, double totalSales, int totalOrders, String mostPaymentMtd) {
        super(title, reportDate, conclusion);
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.mostPaymentMtd = mostPaymentMtd;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getMostPaymentMtd() {
        return mostPaymentMtd;
    }

    public void setMostPaymentMtd(String mostPaymentMtd) {
        this.mostPaymentMtd = mostPaymentMtd;
    }

//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        SalesReport salesReport = new SalesReport();
//        SalesReport salesReport1;
//
//        salesReport1 = salesReport.viewSalesReport(sc);
//
//        System.out.println(salesReport1);
//    }

    public static ArrayList<Payment> allPayments() {
        ArrayList<Payment> payments = new ArrayList<>();

        String sql = " SELECT P.PAYMENT_ID, P.BOOKING_ID, P.PAYMENT_METHOD, P.PAYMENT_AMOUNT, P.CURRENCY, P.PAYMENT_DATE, P.PAYMENT_TIME, P.PAYMENT_STATUS, C.CARD_NO, C.EXPIRED_DATE, C.CVC, T.PHONE_NO, T.PIN_NO FROM PAYMENT P LEFT JOIN CARD C ON P.PAYMENT_ID = C.PAYMENT_ID LEFT JOIN TNG T ON P.PAYMENT_ID = T.PAYMENT_ID WHERE P.PAYMENT_STATUS = ?";
        Object[] params = {"PAID"};

        ResultSet rs;

        try {
            //rs = DatabaseUtils.selectQueryById("SELECT P.PAYMENT_ID, P.BOOKING_ID, P.PAYMENT_METHOD, P.PAYMENT_AMOUNT, P.CURRENCY, P.PAYMENT_DATE, P.PAYMENT_TIME, P.PAYMENT_STATUS, C.CARD_NO, C.EXPIRED_DATE, C.CVC, T.PHONE_NO, T.PIN_NO", "PAYMENT P LEFT JOIN CARD C ON P.PAYMENT_ID = C.PAYMENT_ID LEFT JOIN TNG T ON P.PAYMENT_ID = T.PAYMENT_ID", "P.PAYMENT_STATUS = ?", params);

            rs = DatabaseUtils.select(sql, params);

            while(rs.next()) {

                int paymentId = rs.getInt("PAYMENT_ID");
                String paymentMtd = rs.getString("PAYMENT_METHOD");
                double paymentAmount = rs.getDouble("PAYMENT_AMOUNT");
                String currency = rs.getString("CURRENCY");
                LocalDate paymentDate = LocalDate.parse(rs.getString("PAYMENT_DATE"));
                LocalTime paymentTime = LocalTime.parse(rs.getString("PAYMENT_TIME"));
                String paymentStatus = rs.getString("PAYMENT_STATUS");

                Payment payment;

                if (paymentMtd.equals("CARD")) {
                    String cardNo = rs.getString("CARD_NO");
                    String expiredDate = rs.getString("EXPIRED_DATE");
                    String cvc = rs.getString("CVC");

                    payment = new Card(paymentId, paymentMtd, paymentAmount, currency, paymentDate, paymentTime, paymentStatus, cardNo, expiredDate, cvc);
                } else {
                    String phoneNo = rs.getString("PHONE_NO");
                    String pinNo = rs.getString("PIN_NO");

                    payment = new TNG(paymentId, paymentMtd, paymentAmount, currency, paymentDate, paymentTime, paymentStatus, phoneNo, pinNo);
                }

                payments.add(payment);
            }

            rs.close();

            return payments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public SalesReport viewSalesReport(Scanner sc) {
//        System.out.println("\nSelect the type of sales report: ");
//        System.out.println("1. Daily Sales Report");
//        System.out.println("2. Monthly Sales Report");
//        System.out.print("\nEnter your selection (0 - Back): ");
//        String salesReportSelection = sc.nextLine().trim();
//
//        switch (salesReportSelection) {
//            case "0":
//                break;
//            case "1":
//                super.setTitle("Daily Sales Report");
//                return viewDailySalesReport(sc);
//            default:
//                System.out.println("Invalid input.");
//        }
//
//        return null;
//    }

//    public SalesReport viewDailySalesReport(Scanner sc) {
//        SalesReport salesReport = new SalesReport();
//        ArrayList<Payment> allPayments;
//
//        System.out.print("\nEnter the date (yyyy-mm-dd): ");
//        String viewDate = sc.nextLine().trim();
//        DateTime searchDate = new DateTime();
//
//        int[] dateParts = DateTime.dateFormatValidator(viewDate, "^\\d{4}-\\d{2}-\\d{2}$");
//
//        if (!(dateParts == null)) {
//            searchDate = new DateTime(dateParts[0], dateParts[1], dateParts[2]);
//
//            if (searchDate.isValidDate()) {
//                if (!(searchDate.getDate().equals(LocalDate.now()) || searchDate.getDate().isAfter(LocalDate.now()))) {
//                    // Check the report generated is before today
//                    allPayments = SalesReport.allPayments();
//                    double totalSales = 0;
//                    int totalOrders = 0;
//
//                    for (Payment payment : allPayments) {
//                        if (payment.getPaymentDate().equals(viewDate)) {
//                            totalSales += payment.getPaymentAmount();
//                            totalOrders++;
//                        }
//                    }
//
//                    salesReport.setTotalSales(totalSales);
//                    salesReport.setTotalOrders(totalOrders);
//                }
//
//                else {
//                    System.out.println("Your new start date must before the end date.\n");
//                }
//            }
//
//            else {
//                System.out.println("Please enter valid date range.\n");
//            }
//        }
//
//        return salesReport;
//    }

    public SalesReport calcSalesReportInfo() {
        totalSales = 0;
        totalOrders = 0;
        int countCard = 0;
        int countTNG = 0;

        if (getTitle().contains("Daily")) {
            for (Payment payment : SalesReport.allPayments()) {
                if (payment.getPaymentDate().equals(getReportDate().getDate())) {
                    totalSales += payment.getPaymentAmount();
                    totalOrders++;

                    if (payment.getPaymentMethod().equals("CARD")) {
                        countCard++;
                    }

                    else {
                        countTNG++;
                    }

                }
            }

            setConclusion("From this report we can know in " + getReportDate().getDay() + " " + getReportDate().getDate().getMonth() + " " + getReportDate().getYear() + " the total sales is RM " + totalSales + ", total orders is " + totalOrders + " and the average sales per order is RM " + (totalSales/totalOrders) + ".");
        }

        else {
            LocalDate reportDate = getReportDate().getDate();
            YearMonth reportYearMonth = YearMonth.of(reportDate.getYear(), reportDate.getMonth());

            for (Payment payment : SalesReport.allPayments()) {
                LocalDate paymentDate = payment.getPaymentDate();
                YearMonth paymentYearMonth = YearMonth.of(paymentDate.getYear(), paymentDate.getMonth());

                if (paymentYearMonth.equals(reportYearMonth)) {
                    totalSales += payment.getPaymentAmount();
                    totalOrders++;

                    if (payment.getPaymentMethod().equals("CARD")) {
                        countCard++;
                    }

                    else {
                        countTNG++;
                    }

                }
            }

            setConclusion("From this report we can know in " + getReportDate().getDate().getMonth() + " " + getReportDate().getYear() + " the total sales is RM " + totalSales + ", total orders is " + totalOrders + " and the average sales per day is RM " + (totalSales/getReportDate().getDay()) + ".");
        }

        if (countCard == countTNG) {
            mostPaymentMtd = "Credit/Debit Card & Touch 'n Go";
        } else if (countCard > countTNG) {
            mostPaymentMtd = "Credit/Debit Card";
        } else {
            mostPaymentMtd = "Touch 'n Go";
        }

        if (totalSales > 0) {
            return new SalesReport(getTitle(), getReportDate(), getConclusion(), totalSales, totalOrders, mostPaymentMtd);
        }

        return null;
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("RM 0.00");
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("-------------------------------------------------\n");
        reportBuilder.append(super.getTitle()).append(" - ");

        if (super.getTitle().contains("Daily")) {
            reportBuilder.append(getReportDate().getDay()).append(" ").append(getReportDate().getDate().getMonth()).append(" ").append(getReportDate().getYear());
        } else {
            reportBuilder.append(getReportDate().getDate().getMonth()).append(" ").append(getReportDate().getDate().getYear());
        }

        reportBuilder.append("\n-------------------------------------------------");
        reportBuilder.append("\nTotal Sales: ").append(decimalFormat.format(totalSales));
        reportBuilder.append("\nTotal Orders: ").append(totalOrders);
        reportBuilder.append("\n-------------------------------------------------");

        if (super.getTitle().contains("Daily")) {
            reportBuilder.append("\nAverage Sales (per order): ").append(decimalFormat.format(totalSales / totalOrders));
        } else {
            reportBuilder.append("\nAverage Sales (per day): ").append(decimalFormat.format(totalSales / getReportDate().getDay()));
            reportBuilder.append("\nAverage Order (per day): ").append(decimalFormat.format(totalOrders / getReportDate().getDay()));
        }

        reportBuilder.append("\nMost Payment Method: ").append(mostPaymentMtd);
        // Add code here to find and display the most active customers

        reportBuilder.append("\n-------------------------------------------------\n");

        reportBuilder.append(String.format("\nConclusion: \n%s\n", super.getConclusion()));

        return super.toString() + reportBuilder; // 返回完整的字符串
    }
}

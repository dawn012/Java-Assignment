package Receipt_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;
import Payment_Management.Payment;
import Ticket_Managemnet.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Receipt {
    private static int nextReceiptId;
    private int receiptId;
    private LocalDate receiptDate;
    private LocalTime receiptTime;
    private Payment payment;

    public Receipt() {
    }

    public Receipt(Payment payment) {
        this.receiptId = ++nextReceiptId;
        this.receiptDate = LocalDate.now();
        this.receiptTime = LocalTime.now();
        this.payment = payment;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public LocalTime getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(LocalTime receiptTime) {
        this.receiptTime = receiptTime;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    static {
        Object[] params = null;

        ResultSet rs;

        try {
            rs = DatabaseUtils.selectQueryById("COUNT(*)", "RECEIPT", null, params);

            // 获取整数结果
            if (rs.next()) {
                nextReceiptId = rs.getInt(1);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printReceipt(){
        System.out.println("\n\t\t-------------------------------------------");
        System.out.println("\t\t|\t\t\t\tReceipt\t\t\t\t\t  |");
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Receipt ID   : %04d\n", receiptId);
        System.out.printf("\t\t Payment ID   : %04d\n", payment.getPaymentId());
        System.out.printf("\t\t Booking ID   : %04d\n", payment.getBooking().getBookingId());
        System.out.printf("\t\t Payment Date : %s\n", payment.getPaymentDate());
        System.out.printf("\t\t Payment Time : %tT\n", payment.getPaymentTime().truncatedTo(ChronoUnit.SECONDS));
        System.out.printf("\t\t Receipt Date : %s\n", receiptDate);
        System.out.printf("\t\t Receipt Time : %tT\n", receiptTime.truncatedTo(ChronoUnit.SECONDS));
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Hall ID      : %d\n", payment.getBooking().getTicketList().get(0).getTimeTable().getHall().getHallID());
        System.out.printf("\t\t Movie Name   : %s\n\n", payment.getBooking().getTicketList().get(0).getTimeTable().getMovie().getMvName().getName());
        System.out.println("\t\t Ticket Type\tUnit Price\tQty\t   Subtotal");

        if(payment.getBooking().getAdultTicketQty() > 0)
            System.out.printf("\t\t Adult Ticket\tRM%6.2f     %2d\t   RM%6.2f\n", payment.getBooking().getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 1.2, payment.getBooking().getAdultTicketQty(), payment.getBooking().getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 1.2 * payment.getBooking().getAdultTicketQty());

        if(payment.getBooking().getChildTicketQty() > 0)
            System.out.printf("\t\t Child Ticket\tRM%6.2f     %2d\t   RM%6.2f\n", payment.getBooking().getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 0.8, payment.getBooking().getChildTicketQty(), payment.getBooking().getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 0.8 * payment.getBooking().getChildTicketQty());

        System.out.print("\n\t\t Seat ID : ");

        for(Ticket t : payment.getBooking().getTicketList()){
            System.out.print(t.getSeat().getSeatId()+"  ");
        }

        System.out.println("\n\t\t-------------------------------------------");

        if (payment.getBooking().getPromotion() != null) {
            // Get original Price
            System.out.printf("\t\t Total   \t\t\t\t\t\t  RM%6.2f\n", payment.getPaymentAmount() + payment.getBooking().getPromotion().getDiscountValue());
            System.out.printf("\t\t Discount\t\t\t\t\t\t- RM%6.2f\n", payment.getBooking().getPromotion().getDiscountValue());
        }

        else {
            System.out.printf("\t\t Total   \t\t\t\t\t\t  RM%6.2f\n", payment.getPaymentAmount());
        }

        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Total Amount\t\t\t\t\t  RM%6.2f\n", payment.getBooking().getTotalPrice());
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Payment Method  \t\t%18s\n", payment.getPaymentMethod());
        System.out.printf("\t\t Amount Paid \t\t\t\t\t  RM%6.2f\n", payment.getPaymentAmount());
        System.out.println("\t\t-------------------------------------------");
    }

    public boolean add() {
        String sql = "INSERT INTO RECEIPT (PAYMENT_ID, TOTAL_COST, AMOUNT_PAID, RECEIPT_DATE, RECEIPT_TIME) VALUES (?, ?, ?, ?, ?)";
        Object[] params = {payment.getPaymentId(), payment.getBooking().getTotalPrice(), payment.getPaymentAmount(), receiptDate, receiptTime.truncatedTo(ChronoUnit.SECONDS)};

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
}

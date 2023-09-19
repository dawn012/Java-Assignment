package Payment_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;
import Driver.DateTime;
import Ticket_Managemnet.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Payment {
    protected Booking booking;
    protected int paymentId;
    private static int nextPaymentId;
    protected String paymentMethod;
    protected double paymentAmount;
    protected String currency;
    protected LocalDate paymentDate;
    protected LocalTime paymentTime;
    protected String paymentStatus;

    public Payment() {
    }

    public Payment(Booking booking, String paymentMethod, double paymentAmount, String currency, String paymentStatus) {
        this.paymentId = ++nextPaymentId;
        this.booking =  booking;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.currency = currency;
        this.paymentDate = LocalDate.now();
        this.paymentTime = LocalTime.now();
        this.paymentStatus = paymentStatus;
    }

    public Payment(int paymentId, String paymentMethod, double paymentAmount, String currency, LocalDate paymentDate, LocalTime paymentTime, String paymentStatus) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.currency = currency;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.paymentStatus = paymentStatus;
    }

    public Payment(int paymentId, Booking booking, String paymentMethod, double paymentAmount, String currency, LocalDate paymentDate, LocalTime paymentTime, String paymentStatus) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.currency = currency;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.paymentStatus = paymentStatus;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalTime paymentTime) {
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
        Object[] params = {booking.getBookingId(), paymentMethod, paymentAmount, currency, paymentDate, paymentTime, paymentStatus};

        try {
            DatabaseUtils.insertQuery(sql, params);

        } catch (SQLException e) {
            System.out.println("\nOops! Something went wrong. Please try again!");
            throw new RuntimeException(e);
        }
    }

    public void printPaymentDetail(double originalTotal){
        System.out.println("\n\t\t-------------------------------------------");
        System.out.println("\t\t\t\t\tPayment Details");
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Payment ID   : %04d\n", paymentId);
        System.out.printf("\t\t Booking ID   : %04d\n", booking.getBookingId());
        System.out.printf("\t\t Booking Date : %s\n", booking.getBookingDateTime().getDate());
        System.out.printf("\t\t Booking Time : %-8s\n", booking.getBookingTime());
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Hall ID      : %d\n", booking.getTicketList().get(0).getTimeTable().getHall().getHallID());
        System.out.printf("\t\t Movie Name   : %s\n\n", booking.getTicketList().get(0).getTimeTable().getMovie().getMvName().getName());
        System.out.println("\t\t Ticket Type\tUnit Price\tQty\t   Subtotal");

        if(booking.getAdultTicketQty()>0)
            System.out.printf("\t\t Adult Ticket\tRM%6.2f     %2d\t   RM%6.2f\n", booking.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 1.2, booking.getAdultTicketQty(), booking.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 1.2 * booking.getAdultTicketQty());

        if(booking.getChildTicketQty()>0)
            System.out.printf("\t\t Child Ticket\tRM%6.2f     %2d\t   RM%6.2f\n", booking.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 0.8, booking.getChildTicketQty(), booking.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 0.8 * booking.getChildTicketQty());

        System.out.print("\n\t\t Seat ID : ");

        for(Ticket t : booking.getTicketList()){
            System.out.print(t.getSeat().getSeatId()+"  ");
        }

        System.out.println("\n\t\t-------------------------------------------");
        System.out.printf("\t\t\tTotal \t\t\t\t\t   RM%6.2f\n", originalTotal);

        if (booking.getPromotion() != null) {
            System.out.printf("\t\t\tDiscount\t\t\t\t\t- RM%6.2f\n", booking.getPromotion().getDiscountValue());
            System.out.println("\t\t-------------------------------------------");
            System.out.printf("\t\t\tTotal Amount : \t\t\t\tRM%6.2f\n", booking.getTotalPrice());
        }

        System.out.println("\t\t-------------------------------------------");
    }

//    private boolean makePayment(Scanner sc, Booking booking) {
//        // Deduct discount value
//        if (promotion != null) {
//            booking.setTotalPrice(booking.getTotalPrice() - promotion.getDiscountValue());
//        }
//
//        String paymentMethod;
//
//        // Payment payment;
//        Payment payment;
//        Payment validPayment = null;
//
//        boolean back;
//        boolean successPayment = false;
//
//        do {
//            System.out.println("\nPayment Method: ");
//            System.out.println("1. Credit/Debit Card");
//            System.out.println("2. Touch 'n Go");
//            System.out.print("\nSelect your payment method (0 - Back): ");
//
//            paymentMethod = sc.nextLine().trim();
//
//            do {
//                back = false;
//
//                switch (paymentMethod) {
//                    case "0":
//                        return false;
//
//                    case "1":
//                        // Process Credit/Debit Card Payment
//                        while (true) {
//                            payment = cardPaymentInfo(sc);
//
//                            validPayment = validPayment(payment, booking);
//
//                            if(validPayment != null) {
//                                back = true;
//                                successPayment = true;
//                                break;
//                            }
//
//                            String changePaymentMtd;
//
//                            do {
//                                System.out.println("\nDo you want to change your payment method? (Y / N)");
//                                System.out.print("Answer: ");
//                                String answer = sc.next().trim();
//                                sc.nextLine();
//
//                                changePaymentMtd = SystemClass.askForContinue(answer);
//                            } while (changePaymentMtd.equals("Invalid"));
//
//                            if (changePaymentMtd.equals("Y")) {
//                                back = true;
//                                break;
//                            }
//                        }
//
//                        break;
//                    case "2":
//                        // Process TNG Payment
//                        payment = tngPaymentInfo(sc);
//
//                        validPayment = validPayment(payment, booking);
//
//                        back = true;
//                        successPayment = true;
//
//                        break;
//
//                    default:
//                        System.out.println("Invalid selection. Please retry.");
//                        back = true;
//                }
//
//            } while (!back);
//
//            if (back) {
//                back = false;
//            }
//
//        } while (!back && !successPayment);
//
//        String ctnMakePayment;
//        String cancelPayment;
//
//        do {
//            do {
//                System.out.println("\nContinue to make payment? (Y / N)");
//                System.out.print("Answer: ");
//                String answer = sc.next().trim();
//                sc.nextLine();
//
//                ctnMakePayment = SystemClass.askForContinue(answer);
//            } while (ctnMakePayment.equals("Invalid"));
//
//            if (ctnMakePayment.equals("Y")) {
//                // Confirm to make payment
//                validPayment.addPayment();
//                validPayment.pay();
//
//                if(promotion != null) {
//                    // User use promotion code, update promotion code status
//                    promotion.custApplyPromotion(validPayment.getPaymentId(), custId);
//                }
//
//                System.out.println("\nPayment Successfully! Thanks for your payment.");
//
//                return true;
//            }
//
//            else {
//                do {
//                    System.out.println("\nConfirm to cancel your payment? (Y / N)");
//                    System.out.print("Answer: ");
//                    String answer = sc.next().trim();
//                    sc.nextLine();
//
//                    cancelPayment = SystemClass.askForContinue(answer);
//                } while (cancelPayment.equals("Invalid"));
//            }
//        } while (cancelPayment.equals("N"));
//
//        return false;
//    }
//
//    private static Payment validPayment(Payment payment, Booking booking) {
//        DateTime dateTime = new DateTime();
//
//        if (payment instanceof Card) {
//            Card card = (Card) payment;
//            card.setPaymentAmount(booking.getTotalPrice());
//
//            if(CardValidator.stripeValidator(card.createPaymentIntent())) {
//                return new Card(booking, "CREDIT/DEBIT CARD", booking.getTotalPrice(), "MYR", dateTime.getCurrentDate(), dateTime.getCurrentTime(), "PAID", card.getCardNo(), card.getExpiredDate(), card.getCvc(), card.getEmail());
//            }
//
//        } else {
//            TNG tng = (TNG) payment;
//
//            return new TNG(booking.getBooking_id(), "TNG", booking.getTotalPrice(), "MYR", dateTime.getCurrentDate(), dateTime.getCurrentTime(), "PAID", tng.getPhoneNo(), tng.getPinNo());
//        }
//
//        return null;
//    }
}

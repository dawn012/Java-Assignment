package Payment_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Card extends Payment {
    private String cardNo;
    private String expiredDate;
    private String cvc;

    public Card() {
        super();
    }

    public Card(String cardNo, String expiredDate, String cvc) {
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
    }

    public Card(Booking booking, String paymentMethod, double paymentAmount, String currency, String paymentStatus, String cardNo, String expiredDate, String cvc) {
        super(booking, paymentMethod, paymentAmount, currency, paymentStatus);
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
    }

    public Card(int paymentId, String paymentMethod, double paymentAmount, String currency, LocalDate paymentDate, LocalTime paymentTime, String paymentStatus, String cardNo, String expiredDate, String cvc) {
        super(paymentId, paymentMethod, paymentAmount, currency, paymentDate, paymentTime, paymentStatus);
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
    }

    public Card(int paymentId, Booking booking, String paymentMethod, double paymentAmount, String currency, LocalDate paymentDate, LocalTime paymentTime, String paymentStatus, String cardNo, String expiredDate, String cvc) {
        super(paymentId, booking, paymentMethod, paymentAmount, currency, paymentDate, paymentTime, paymentStatus);
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    @Override
    public void pay() {
        String sql = "INSERT INTO CARD (PAYMENT_ID, CARD_NO, EXPIRED_DATE, CVC) VALUES (?,?,?,?)";
        Object[] params = {paymentId, cardNo, expiredDate, cvc};

        try {
            DatabaseUtils.insertQuery(sql, params);

        } catch (SQLException e) {
            System.out.println("\nOops! Something went wrong. Please try again!");
            throw new RuntimeException(e);
        }
    }

    private int getExpiredMonth(){
        // Date user entered
        String[] monthYear = expiredDate.split("/");

        return Integer.parseInt(monthYear[0]);
    }

    private int getExpiredYear(){
        // Date user entered
        String[] monthYear = expiredDate.split("/");

        return Integer.parseInt(monthYear[1]);
    }

    private PaymentMethod createPaymentMethod() {
        try {
            PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams.builder()
                    .setType(PaymentMethodCreateParams.Type.CARD)
                    .setCard(PaymentMethodCreateParams.CardDetails.builder()
                            .setNumber(cardNo)
                            .setExpMonth((long) getExpiredMonth())
                            .setExpYear((long) getExpiredYear())
                            .setCvc(cvc)
                            .build())
                    .build();

            return PaymentMethod.create(paymentMethodParams);
        } catch (StripeException e) {
            System.out.println("\nPayment Failed. Your card info is invalid.");
            return null;
        }
    }

    private Customer createCustomer() {
        try {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(booking.getCustomer().getEmail())
                    .build();

            return Customer.create(customerParams);

        } catch (StripeException e) {
            System.out.println("\nPayment Failed. Your email is invalid.");
            return null;
        }
    }

    public PaymentIntent createPaymentIntent() {
        if (createPaymentMethod() != null && createCustomer() != null) {
            try {
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount((long) paymentAmount * 100)
                        .setCurrency("myr")
                        .setPaymentMethod(createPaymentMethod().getId())
                        .setCustomer(createCustomer().getId())
                        .build();

                return PaymentIntent.create(params);

            } catch (StripeException e) {
                System.out.println("\nError creating payment intent: " + e.getMessage());
                return null;
            }
        }

        return null;
    }
}


package Payment_Management;

import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

public class CardValidator {
    private CardValidator() {
    }

    public static boolean stripeValidator(PaymentIntent createPaymentIntent) {
        // Set Secret Key
        StripeAPIKey.init();

        if (createPaymentIntent != null) {
            return true;
        }

        return false;
    }

    public static boolean cardNoFormatValidator(String cardNo) {
        String regex = "^\\d{16}$";

        if(cardNo.matches(regex)) {
            return true;
        }

        System.out.println("Invalid card number format. Please enter 16-digits number.");

        return false;
    }

    public static boolean expiredDateFormatValidator(String expiredDate) {
        String regex = "^\\d{2}/\\d{2}$";

        if(expiredDate.matches(regex)) {
            return true;
        }

        System.out.println("Invalid expired date format. Please follow the format.");

        return false;
    }

    public static boolean cvcFormatValidator(String cvc) {
        String regex = "^\\d{3}$";

        if(cvc.matches(regex)) {
            return true;
        }

        System.out.println("Invalid cvc format. Please enter 3-digits number.");

        return false;
    }
}

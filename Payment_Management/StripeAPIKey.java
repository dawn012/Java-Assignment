package Payment_Management;

import com.stripe.Stripe;

public class StripeAPIKey {
    private static final String SECRET_KEY ="sk_test_51MkK28LVFpC4e2xLKyOXhFYXLSuNsq09s3Xe9jlND5s962q9Pbz7e7bzRgolCdwykFg6eoi9YcgpVOoYLp5biAl900D4JVXM3J";

    public StripeAPIKey() {
    }

    static {
        // Initialize Stripe with the secret key
        Stripe.apiKey = SECRET_KEY;
    }

    public static void init() {
        // Empty method to trigger static block initialization
    }
}

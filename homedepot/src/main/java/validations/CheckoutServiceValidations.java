package validations;

public class CheckoutServiceValidations {

    public static void validateRentalDayCount(int rentalDayCount) throws Exception {
        if (rentalDayCount  < 1) {
            throw new Exception("Rental day count is not 1 or greater");
        }
    }

    public static void validateDiscountPercentage(int discount) throws Exception {
        if (discount < 0 || discount > 100) {
            throw new Exception("Discount percent is not in the range 0-100");
        }
    }
}

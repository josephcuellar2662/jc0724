package service;

import model.RentalAgreement;
import model.Tool;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    private CheckoutService testObject = new CheckoutService();

    @Test
    public void testCaseOne() {
        RentalAgreement result = testObject.checkout(Tool.JAKR, LocalDate.of(2015, 9, 3), 5, 101);
        assertNull(result);
    }

    @Test
    public void testCaseTwo() {
        RentalAgreement result = testObject.checkout(Tool.LADW, LocalDate.of(2020, 7, 2), 3, 10);
        assertEquals(3, result.getChargeDays());
        assertEquals(5.97, result.getPreDiscountCharge().doubleValue());
        assertEquals(0.60, result.getDiscountAmount().doubleValue());
        assertEquals(5.37, result.getFinalCharge().doubleValue());

    }

    @Test
    public void testCaseThree() {
        RentalAgreement result = testObject.checkout(Tool.CHNS, LocalDate.of(2015, 7, 2), 5, 25);
        assertEquals(4, result.getChargeDays());
        assertEquals(5.96, result.getPreDiscountCharge().doubleValue());
        assertEquals(1.49, result.getDiscountAmount().doubleValue());
        assertEquals(4.47, result.getFinalCharge().doubleValue());
    }

    @Test
    public void testCaseFour() {
        RentalAgreement result = testObject.checkout(Tool.JAKD, LocalDate.of(2015, 9, 3), 6, 0);
        assertEquals(3, result.getChargeDays());
        assertEquals(8.97, result.getPreDiscountCharge().doubleValue());
        assertEquals(0.0, result.getDiscountAmount().doubleValue());
        assertEquals(8.97, result.getFinalCharge().doubleValue());
    }

    @Test
    public void testCaseFive() {
        RentalAgreement result = testObject.checkout(Tool.JAKR, LocalDate.of(2015, 7, 2), 9, 0);
        assertEquals(6, result.getChargeDays());
        assertEquals(17.94, result.getPreDiscountCharge().doubleValue());
        assertEquals(0.0, result.getDiscountAmount().doubleValue());
        assertEquals(17.94, result.getFinalCharge().doubleValue());
    }

    @Test
    public void testCaseSix() {
        RentalAgreement result = testObject.checkout(Tool.JAKR, LocalDate.of(2020, 7, 2), 5, 50);
        assertEquals(3, result.getChargeDays());
        assertEquals(8.97, result.getPreDiscountCharge().doubleValue());
        assertEquals(4.49, result.getDiscountAmount().doubleValue());
        assertEquals(4.49, result.getFinalCharge().doubleValue());
    }

}
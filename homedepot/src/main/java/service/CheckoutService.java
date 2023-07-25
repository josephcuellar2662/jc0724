package service;

import model.Tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CheckoutService {

    public void checkout(Tool tool, LocalDate checkoutDate, int numberOfDays, int discount) {

        System.out.println("Tool code: " + tool.getToolCode());
        System.out.println("Tool type: " + tool.getToolType());
        System.out.println("Tool brand: " + tool.getBrand());
        System.out.println("Rental days: " + numberOfDays);
        System.out.println("Check out date: " + checkoutDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        String formattedDate = formatter.format(checkoutDate.plusDays(numberOfDays));
        System.out.println("Due date: " + formattedDate);


        System.out.println("Daily rental charge: " + tool.getDailyCharge());
        System.out.println("Charge days: " + countChardDays());
        System.out.println("Pre-discount charge: " + calculatePreDiscountCharge());
        System.out.println("Discount percent: " + discount + "%");
        System.out.println("Discount amount: " + calculateDiscountAmount());
        System.out.println("Final Charge: " + calculateFinalCharge());
    }

    private int countChardDays() {

        return 0;
    }

    private double calculatePreDiscountCharge() {

        return 0.0;
    }

    private double calculateDiscountAmount() {
        return 0.0;
    }

    private double calculateFinalCharge() {
        return 0.0;
    }

//    public static void main(String args[]) {
//        LocalDate checkoutDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
//        String formattedDate = formatter.format(checkoutDate.plusDays(3));
//        System.out.println("Due date: " + formattedDate);
//    }
}

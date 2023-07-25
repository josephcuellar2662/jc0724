package service;

import model.Tool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;

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


        System.out.println("Daily rental charge: $" + tool.getDailyCharge());

        int chargeDays = countChargeDays(tool, checkoutDate, numberOfDays);
        System.out.println("Charge days: " + chargeDays);

        double preDiscountCharge = calculatePreDiscountCharge(tool, chargeDays);
        System.out.println("Pre-discount charge: $" + String.format("%.2f", preDiscountCharge));
        System.out.println("Discount percent: " + discount + "%");

        double discountAmount = calculateDiscountAmount(discount, preDiscountCharge);
        System.out.println("Discount amount: " + String.format("%.2f", discountAmount));

        System.out.println("Final Charge: $" + String.format("%.2f", (preDiscountCharge - discountAmount)));
    }

    private int countChargeDays(Tool tool, LocalDate checkoutDate, int numberOfDays) {
        int count = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        for(int i=1; i<=numberOfDays; i++) {

            String dayOfWeek = checkoutDate.plusDays(i).getDayOfWeek().toString();
            boolean isWeekend = checkIsWeekend(dayOfWeek);
            boolean isHoliday = checkIsHoliday(checkoutDate);

            if (tool.isWeekendCharge() && isWeekend) {
                count += 1;
            }
            else if (tool.isWeekdayCharge() && !isWeekend) {
                count += 1;
            }
            else if (tool.isHolidayCharge() && isHoliday) {
                count += 1;
            }

        }

        return count;
    }

    private boolean checkIsWeekend(String dayOfWeek) {

        if (dayOfWeek.equalsIgnoreCase("SATURDAY") || dayOfWeek.equalsIgnoreCase("SUNDAY")) {
            return true;
        }

        return false;
    }

    private boolean checkIsHoliday(LocalDate date) {
        String month = date.getMonth().toString();
        String dayOfWeek = date.getDayOfWeek().toString();
        int dayOfMonth = date.getDayOfMonth();

        // Independence Day
        if (month.equalsIgnoreCase("July") && dayOfMonth == 4) {
            return true;
        }

        // Labor Day
        LocalDate firstMonday = date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        if (month.equalsIgnoreCase("September") && dayOfWeek.equalsIgnoreCase(firstMonday.getDayOfWeek().toString())) {
            return true;
        }

        return false;
    }

    private double calculatePreDiscountCharge(Tool tool, int chargeDays) {

        return tool.getDailyCharge() * chargeDays;
    }

    private double calculateDiscountAmount(int discount, double preDiscountCharge) {
        return preDiscountCharge * ((double)discount/100);
    }

    public static void main(String args[]) {
        CheckoutService checkoutService = new CheckoutService();
        checkoutService.checkout(Tool.JAKR, LocalDate.now(), 5, 100);
    }
}

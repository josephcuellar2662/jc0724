package service;

import model.RentalAgreement;
import model.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;

import static validations.CheckoutServiceValidations.validateDiscountPercentage;
import static validations.CheckoutServiceValidations.validateRentalDayCount;

public class CheckoutService {

    public RentalAgreement checkout(Tool tool, LocalDate checkoutDate, int numberOfDays, int discount) {
        RentalAgreement rentalAgreement = new RentalAgreement();
        try {
            validateRentalDayCount(numberOfDays);
            validateDiscountPercentage(discount);

            rentalAgreement.setToolCode(tool.getToolCode());
            rentalAgreement.setToolType(tool.getToolType());
            rentalAgreement.setToolBrand(tool.getBrand());
            rentalAgreement.setRentalDays(numberOfDays);
            rentalAgreement.setCheckoutDate(checkoutDate);
            rentalAgreement.setDueDate(checkoutDate.plusDays(numberOfDays));
            rentalAgreement.setDailyRentalCharge(new BigDecimal(tool.getDailyCharge()).setScale(2, RoundingMode.HALF_UP));
            rentalAgreement.setDiscountPercent(discount);

            int chargeDays = countChargeDays(tool, checkoutDate, numberOfDays);
            rentalAgreement.setChargeDays(chargeDays);

            double preDiscountCharge = calculatePreDiscountCharge(tool, chargeDays);
            rentalAgreement.setPreDiscountCharge(new BigDecimal(preDiscountCharge).setScale(2, RoundingMode.HALF_UP));

            double discountAmount = calculateDiscountAmount(discount, preDiscountCharge);
            rentalAgreement.setDiscountAmount(new BigDecimal(discountAmount).setScale(2, RoundingMode.HALF_UP));

            double finalCharge = preDiscountCharge - discountAmount;
            rentalAgreement.setFinalCharge(new BigDecimal(finalCharge).setScale(2, RoundingMode.HALF_UP));

            printRentalAgreement(rentalAgreement);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return rentalAgreement;
    }

    private void printRentalAgreement(RentalAgreement rentalAgreement) {
        System.out.println("Tool code: " + rentalAgreement.getToolCode());
        System.out.println("Tool type: " + rentalAgreement.getToolType());
        System.out.println("Tool brand: " + rentalAgreement.getToolBrand());
        System.out.println("Rental days: " + rentalAgreement.getRentalDays());
        System.out.println("Check out date: " + rentalAgreement.getCheckoutDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        String formattedDueDate = formatter.format(rentalAgreement.getCheckoutDate().plusDays(rentalAgreement.getRentalDays()));
        System.out.println("Due date: " + formattedDueDate);

        System.out.println("Daily rental charge: $" + rentalAgreement.getDailyRentalCharge());
        System.out.println("Charge days: " + rentalAgreement.getChargeDays());
        System.out.println("Pre-discount charge: $" + rentalAgreement.getPreDiscountCharge());
        System.out.println("Discount percent: " + rentalAgreement.getDiscountPercent() + "%");
        System.out.println("Discount amount: " + rentalAgreement.getDiscountAmount());
        System.out.println("Final Charge: $" + rentalAgreement.getFinalCharge());


    }

    private int countChargeDays(Tool tool, LocalDate checkoutDate, int numberOfDays) {

        LocalDate dueDate = checkoutDate.plusDays(numberOfDays);
        int count = 0;
        String holidayDayOfWeek = null;
        String holidayDayOfWeekYesterday = null;

        for(int i=1; i<=numberOfDays; i++) {
            LocalDate currDate = checkoutDate.plusDays(i);
            String dayOfWeek = currDate.getDayOfWeek().toString();
            boolean isHolidayToday = checkIsHoliday(currDate);
            boolean isHolidayTomorrow = checkIsHoliday(currDate.plusDays(1));
            boolean isHolidayYesterday = checkIsHoliday(currDate.plusDays(-1));

            if (isHolidayToday) {
                holidayDayOfWeek = dayOfWeek;
            } else if (isHolidayTomorrow) {
                holidayDayOfWeek = currDate.plusDays(1).getDayOfWeek().toString();
            }
            if (isHolidayYesterday) {
                holidayDayOfWeekYesterday = currDate.plusDays(-1).getDayOfWeek().toString();
            }

            // skip weekend when it is holiday since it has already been account for on Friday or Monday as
            // per requirement
            if (checkIsWeekend(dayOfWeek) && isHolidayToday && tool.isHolidayCharge())
                continue;

            if (!checkIsWeekend(dayOfWeek)) {
                // increment count when today is not a holiday on a weekday
                if (!isHolidayToday) {
                    count += 1;
                } else {
                    // increment count when today is holiday and isHolidayCharge is true
                    if (tool.isHolidayCharge())
                        count += 1;
                }
            } else {
                // increment count when today is the weekend and isWeekendCharge is true
                if (tool.isWeekendCharge()) {
                    count += 1;
                }
            }

            // increment count when today is Friday and tomorrow is a Holiday
            if (dayOfWeek.equalsIgnoreCase("Friday")
                    && holidayDayOfWeek != null
                    && holidayDayOfWeek.equalsIgnoreCase("Saturday")
                    && currDate.isBefore(dueDate)) {
                if ((tool.isHolidayCharge() && isHolidayTomorrow)) {
                    count += 1;
                }
            }
            // increment count when today is Monday and yesterday was a holiday
            else if (dayOfWeek.equalsIgnoreCase("Monday")
                    && holidayDayOfWeekYesterday != null
                    && holidayDayOfWeekYesterday.equalsIgnoreCase("Sunday")) {
                if ((tool.isHolidayCharge() && isHolidayYesterday)) {
                    count += 1;
                }
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

}

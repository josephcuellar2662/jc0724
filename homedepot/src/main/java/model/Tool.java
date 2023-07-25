package model;

import java.math.BigDecimal;

public enum Tool {
    CHNS("CHNS", "Chainsaw", "Stihl", new BigDecimal(1.49), true, false, true),
    LADW("LADW","Ladder", "Werner", new BigDecimal(1.99), true, true, false),
    JAKD("JAKD", "Jackhammer", "DeWalt", new BigDecimal(2.99), true, false, false),
    JAKR("JAKR","Jackhammer", "Ridgid", new BigDecimal(2.99), true, false, false);

    private String toolCode;
    private String toolType;
    private String brand;
    private BigDecimal dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    Tool(String toolCode, String toolType, String brand, BigDecimal dailyCharge, boolean weekdayCharge,
         boolean weekendCharge, boolean holidayCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        return holidayCharge;
    }
}

package Zkart.deals;

import java.util.Locale;

public class Deal {

    private long id;
    private String status;
    private String dealName;
    private String discountCode;
    private double discountPercent;
    private int ordersAfterDealExpires;
    private int lastValidInvoiceNumber; //Internal Invoice count for each customer

    public Deal(String dealName, int minDiscount, int maxDiscount, int expiryCount) {

        int discountCodeLength = 6;
        this.dealName = dealName;
        this.discountCode = createDiscountCode(discountCodeLength);
        this.discountPercent = createPercentageOfDiscount(minDiscount, maxDiscount);
        // Can use the discount code only for the next "expiryCount" number of transactions.
        this.ordersAfterDealExpires = expiryCount;
    }

    public Deal(long id, String dealName, String discountCode, double discountPercent, int ordersAfterDealExpires) {
        this.id = id;
        this.dealName = dealName;
        this.discountCode = discountCode;
        this.discountPercent = discountPercent;
        this.ordersAfterDealExpires = ordersAfterDealExpires;
    }

    private String createDiscountCode (int codeLength) {

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower =  upper.toLowerCase(Locale.ROOT);
        String number = "0123456789";
        String alphaNumericString = upper + lower + number;

        String discountCode = "";

        for (int i=0; i<codeLength; i++) {
            int randomNumber = (int)Math.floor(Math.random() * alphaNumericString.length());
            discountCode += alphaNumericString.charAt(randomNumber);
        }

        return discountCode;
    }

    private double createPercentageOfDiscount (int rangeStart, int rangeEnd) {

        double percentage = (double)Math.floor( Math.random() * (rangeEnd - rangeStart + 1) + rangeStart );
        return (double)(percentage/100);
    }

    //CalculateEligibility



    // Getters and Setters


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getOrdersAfterDealExpires() {
        return ordersAfterDealExpires;
    }

    public void setOrdersAfterDealExpires(int ordersAfterDealExpires) { this.ordersAfterDealExpires = ordersAfterDealExpires; }

    // Only applicable when associated with a customer

    public int getLastValidInvoiceNumber() { return lastValidInvoiceNumber; }

    public void setLastValidInvoiceNumber(int lastValidInvoiceNumber) {
        this.lastValidInvoiceNumber = lastValidInvoiceNumber; }

    public String getStatus() {  return status; }

    public void setStatus(String status) { this.status = status; }
}

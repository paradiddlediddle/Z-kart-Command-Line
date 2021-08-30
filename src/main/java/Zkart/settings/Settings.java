package Zkart.settings;

public class Settings {

    public static int reorderLimit = 16;
    public static double dealOfTheMomentOfferPercentage = 0.10;


    // Getters and Setters

    public static int getReorderLimit() { return reorderLimit; }

    public static void setReorderLimit(int reorderLimit) { Settings.reorderLimit = reorderLimit; }

    public static double getDealOfTheMomentOfferPercentage() { return dealOfTheMomentOfferPercentage; }

    public static void setDealOfTheMomentOfferPercentage(int dealOfTheMomentOfferPercentage) {
        Settings.dealOfTheMomentOfferPercentage = dealOfTheMomentOfferPercentage; }



}

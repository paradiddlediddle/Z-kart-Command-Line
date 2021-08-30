package Zkart.deals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DealManager {

    private static List<Deal> allDeals = new ArrayList<>();

    public static void createDeals () {
        System.out.println("-x-x-Create a Deal-x-x-");
        Scanner dealScanner = new Scanner(System.in);
        String dealName;
        int minimumDiscount, maximumDiscount,ordersAfterDealExpires;

        System.out.println("\n\nPlease fill the following details:\n");
        // Deal Name
        while (true){
            System.out.print("1. Deal name: ");
            dealName = dealScanner.next();
            if (dealName.length() > 0 && dealName.length() < 20) { break; }
            else { System.out.println("Enter a name within 20 Characters!");}
        }
        //Minimum Discount
        while (true){
            System.out.print("\n(Enter a number between 1-100)\n2. Enter the minimum discount :");
            minimumDiscount = dealScanner.nextInt();
            if (minimumDiscount > 0 && minimumDiscount <= 100) { break; }
            else { System.out.println("Please enter a number between 1-100");}
        }
        //Maximum Discount
        while (true){
            System.out.print("\n(minimum Discount < maximum discount <= 100)\n2. Enter the maximum discount :");
            maximumDiscount = dealScanner.nextInt();
            if (maximumDiscount >= minimumDiscount && maximumDiscount <= 100) { break; }
            else { System.out.println("Please enter a number higher than the minimum discount percent");}
        }
        //Expiry count
        while (true){
            System.out.print("\nEnter the number of orders after which the deal should expire (1-10)");
            System.out.print("\n Order expires after: ");
            ordersAfterDealExpires = dealScanner.nextInt();
            if (ordersAfterDealExpires > 0 && ordersAfterDealExpires <= 10) { break; }
            else { System.out.println("Please enter a number higher than the minimum discount percent");}
        }

        // All inputs received
        Deal deal = new Deal(dealName, minimumDiscount, maximumDiscount, ordersAfterDealExpires);
        DealRepository dealRepository = new DealRepository();
        dealRepository.addDealToDB(deal);
    }



    //Getters and Setters

    public static List<Deal> getAllDeals() {

        return allDeals;
    }

    public static void setAllDeals(List<Deal> allDeals) {
        DealManager.allDeals = allDeals;
    }
}

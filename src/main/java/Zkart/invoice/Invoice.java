package Zkart.invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private long id;
    private String date;
    private long customerID;
    private List<LineItem> lineItems = new ArrayList<>();
    private double subTotal;
    private double discount;
    private double total;

    //Only id
    public Invoice(long customerID) {
        this.customerID = customerID;
        this.date = LocalDate.now().toString();

    }

    public Invoice(long id, String date, long customerID) {
        this.id = id;
        this.date = date;
        this.customerID = customerID;
    }

    //Except id
    public Invoice(long customerID, String date, List<LineItem> lineItems, double discount) {
        this.customerID = customerID;
        this.date = date;
        this.lineItems = lineItems;
        for (LineItem lineItem: lineItems) { this.subTotal += lineItem.getLineItemTotal(); }
        this.discount = discount;
        this.total = this.subTotal - discount;
    }

    // All args
    public Invoice(long id, String date, long customerID, List<LineItem> lineItems, double discount) {
        this.id = id;
        this.customerID = customerID;
        this.date = date;
        this.lineItems = lineItems;
        for (LineItem lineItem: lineItems) { this.subTotal += lineItem.getLineItemTotal(); }
        this.discount = discount;
        this.total = this.subTotal - discount;
    }




    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void addLineItem (LineItem lineItem) {
        this.lineItems.add(lineItem);
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

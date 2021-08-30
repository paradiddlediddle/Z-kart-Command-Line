package Zkart.invoice;

public class LineItem {

    private long id;
    private long invoiceID;
    private long productID;
    private int quantity;
    private double itemRate;
    private double lineItemTotal;

    //Except id and lineItemTotal
    public LineItem(long invoiceID, long productID, int quantity, double itemRate) {
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.quantity = quantity;
        this.itemRate = itemRate;
        this.lineItemTotal = itemRate * quantity;
    }

    public LineItem(long id, long invoiceID, long productID, int quantity, double itemRate) {
        this.id = id;
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.quantity = quantity;
        this.itemRate = itemRate;
        this.lineItemTotal = itemRate * quantity;
    }

    //Methods





    //Getters and Setters

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemRate() {
        return itemRate;
    }

    public void setItemRate(double itemRate) {
        this.itemRate = itemRate;
    }

    public double getLineItemTotal() {
        return lineItemTotal;
    }

    public void setLineItemTotal(double lineItemTotal) {
        this.lineItemTotal = lineItemTotal;
    }

    public long getInvoiceID() { return invoiceID; }

    public void setInvoiceID(long invoiceID) { this.invoiceID = invoiceID; }
}

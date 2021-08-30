package Zkart.cart;

public class CartItem {

    /**During checkout,
     * 1. A Blank invoice is created
     * 2. Customer cart items are converted into Invoice line items and stored in "InvoiceItems" table
     * under the blank invoice ID.
     * 3. Invoice is then loaded with the line item details from the "InvoiceItems" section, date, discount, total.
     * 4. Once the complete invoice is generated, the invoice ID is added to the customer purchase history.
     */

    private int sNo;
    private long customerID;
    private long productID;
    private int quantity;
    private double productRate;
    private double lineTotal;

    public CartItem(int sNo, long customerID, long productID, int quantity, double productRate) {
        this.sNo = sNo;
        this.customerID = customerID;
        this.productID = productID;
        this.productRate = productRate;
        this.quantity = quantity;
        this.lineTotal = productRate * quantity;
    }

//Getters and Setters


    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

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

    public double getProductRate() {
        return productRate;
    }

    public void setProductRate(double productRate) {
        this.productRate = productRate;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public int getsNo() {return sNo;}

    public void setsNo(int sNo) {this.sNo = sNo;}
}

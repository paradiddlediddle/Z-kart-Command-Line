package Zkart.user.customer;


import Zkart.cart.CartItem;
import Zkart.deals.Deal;
import Zkart.deals.DealRepository;
import Zkart.invoice.Invoice;
import Zkart.invoice.InvoiceRepository;
import Zkart.invoice.LineItem;
import Zkart.menu.MainMenu;
import Zkart.product.Product;
import Zkart.product.ProductRepository;
import Zkart.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer extends User {

    private List<Deal> deals = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();
    private List<Invoice> orderHistory = new ArrayList<>();

    //Without ID
    public Customer(String name, String email, String password, long phoneNumber) {
        super(name, email, password, phoneNumber);
    }
    public Customer(long id, String name, String email, String password, long phoneNumber, List<Deal> deals) {
        super(id, name, email, password, phoneNumber);
        this.deals = deals;
    }
    // All args
    public Customer(long id, String name, String email, String password, long phoneNumber, List<Deal> deals, List<CartItem> cartItems, List<Invoice> orderHistory) {
        super(id, name, email, password, phoneNumber);
        this.deals = deals;
        this.cartItems = cartItems;
        this.orderHistory = orderHistory;
    }
    //Just email
    public Customer(String email) {
        super(email);
    }


    //Methods
    public void welcomeScreen (Customer customer) {
        System.out.println("\n-x-x- Home Screen -x-x-");
        manageCustomerDeals(); // Alter deals before creating a shopping session
        Shopping shopping = new Shopping(customer);
        System.out.println("\n1. Shop\n2. Order History\n3. Logout");
        System.out.println("\nPlease enter your selection: ");
        Scanner welcomeScreenScan = new Scanner(System.in);
        int userSelection = welcomeScreenScan.nextInt();
        switch (userSelection) {
            case 1: shopping.shoppingScreen(); break;
            case 2: displayCustomerOrders(); welcomeScreen(customer); break;
            case 3: logout(); break;
        }
    }

    // If there are any new deals added and the customer doesn't have them, it will be added to his list
    private void manageCustomerDeals () {
        assignDealsToCustomer(); //Assigns deal and set the status as Available.
        setStatusToExpiredDeals(); //Expires the unused deals after validity expires.
    }

    private void assignDealsToCustomer () {
        DealRepository dealRepository = new DealRepository();
        List<Deal> allDeals = dealRepository.fetchAllDeals();

        for (Deal generalDeal : allDeals) {
            boolean isGeneralDealPresentInCustomer = false;

            for (Deal customerDeal : this.getDeals()){

                if (generalDeal.getDealName().equals(customerDeal.getDealName())){
                    isGeneralDealPresentInCustomer = true;
                    break;
                }
                // If the deal is not present in the customer deal, the value remains false.
            }

            //If value is false
            if (!isGeneralDealPresentInCustomer) {
                // If the customer has purchased
                int minimumOrdersRequired = 3;
                int maximumPurchaseAmount = 20_000;

                if (this.orderHistory.size() >= minimumOrdersRequired ||
                        getCustomersOverallPurchaseAmount() > maximumPurchaseAmount){

                    int lastValidInvoiceNumber = this.orderHistory.size() + generalDeal.getOrdersAfterDealExpires();
                    generalDeal.setLastValidInvoiceNumber(lastValidInvoiceNumber);
                    generalDeal.setStatus("Available");
                    this.deals.add(generalDeal);
                    dealRepository.addCustomerDealToDB(this.getId(), generalDeal);
                }
            }
        }
    }

    private void setStatusToExpiredDeals () {
        int latestInvoiceNumber  = this.orderHistory.size();

        for (Deal deal : this.deals){
            if (latestInvoiceNumber > deal.getLastValidInvoiceNumber()) {
                String status = "Expired";
                deal.setStatus(status);
                DealRepository dealRepository = new DealRepository();
                dealRepository.updateDealStatusInDB(this.getId(), deal, status);
            }
        }
    }


    private void displayCustomerOrders () {
        InvoiceRepository invoiceRepository = new InvoiceRepository();
        List<Invoice> customerOrders = invoiceRepository.fetchCustomerOrderHistory(this.getId());

        if (customerOrders.size() >  0) {

            int invoiceCount =1;
            for (Invoice invoice: customerOrders) {

                System.out.println("\n#"+invoiceCount+" Invoice ID: "+invoice.getId());
                System.out.printf("%-3s  %-12s %-18s %-10s %-8s %s\n", "S.No", "Brand", "Model", "Category", "Qty", "Amount");

                int lineItemCount = 1;
                for (LineItem lineItem: invoice.getLineItems()){

                    ProductRepository productRepository = new ProductRepository();
                    Product product = productRepository.fetchProductByID(lineItem.getProductID());

                    System.out.printf(" %-3s  %-12s %-18s %-10s %-8s %s\n", lineItemCount, product.getBrand(),
                            product.getModel(), product.getCategory(), lineItem.getQuantity(), lineItem.getLineItemTotal());

                    lineItemCount++;
                }

                System.out.printf("\n%-37s %-19s %.1f\n","","Subtotal: ",invoice.getSubTotal());
                System.out.printf("%-37s %-19s %.1f\n","","Discount: ",invoice.getDiscount());
                System.out.printf("%-37s %-19s %.1f\n","","Total: ",invoice.getTotal());

                invoiceCount++;
            }
        }
        else {
            System.out.println("No orders present, please place your first order!");
        }

    }




    public boolean isEmailPresent (String email) {
        CustomerRepository customerRepository = new CustomerRepository();
        return customerRepository.isUserEmailPresent(email);
    }


    public void addCustomerToDB (String name, String encryptedPassword, long phoneNumber) {
        CustomerRepository customerRepository = new CustomerRepository();
        customerRepository.insertCustomerIntoDB(name, this.getEmail(), encryptedPassword, phoneNumber);
    }

    private double getCustomersOverallPurchaseAmount () {
        int totalPurchaseAmount = 0;

        for (Invoice invoice: this.orderHistory){
            totalPurchaseAmount+= invoice.getTotal();
        }
        return totalPurchaseAmount;
    }

    public void logout () {
        System.out.println("Logging out now...");
        MainMenu mainMenu = new MainMenu();
        mainMenu.mainScreen();

    }


    // Getters and setters


    public List<Deal> getDeals() {
        return deals;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem (CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

    public void setDeals(Deal deal) {
        deals.add(deal);
    }

    public List<Invoice> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(Invoice invoice) {
        this.orderHistory.add(invoice);
    }
}

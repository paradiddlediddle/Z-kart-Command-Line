package Zkart.user.customer;

import Zkart.cart.CartItem;
import Zkart.cart.CartRepository;
import Zkart.deals.Deal;
import Zkart.deals.DealRepository;
import Zkart.invoice.Invoice;
import Zkart.invoice.InvoiceRepository;
import Zkart.invoice.LineItem;
import Zkart.product.Product;
import Zkart.product.ProductRepository;
import Zkart.settings.Settings;

import java.util.*;

public class Shopping {

    private final Customer customer;
    private List<Product> inventory;
    HashMap<Long, Product> inventoryByID;

    public Shopping(Customer customer) {
        this.customer = customer;
    }

    //Methods
    public void shoppingScreen () {
        System.out.println("\n\n-x-x- Shop -x-x-\n");
        refresh();
        dealOfTheMoment();
        System.out.println("\nChoose the category that you'd like to browse: ");
        System.out.println("1. Mobile Phones\n2. Laptops\n3. Tablets\n4. Home Screen");
        System.out.println("\nPlease enter your selection: ");
        Scanner shoppingScreenScan  = new Scanner(System.in);
        int userSelection = shoppingScreenScan.nextInt();
        switch (userSelection) {
            case 1: displayProductsByCategory("Phone"); break;
            case 2: displayProductsByCategory("Laptop"); break;
            case 3: displayProductsByCategory("Tablet"); break;
            case 4: customer.welcomeScreen(this.customer); break;
        }

    }

    public void displayProductsByCategory (String category) {
        HashMap<Integer, Product> productCountHashMap = new HashMap<>();

        System.out.println("\n-x-x- "+category.toUpperCase(Locale.ROOT)+"S -x-x-");
        System.out.printf("%-5s %-10s %-15s %s%n","S.No", "Brand", "Model", "Price(USD)");

        int productCount = 1;

        for (Product product : this.inventory) {


            if (product.getCategory().equals(category)) {
                productCountHashMap.put(productCount, product);

                if (product.getDiscountedPrice() > 0 ) {
                    System.out.printf(" %-3d- %-10s %-15s %.0f%n",
                            productCount, product.getBrand(), product.getModel(),
                            product.getDiscountedPrice());
                }
                else {
                    System.out.printf(" %-3d- %-10s %-15s %.0f%n",
                            productCount, product.getBrand(), product.getModel(),
                            product.getPrice());
                }
                productCount++;
            }
        }// Finishes displaying items

        Scanner cartInputScan = new Scanner(System.in);

        System.out.println("\nPlease enter you Selection:\n1. Add item to cart\n2. View cart" +
                "\n3. Checkout\n4. Back");
        System.out.println("\nPlease enter your selection: ");
        int userSelection = cartInputScan.nextInt();
        switch (userSelection) {
            case 1: getInputAndAddToCart(productCountHashMap); displayProductsByCategory(category); break;
            case 2: displayCartByCustomerID(this.customer.getId()); displayProductsByCategory(category); break;
            case 3: checkout(); customer.welcomeScreen(this.customer); break;
            case 4: shoppingScreen();
        }

    }

    private void checkout () {

        //Create blank invoice
        int invoiceSubTotal = 0;
        double discount = 0;
        Invoice invoice = new Invoice(this.customer.getId());
        InvoiceRepository invoiceRepository = new InvoiceRepository();
        Invoice newInvoice = invoiceRepository.addAndFetchInvoice(invoice);
        for (CartItem cartItem: this.customer.getCartItems()) {

            LineItem lineItem = new LineItem(newInvoice.getId(), cartItem.getProductID(),
                    cartItem.getQuantity(), cartItem.getProductRate());

            invoiceSubTotal += cartItem.getLineTotal();

            newInvoice.addLineItem(lineItem);
            invoiceRepository.addLineItemsToDB(lineItem);
        }
        // Invoice with date, id and line items and subtotal is generated
        // Just the discount and final total is yet to be generated.

        //Display invoice before discount:
        System.out.println("\n\nCart Details: ");
        System.out.printf("%-3s  %-12s %-18s %-10s %-8s %s\n", "S.No", "Brand", "Model", "Category", "Qty", "Amount");

        int lineItemCount = 1;
        for (LineItem lineItem: newInvoice.getLineItems()){

            ProductRepository productRepository = new ProductRepository();
            Product product = productRepository.fetchProductByID(lineItem.getProductID());

            System.out.printf(" %-3s  %-12s %-18s %-10s %-8s %s\n", lineItemCount, product.getBrand(),
                    product.getModel(), product.getCategory(), lineItem.getQuantity(), lineItem.getLineItemTotal());

            lineItemCount++;
        }
        System.out.println("\nSubtotal: "+invoiceSubTotal);


        if (returnCustomersAvailableDeals() > 0) {

            System.out.println("\n\nDiscounts Available: \n");

            int dealCount = 1;
            HashMap<Integer, Deal> dealMap = new HashMap<>();
            for (Deal deal: this.customer.getDeals()) {

                if (deal.getStatus().equals("Available")){

                    dealMap.put(dealCount, deal);
                    System.out.println(dealCount + ". "+deal.getDealName()+
                            " - "+(deal.getDiscountPercent() * 100)+"% discount");
                    dealCount++;
                }

            }
            System.out.println((dealMap.size() + 1)+". Skip the discount");
            System.out.println("\nEnter your selection: ");
            Scanner discountScan = new Scanner(System.in);
            int userSelection = discountScan.nextInt();
            if (userSelection <= dealMap.size()) {
                Deal selectedDeal = dealMap.get(userSelection);
                discount = invoiceSubTotal * selectedDeal.getDiscountPercent();
                //update status to used
                DealRepository dealRepository = new DealRepository();
                dealRepository.updateDealStatusInDB(customer.getId(), selectedDeal, "Used");
            }
            else { System.out.println("Discount Skipped!"); } // Can improve the way to handle invalid data
        }

        // Updating the remaining fields
        newInvoice.setDiscount(discount);
        newInvoice.setSubTotal(invoiceSubTotal);
        newInvoice.setTotal( invoiceSubTotal - discount);

        // update invoice with complete details in DB
        invoiceRepository.updateInvoiceInDBWithCompleteDetails(newInvoice);

        //Reduce the stock of items
        ProductRepository productRepository = new ProductRepository();
        for (CartItem cartItem: this.customer.getCartItems()) {

            Product currentProduct = productRepository.fetchProductByID(cartItem.getProductID());
            int quantityOrdered = cartItem.getQuantity();
            int remainingStock = currentProduct.getStock() - quantityOrdered;

            productRepository.updateStockByID(cartItem.getProductID(), remainingStock);
        }

        // Empty cart and cartItems in DB
        this.customer.getCartItems().clear();
        CartRepository cartRepository = new CartRepository();
        cartRepository.clearCartByCustomerID(newInvoice.getCustomerID());
        System.out.println("Order Placed! Your invoice ID is "+newInvoice.getId());

    }

    private int returnCustomersAvailableDeals () {

        int availableDealCount = 0;

        for (Deal deal: this.customer.getDeals()) {
            if (deal.getStatus().equals("Available")){
                availableDealCount++;
            }
        }
        return availableDealCount;
    }

    private void displayCartByCustomerID (long customerID) {
        CartRepository cartRepository = new CartRepository();
        List<CartItem> cart = cartRepository.getCartItemsByCustomerID(customerID);

        if (cart.size() > 0 ) {
            System.out.println("CART DETAILS: ");
            System.out.printf("\n%-3s %-8s %-15s %-15s %s\n", "S.No", "Brand", "Model", "Category", "Qty");

            int count = 1;
            int cartTotal = 0;
            ProductRepository productRepository = new ProductRepository();
            for (CartItem cartItem : cart) {

                Product product = productRepository.fetchProductByID(cartItem.getProductID());

                System.out.printf("%-3s %-8s %-15s %-15s %s\n", cartItem.getsNo(), product.getBrand(),
                        product.getModel(), product.getCategory(), cartItem.getQuantity());

                cartTotal += cartItem.getLineTotal();

            }
            System.out.println("Cart Total: "+ cartTotal);
        }

        else { System.out.println("Your cart is empty, you can see the items here as you add them to the cart!"); }

    }


    private void getInputAndAddToCart (HashMap<Integer, Product> productCountHashMap) {

        Scanner cartScan = new Scanner(System.in);
        // Variable declaration
        int productCount, productQuantity, cartSerialNo;
        double lineTotal, productRate;
       while (true){
           System.out.println("Enter the S.No of the item: ");
           productCount = cartScan.nextInt();
           if (productCount <= productCountHashMap.size()) {
               break;
           } else System.out.println("Enter a number within the available serial number");
       }
       // Get the selected product
       Product productSelected = productCountHashMap.get(productCount);
       // If discount is available, apply discount
       if (productSelected.getDiscountedPrice() > 0) {productRate = productSelected.getDiscountedPrice(); }
       else productRate = productSelected.getPrice();
       //Get Serial No for cart item insertion
        if (this.customer.getCartItems().size() == 0) { cartSerialNo = 1; } // if it is empty the first item will be added.
        else cartSerialNo = this.customer.getCartItems().size() + 1;

       while (true) {
           System.out.println("Enter quantity: ");
           productQuantity = cartScan.nextInt();
           if (productQuantity > productSelected.getStock() ) {
               System.out.println("You can order a maximum of "+productSelected.getStock()+" units");
           } else {
               break;
           }
       }
       lineTotal = productQuantity * productRate;

        CartItem cartItem = new CartItem(cartSerialNo, this.customer.getId(),
                productSelected.getId(), productQuantity, productRate);

        // add cart to customer list
        this.customer.addCartItem(cartItem);
       // Add to cart repo
        CartRepository cartRepository = new CartRepository();
        cartRepository.addCartItemsToDB(cartItem);
        // Get list of cartItems by customerID and add the size + 1 in cartSerialNo
    }


    public void dealOfTheMoment () {
        List<Product> dealOfTheMomentProducts = getItemsWithHighestStockWithDiscountApplied();
        System.out.println("DEAL OF THE MOMENT: ");
        int dealCount = 1;
        for (Product product: dealOfTheMomentProducts) {

            double discount = product.getPrice() * Settings.getDealOfTheMomentOfferPercentage();
            product.setDiscountedPrice(product.getPrice() - discount);

            System.out.println("Deal #"+dealCount+": "+product.getBrand()+" "+product.getModel()+" "
            +product.getCategory()+" of MRP "+product.getPrice()+" is now available at "+product.getDiscountedPrice()+"!!");
            dealCount++;
        }
    }

    private void refresh () {
        ProductRepository productRepository = new ProductRepository();
       this.inventory = productRepository.fetchActiveInventory(true);
       this.inventoryByID = convertInventoryListToHashMap(this.inventory);
    }

    private HashMap<Long, Product> convertInventoryListToHashMap (List<Product> inventory) {
        HashMap<Long, Product> inventoryByID = new HashMap<>();

        for (Product product: inventory) { inventoryByID.put(product.getId(), product); }
        return inventoryByID;
    }


    private List<Product> getItemsWithHighestStockWithDiscountApplied () {
        List<Product> productsWithHighestStock = new ArrayList<>();
        this.inventory.sort(Comparator.comparing(Product::getStock).reversed());

        int highestStock = this.inventory.get(0).getStock();

        for (Product product: this.inventory) {
            if(product.getStock() == highestStock) {
                double discount = product.getPrice() * Settings.getDealOfTheMomentOfferPercentage();
                product.setDiscountedPrice(product.getPrice() - discount);
                productsWithHighestStock.add(product);
            }
        }
        return productsWithHighestStock;
    }




}

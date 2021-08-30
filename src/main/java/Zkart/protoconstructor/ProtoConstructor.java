package Zkart.protoconstructor;

import Zkart.cart.CartItem;
import Zkart.deals.Deal;
import Zkart.invoice.Invoice;
import Zkart.invoice.LineItem;
import Zkart.product.Product;
import Zkart.user.admin.Admin;
import Zkart.user.customer.Customer;
import protobuilder.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Currently the data is fetched from the db and converted into an object
 * Then the data from the object is serialized through a protobuilder
 * Instead, we could just obtain the raw data from the DB and directly serialize it.
 *
 *
 */

public class ProtoConstructor {

    public ProtoConstructor() {
    }

    public void backupProducts (List<Product> inventory) {

        InventoryDB.Builder inventoryBackup = InventoryDB.newBuilder();

        for (Product dbProduct : inventory) {

            protobuilder.Product product = protobuilder.Product.newBuilder()
                    .setId(dbProduct.getId())
                    .setCategory(dbProduct.getCategory())
                    .setBrand(dbProduct.getBrand())
                    .setModel(dbProduct.getModel())
                    .setStock(dbProduct.getStock())
                    .setPrice(dbProduct.getPrice())
                    .build();

            inventoryBackup.addProducts(product);
        }
        InventoryDB inventoryDB = inventoryBackup.build();
        try {
            String filePath = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/products.bin";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            inventoryDB.writeTo(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) { e.printStackTrace(); }

    }


    public void backupDeals (List<Deal> dealListDB) {

        DealsDB.Builder dealsBackup = DealsDB.newBuilder();

        for (Deal dealDB: dealListDB) {

            protobuilder.Deal deal = protobuilder.Deal.newBuilder()
                    .setId(dealDB.getId())
                    .setDealName(dealDB.getDealName())
                    .setDiscountCode(dealDB.getDiscountCode())
                    .setDiscountPercent(dealDB.getDiscountPercent())
                    .setOrdersAfterDealExpires(dealDB.getOrdersAfterDealExpires())
                    .build();

            dealsBackup.addDeals(deal);
        }
        DealsDB dealsDB = dealsBackup.build();
        try {
            String filePath = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/deals.bin";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            dealsDB.writeTo(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void backupAdmin (List<Admin> adminsList) {

        AdminsDB.Builder adminBackup = AdminsDB.newBuilder();

        for (Admin adminDB: adminsList) {

            protobuilder.Admin admin = protobuilder.Admin.newBuilder()
                    .setId(adminDB.getId())
                    .setName(adminDB.getName())
                    .setEmail(adminDB.getEmail())
                    .setPassword(adminDB.getEncryptedPassword())
                    .setPhoneNumber(adminDB.getPhoneNumber())
                    .build();

            adminBackup.addAdmins(admin);
        }
        AdminsDB adminsDB = adminBackup.build();
        try {
            String filePath = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/admins.bin";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            adminsDB.writeTo(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void backupCustomers (List<Customer> customers) {

        CustomersDB.Builder customersBackup = CustomersDB.newBuilder();

        for (Customer customerDB: customers) {

            protobuilder.Customer.Builder customer = protobuilder.Customer.newBuilder();

            //fetch deals and add it to the build
            for (Deal dealDB : customerDB.getDeals()) {

                protobuilder.Deal deal =  protobuilder.Deal.newBuilder()
                        .setId(dealDB.getId())
                        .setDealName(dealDB.getDealName())
                        .setDiscountCode(dealDB.getDiscountCode())
                        .setDiscountPercent(dealDB.getDiscountPercent())
                        .setOrdersAfterDealExpires(dealDB.getOrdersAfterDealExpires())
                        .build();


                customer.addDeals(deal);

            }

            //fetch cart items and add it to the build
            for (CartItem cartItemDB: customerDB.getCartItems()) {

               protobuilder.CartItem cartItem = protobuilder.CartItem.newBuilder()
                        .setSNo(cartItemDB.getsNo())
                        .setCustomerID(cartItemDB.getCustomerID())
                        .setProductID(cartItemDB.getProductID())
                        .setQuantity(cartItemDB.getQuantity())
                        .setProductRate(cartItemDB.getProductRate())
                        .setLineTotal(cartItemDB.getLineTotal())
                        .build();

               customer.addCartItems(cartItem);

            }

            // use fetch order inside this method
            for (Invoice invoiceDB: customerDB.getOrderHistory()) {

                protobuilder.Invoice.Builder invoice = convertInvoiceToBuilder(invoiceDB);
                customer.addOrderHistory(invoice);
            }


            customer.setId(customerDB.getId())
                    .setName(customerDB.getName())
                    .setEmail(customerDB.getEmail())
                    .setPassword(customerDB.getEncryptedPassword())
                    .build();

            customersBackup.addCustomers(customer);
        }
        CustomersDB customersDB = customersBackup.build();

        try {
            String filePath = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/customers.bin";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            customersDB.writeTo(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public protobuilder.Invoice.Builder convertInvoiceToBuilder (Invoice invoiceDB) {


            protobuilder.Invoice.Builder invoice = protobuilder.Invoice.newBuilder();

            //Just fetch all line items of the invoice pojo and add it to the builder
            for (LineItem lineItemDB : invoiceDB.getLineItems()) {

                protobuilder.LineItem lineItem = protobuilder.LineItem.newBuilder()
                        .setId(lineItemDB.getId())
                        .setInvoiceID(lineItemDB.getInvoiceID())
                        .setProductID(lineItemDB.getProductID())
                        .setQuantity(lineItemDB.getQuantity())
                        .setItemRate(lineItemDB.getItemRate())
                        .setLineItemTotal(lineItemDB.getLineItemTotal())
                        .build();

                invoice.addLineItems(lineItem);
            }

            invoice.setId(invoiceDB.getId())
                    .setDate(invoiceDB.getDate())
                    .setCustomerID(invoiceDB.getCustomerID())
                    .setSubTotal(invoiceDB.getSubTotal())
                    .setDiscount(invoiceDB.getDiscount())
                    .setTotal(invoiceDB.getTotal())
                    .build();

        return invoice;
    }


}

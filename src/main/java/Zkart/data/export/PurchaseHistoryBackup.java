package Zkart.data.export;

import Zkart.invoice.Invoice;
import Zkart.protoconstructor.ProtoConstructor;
import Zkart.user.customer.Customer;
import Zkart.user.customer.CustomerRepository;
import protobuilder.Orders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PurchaseHistoryBackup implements Runnable{

    public PurchaseHistoryBackup() {
        run();
    }

    @Override
    public void run() {
        exportCustomerPurchaseHistory();
    }

    public void exportCustomerPurchaseHistory () {
        CustomerRepository customerRepository = new CustomerRepository();
        List<Customer> customers = customerRepository.fetchAllCustomers();
        ProtoConstructor protoConstructor = new ProtoConstructor();

        for (Customer customer : customers) {

            // Create a folder only if the customer has a history of orders

            if (customer.getOrderHistory().size() > 0) {
                try {
                    // Create a folder
                    // /Users/roshan-6965/Desktop/Z-kart
                    String email =  customer.getEmail();
                    File customerFolder = new File("./src/main/backups/purchase_history/"+email);
                    Boolean isFolderCreated = customerFolder.mkdirs(); // create the intermediate folder

                    //Write a file
                    FileOutputStream file = new FileOutputStream(customerFolder.getCanonicalPath()+"/orders.bin");
                    // Purchase History Builder
                    Orders.Builder purchaseHistoryBuilder = Orders.newBuilder();

                    for (Invoice invoiceDB : customer.getOrderHistory()) {

                        // The function returns a builder class, which is built and stored as an protoBuilder Invoice
                        protobuilder.Invoice invoice = protoConstructor.convertInvoiceToBuilder(invoiceDB).build();
                        // added back to the invoice
                        purchaseHistoryBuilder.addInvoices(invoice);
                    }

                    Orders purchaseHistory = purchaseHistoryBuilder.build();
                    purchaseHistory.writeTo(file);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}

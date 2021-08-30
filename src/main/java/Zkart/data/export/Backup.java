package Zkart.data.export;

import Zkart.deals.Deal;
import Zkart.deals.DealRepository;
import Zkart.product.Product;
import Zkart.product.ProductRepository;
import Zkart.protoconstructor.ProtoConstructor;
import Zkart.user.admin.Admin;
import Zkart.user.admin.AdminRepository;
import Zkart.user.customer.Customer;
import Zkart.user.customer.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Backup {

    List<Customer> customers = new ArrayList<>();
    List<Admin> admins = new ArrayList<>();
    List<Product> inventory = new ArrayList<>();
    List<Deal> deals = new ArrayList<>();

    public Backup() {

    }

    public void completeBackup () {
        System.out.println("Please wait, the back up is being initiated!");
        // Get data from the DB in the form of POJO
        ProductRepository productRepository = new ProductRepository();
        inventory =  productRepository.fetchActiveInventory(true);
        AdminRepository adminRepository = new AdminRepository();
        admins = adminRepository.fetchAllAdmins();
        DealRepository dealRepository = new DealRepository();
        deals = dealRepository.fetchAllDeals();
        CustomerRepository customerRepository = new CustomerRepository();
        customers = customerRepository.fetchAllCustomers();

        //Export .bin files
        ProtoConstructor protoConstructor = new ProtoConstructor();
        protoConstructor.backupProducts(inventory);
        protoConstructor.backupAdmin(admins);
        protoConstructor.backupDeals(deals);
        protoConstructor.backupCustomers(customers);
        System.out.println("Backup successful!");
    }


    public void purchaseHistory () {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new PurchaseHistoryBackup());
    }



}

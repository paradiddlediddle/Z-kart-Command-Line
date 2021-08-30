package Zkart.user.admin;


import Zkart.data.DataImport;
import Zkart.data.export.Backup;
import Zkart.deals.DealManager;
import Zkart.menu.MainMenu;
import Zkart.product.Product;
import Zkart.product.ProductRepository;
import Zkart.settings.Settings;
import Zkart.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Admin extends User {


    public Admin(String name, String email, String encryptedPassword, long phoneNumber) {
        super(name, email, encryptedPassword, phoneNumber);
    }
    // All args constructor
    public Admin(long id, String name, String email, String password, long phoneNumber) {
        super(id, name, email, password, phoneNumber);
    }


// Methods
    public void welcomeScreen () {
        System.out.println("\n\n-x-x- Welcome to the admin portal -x-x-");
        //Check if password change is needed
        this.checkForAdminsPasswordChange(this.getEmail());
        //Check if the items need to be reordered
        if (fetchItemsToBeReordered(Settings.getReorderLimit()).size() > 0) {
           List<Product> reorderList = fetchItemsToBeReordered(Settings.getReorderLimit());
            System.out.println("\nNumber of items which has stock lesser than "+Settings.getReorderLimit()+": "+reorderList.size());
            System.out.println("Please re-order\n");
            reorder("lowStock");
        }
        //Options for Admin
        System.out.println("\n1. Create Deals\n2. Re-order\n3. Import Customers" +
                "\n4. Import Products\n5. Complete Back-up\n6. Exit to Main Menu");
        System.out.println("\nEnter your selection: ");
        Scanner adminScreenScan = new Scanner(System.in);
        int adminSelection = adminScreenScan.nextInt();
        MainMenu mainMenu = new MainMenu();
        switch (adminSelection){
            case 1: DealManager.createDeals(); welcomeScreen(); return;
            case 2: reorderScreen(); welcomeScreen(); return;
            case 3: importCustomers(); welcomeScreen(); return;
            case 4: importProducts(); welcomeScreen(); return;
            case 5: initiateCompleteBackup(); welcomeScreen(); return;
            case 6: mainMenu.mainScreen(); break;
        }

    }

    public void importProducts () {

        Scanner importScan = new Scanner(System.in);
        System.out.println("Please make sure that your customers file is uploaded in the location: ");
        System.out.println("Folder Location: /Users/roshan-6965/Desktop/Z-kart/src/main/java/Zkart/data/import/products");
        while (true) {
            System.out.println("Please type 'Confirm' to continue: ");
            String userEntry = importScan.nextLine();
            if (userEntry.toLowerCase(Locale.ROOT).equals("confirm")) { break; }
        }
        DataImport dataImport = new DataImport();
        dataImport.importProducts();

    }

    public void importCustomers () {

        Scanner importScan = new Scanner(System.in);
        System.out.println("Please make sure that your customers file is uploaded in the location: ");
        System.out.println("Folder Location: /Users/roshan-6965/Desktop/Z-kart/src/main/java/Zkart/data/import/customers");
        while (true) {
            System.out.println("Please type 'Confirm' to continue: ");
            String userEntry = importScan.nextLine();
            if (userEntry.toLowerCase(Locale.ROOT).equals("confirm")) { break; }
        }
        DataImport dataImport = new DataImport();
        dataImport.importCustomers();

    }


    private void createAnotherAdmin () {
        Scanner adminScan = new Scanner(System.in);
        System.out.println("\n\n-x-x- Setup another Admin -x-x-");
        System.out.println("Please enter the admin's email: ");
        String email = adminScan.nextLine();
        System.out.println("Please set the admin's password: ");
        String password = adminScan.nextLine();
        String encryptedPassword = encrypt(password);
        AdminRepository adminRepository = new AdminRepository();
        adminRepository.addAdminToDB(email, encryptedPassword);
    }


    public boolean checkPassword (String password) {
        String encryptedPassword = encrypt(password);
        AdminRepository adminRepository = new AdminRepository();
       return adminRepository.isUserPasswordCorrect(encryptedPassword);
    }

    public void updatePassword (String email, String password) {
        String encryptedPassword = encrypt(password);
        AdminRepository adminRepository = new AdminRepository();
        adminRepository.updatePassword(email, encryptedPassword);
    }

    private void checkForAdminsPasswordChange (String email) {
        AdminRepository adminRepository = new AdminRepository();
        String password = adminRepository.checkForAdminsPasswordChange(email);
        String adminOTP = "xyzzy";
        boolean passwordChangeRequired = adminOTP.equals(this.decrypt(password));

        if (passwordChangeRequired) {
            Scanner forcePasswordScan = new Scanner(System.in);
            System.out.println("\nAttention: Please change your password! ");
            while (true) {
                System.out.println("Enter new Password: ");
               String newPassword = forcePasswordScan.nextLine();
                if (this.verifyPassword(newPassword)){
                    this.updatePassword(email, newPassword);
                    return;
                }
                else if (newPassword.equals("exit")) { return; }
                else {
                    System.out.println("Please enter a valid password");
                    System.out.println("Password needs to have at least 2 uppercase, 2 lowercase and 2 number characters in it.\n");
                }
            }
        }

    }

    // REORDER FOR NORMAL ITEMS

    // Reorder
    private void reorderScreen () {
        Scanner reorderScan = new Scanner(System.in);
        System.out.println("-x-x- Reorder Screen -x-x-");
        System.out.println("\n1. Change reorder point");
        System.out.println("2. Reorder items");
        System.out.println("\nEnter your selection: ");
        int userSelection = reorderScan.nextInt();
        switch (userSelection) {
            case 1: changeReorderPoint(); break;
            case 2: reorder("all"); break; // need to show inventory instead
        }

    }


    private void reorder (String itemsToBeDisplayed) {

        ProductRepository productRepository = new ProductRepository();
        List<Product> listToBeDisplayed = new ArrayList<>();

        if (itemsToBeDisplayed.equals("all")) {
            listToBeDisplayed = productRepository.fetchActiveInventory(true);
        }
        else if (itemsToBeDisplayed.equals("lowStock")) {
            int reorderPoint = Settings.getReorderLimit();
            listToBeDisplayed = productRepository.fetchLowStockItems(reorderPoint);
            displayItemsToBeReordered(listToBeDisplayed);
        }

        placeReorder(listToBeDisplayed);
    }

    private void changeReorderPoint () {
        Scanner reorderScan = new Scanner(System.in);
        System.out.println("Enter new reorder limit: ");
        int userSelection = reorderScan.nextInt();
        Settings.setReorderLimit(userSelection);
        System.out.println("New reorder point set to "+userSelection);
        welcomeScreen();
    }

    private void displayItemsToBeReordered (List<Product> listToBeDisplayed ) {

       if (listToBeDisplayed.size() > 0) {
           System.out.println("ITEMS TO BE REORDERED: \n");
           System.out.printf(" %-3s %-9s %-10s %-15s %-8s %s%n", "Id", "Category", "Brand", "Model", "Stock", "Price(USD)");

           for (Product product : listToBeDisplayed) {

               long id = product.getId();
               String category = product.getCategory();
               String brand = product.getBrand();
               String model = product.getModel();
               int stock = product.getStock();
               double price = product.getPrice();

               System.out.printf("#%-3d %-9s %-10s %-15s %-8d %.0f%n", id, category, brand, model, stock, price);
           }
       }
    }

    private List<Product> fetchItemsToBeReordered (int reorderPoint) {

        ProductRepository productRepository = new ProductRepository();
        List<Product> lowStockList = productRepository.fetchLowStockItems(reorderPoint);

        if (lowStockList.size() > 0) {

            for (Product product : lowStockList) {

                long id = product.getId();
                String category = product.getCategory();
                String brand = product.getBrand();
                String model = product.getModel();
                int stock = product.getStock();
                double price = product.getPrice();

            }
            return lowStockList;
        }
        else {
            return lowStockList;
        }

    }

    private void placeReorder (List<Product> listToBeReordered) {

        if (listToBeReordered != null) {

            Scanner reorderScan = new Scanner(System.in);
            System.out.println("-x-x- Reorder Stock -x-x-\n\n");
            ProductRepository productRepository = new ProductRepository();

            for (Product product: listToBeReordered) {
                long id = product.getId();
                String brand = product.getBrand();
                String model = product.getModel();
                int currentStock = product.getStock();

                System.out.printf(" %-3s %-10s %-15s %-8s %n", "Id", "Brand", "Model", "Current Stock");
                System.out.printf("#%-3d %-10s %-15s %-8d %n", id, brand, model, currentStock);

                System.out.println("\nPlease enter the quantity to be ordered for "+model+": ");
                int quantityToOrder = reorderScan.nextInt();
                int newStock = quantityToOrder + currentStock;
                productRepository.updateStockByID(id, newStock);
            }
        }

        }

        public void initiateCompleteBackup () {
            Backup backup = new Backup();
            backup.completeBackup();
        }


}

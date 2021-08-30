package Zkart.menu;

import Zkart.data.export.Backup;
import Zkart.user.admin.Admin;
import Zkart.user.admin.AdminRepository;
import Zkart.user.customer.Customer;
import Zkart.user.customer.CustomerRepository;

import java.util.Scanner;

public class MainMenu {

    public void mainScreen () {

        Scanner loginScan  = new Scanner(System.in);

       while (true) {
           initialActivities();
           System.out.println("\n\n-x-x- Z-kart -x-x-");
           System.out.println("\n1. Sign-in");
           System.out.println("2. Sign-up");
           System.out.println("3. Administrator Login");
           System.out.println("4. Exit");
           System.out.println("\nPlease enter your selection: ");
           int userEntry = loginScan.nextInt();
           switch (userEntry){
               case 1: verifyLogin("customer"); return;
               case 2: createNewAccount(); return;
               case 3: verifyLogin("admin"); return;
               case 4: System.out.println("\n\nThank you for using Z-kart!"); System.exit(userEntry);
           }

       }

    }

    //From main screen
    private void createNewAccount () {
       Scanner newAccountScan = new Scanner(System.in);
        System.out.println("\n\n-x-x- Create a new account -x-x-");
        System.out.println("\nPlease enter your name: ");
        String name = newAccountScan.nextLine();
        if(name.equals("exit")) { mainScreen(); return; }
            System.out.println("Please enter your email: ");
            String email = newAccountScan.nextLine();
            Customer customer = new Customer(email);

            if (email.length() > 0 && customer.isEmailPresent(email)) {
                System.out.println("Email already exists! Please sign-in.");
                return;
            }

        System.out.println("Please enter you Phone Number: ");
        long phoneNumber = newAccountScan.nextLong();
        String password;
        while (true) {
            Scanner passwordScan = new Scanner(System.in);
            System.out.println("Enter password: ");
            password = passwordScan.nextLine();
            if(password.equals("exit")) { mainScreen(); return; }
            boolean passwordMeetsRequirement = customer.verifyPassword(password);
            if (passwordMeetsRequirement) {
                System.out.println("Confirm password: ");
                String reEnteredPassword = passwordScan.nextLine();
                if (password.equals(reEnteredPassword)) {
                    customer.addCustomerToDB(name, customer.encrypt(reEnteredPassword), phoneNumber);
                    break;
                }
                else if (reEnteredPassword.equals("exit")) { mainScreen(); return; }
                else { System.out.println("Password doesn't match, please try again!"); }
            }
            else {
                System.out.println("Please enter a valid password");
                System.out.println("Password needs to have at least 2 uppercase, 2 lowercase and 2 number characters in it.\n");
            }
        }

        customer.welcomeScreen(customer); mainScreen();
    }

    //Admin Login



    private void verifyLogin (String user) {
        Scanner verifyLoginScan = new Scanner(System.in);
        Customer customer = null;
        Admin admin = null;

        System.out.println("\n\n-x-x- Log into your account -x-x-");

        while (true) {
            System.out.println("\nPlease enter your email: ");
            String userEmail = verifyLoginScan.nextLine();
            userEmail = userEmail.toLowerCase().trim();
            switch (user) {
                case "customer": {
                    CustomerRepository customerRepository = new CustomerRepository();
                    if (customerRepository.isUserEmailPresent(userEmail)) {
                        customer = customerRepository.getCustomerByEmail(userEmail);
                    } else {
                        System.out.println("\nOops! the email entered doesn't have an account.");
                        System.out.println("Do you want to re-enter the email?");
                        System.out.println("\n1. Yes, let me re-enter.");
                        System.out.println("2. No, I want create a new account.");
                        System.out.println("3. Main screen");
                        System.out.println("\nPlease enter your selection: ");
                        int userSelection = verifyLoginScan.nextInt();
                        switch (userSelection) {
                            case 1: verifyLogin("customer"); return;
                            case 2: createNewAccount(); return;
                            case 3: mainScreen(); return;
                        }
                    } // email verification ends
                    while (true) {
                    System.out.println("Please enter your password: ");
                    String enteredPassword = verifyLoginScan.nextLine();
                    enteredPassword = customer.encrypt(enteredPassword);
                        if (enteredPassword.length() > 0 && customer.getEncryptedPassword().equals(enteredPassword)) {
                            customer.welcomeScreen(customer); mainScreen();
                            return;
                        }
                        else if (enteredPassword.equals("exit")) {
                            // try to show main menu once again
                           return;
                        }
                        else {
                            System.out.println("Password Incorrect!");
                        }
                    }
                }
                case "admin": {
                    AdminRepository adminRepository = new AdminRepository();
                    if (adminRepository.isUserEmailPresent(userEmail) ||
                            userEmail.equals("admin@zoho.com")) {
                        admin = adminRepository.getAdminByAdminEmailID(userEmail);
                    } else {
                        System.out.println("Admin email doesn't match");
                        System.out.println("Do you want to re-enter the email?");
                        System.out.println("\n1. Yes, let me re-enter.");
                        System.out.println("2. No, take me to Main screen");
                        System.out.println("\nPlease enter your selection: ");
                        int userSelection = verifyLoginScan.nextInt();
                        switch (userSelection) {
                            case 1: verifyLogin("admin"); break;
                            case 2: break;
                        }
                    } // email verification ends
                    while (true) {
                        System.out.println("Please enter your password: ");
                        String enteredPassword = verifyLoginScan.nextLine();
                        enteredPassword = admin.encrypt(enteredPassword);
                        if (enteredPassword.length() > 0 && admin.getEncryptedPassword().equals(enteredPassword)) {
                            admin.welcomeScreen(); mainScreen();
                           return;
                        }
                        else if (enteredPassword.equals("exit")) {
                            return;
                        }
                        else {
                            System.out.println("Password Incorrect");
                        }
                    }
                }
            }
        } // while loop ends
    }

    public void initialActivities () {
        Backup backup = new Backup();
        backup.purchaseHistory();
    }


}

package Zkart.data;

import Zkart.persistence.PersistenceLayer;
import protobuilder.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public class DataImport extends PersistenceLayer {

    public void importCustomers () {

        String filePath = "/Users/roshan-6965/Desktop/Z-kart/src/main/java/Zkart/data/import/customers/customers.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            Connection connection = connect();
            reader.readLine(); // Skips header by reading the first line before entering the while loop.

            String query = "insert into Customers (name, email, password, phoneNumber) values (?,?,?,?)";
            int rowsUpdated = 0;


            while ((line = reader.readLine()) != null) {

                String[] columns = line.split(",");
                String name = columns[0];
                String email = columns[1];
                long phone = Long.parseLong(columns[2]);
                String password = columns[3];


                assert connection != null;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setLong(4, phone);

                rowsUpdated += preparedStatement.executeUpdate();
                preparedStatement.close();
            }

            assert connection != null;
            connection.close();
            if(rowsUpdated > 0) System.out.println("Customers successfully imported!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("\n"+e.getMessage());
        }
        catch (IOException | SQLException e) { e.printStackTrace(); }
    }

    public void importProducts () {

        String filePath = "/Users/roshan-6965/Desktop/Z-kart/src/main/java/Zkart/data/import/products/products.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            reader.readLine(); // Skips header

                Connection connection = connect();

                String query = "insert into Products (category, brand, model, stock, price) values (?,?,?,?,?)";
                int rowsUpdated = 0;


                while ((line = reader.readLine()) != null) {

                    String[] columns = line.split(",");
                    String productCategory = columns[0];
                    String brand = columns[1];
                    String model = columns[2];
                    double price = Double.parseDouble(columns[3]) ;
                    long stock = Long.parseLong(columns[4]);

                    assert connection != null;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, productCategory);
                    preparedStatement.setString(2, brand);
                    preparedStatement.setString(3, model);
                    preparedStatement.setLong(4, stock);
                    preparedStatement.setDouble(5, price);

                    rowsUpdated += preparedStatement.executeUpdate();
                    preparedStatement.close();
                }

            assert connection != null;
            connection.close();
            if(rowsUpdated > 0) System.out.println("Products successfully imported!");


        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("\n"+e.getMessage());
        }

        catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    public InventoryDB convertBinaryToProducts () {

        String fileName = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/products.bin";
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return InventoryDB.parseFrom(fileInputStream);
        } catch (IOException e) { e.printStackTrace(); return null; }

    }
    public AdminsDB convertBinaryToAdmins () {
        String fileName = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/admins.bin";
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return AdminsDB.parseFrom(fileInputStream);
        } catch (IOException e) { e.printStackTrace(); return null; }

    }
    public DealsDB convertBinaryToDeals () {
        String fileName = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/deals.bin";
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return DealsDB.parseFrom(fileInputStream);
        } catch (IOException e) { e.printStackTrace(); return null; }

    }
    public CustomersDB convertBinaryToCustomers () {
        String fileName = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/complete_backup/customers.bin";
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return CustomersDB.parseFrom(fileInputStream);
        } catch (IOException e) { e.printStackTrace(); return null; }

    }
    public Orders convertBinaryToOrders () {

        String fileName = "/Users/roshan-6965/Desktop/Z-kart/src/main/backups/purchase_history/rosh@gmail.com/orders.bin";
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return Orders.parseFrom(fileInputStream);
        } catch (IOException e) { e.printStackTrace(); return null; }

    }

}

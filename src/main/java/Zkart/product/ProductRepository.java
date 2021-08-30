package Zkart.product;

import Zkart.persistence.PersistenceLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends PersistenceLayer {


    public List<Product> fetchActiveInventory (Boolean fetchActiveStock ) {
        String query;
        if (fetchActiveStock) { query = "select * from Products";  }
        else { query = "select * from products where stock > 1"; }

        List<Product> inventory = new ArrayList<>();
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id =  resultSet.getLong("id");
                String category = resultSet.getString("category");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int stock = resultSet.getInt("stock");
                double price = resultSet.getDouble("price");

                Product product = new Product(id, category, brand, model, stock, price);
                inventory.add(product);
            }

            preparedStatement.close();
            connection.close();

            return inventory;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

    }


    public List<Product> fetchLowStockItems (int reorderPoint ) {

        List<Product> lowStock = new ArrayList<>();
        String query = "select * from Products where stock <'"+reorderPoint+"'";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
              long id =  resultSet.getLong("id");
              String category = resultSet.getString("category");
              String brand = resultSet.getString("brand");
              String model = resultSet.getString("model");
              int stock = resultSet.getInt("stock");
              double price = resultSet.getDouble("price");

              Product product = new Product(id, category, brand, model, stock, price);
              lowStock.add(product);
            }

            preparedStatement.close();
            connection.close();

            return lowStock;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

    }

    public Product fetchProductByID (long productID ) {

        Product product = null;
        String query = "select * from Products where id="+productID+"";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long id =  resultSet.getLong("id");
                String category = resultSet.getString("category");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int stock = resultSet.getInt("stock");
                double price = resultSet.getDouble("price");

                product = new Product(id, category, brand, model, stock, price);
            }

            preparedStatement.close();
            connection.close();

            return product;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

    }

    public void updateStockByID (Long id, int newQuantity) {

        String query = "UPDATE Products SET `stock` = '"+newQuantity+"' WHERE (`id` = '"+id+"')";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println(rowsUpdated+" row(s) updated");

            preparedStatement.close();
            connection.close();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }


    }


}

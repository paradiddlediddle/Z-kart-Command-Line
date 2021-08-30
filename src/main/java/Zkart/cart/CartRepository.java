package Zkart.cart;

import Zkart.persistence.PersistenceLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRepository extends PersistenceLayer {


    public void addCartItemsToDB (CartItem cartItem) {

        String query = "insert into CartItems (sNo, customer_id, product_id, quantity, productRate, lineTotal) values (?,?,?,?,?,?)";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            preparedStatement.setInt(1, cartItem.getsNo());
            preparedStatement.setLong(2,cartItem.getCustomerID());
            preparedStatement.setLong(3,cartItem.getProductID());
            preparedStatement.setInt(4,cartItem.getQuantity());
            preparedStatement.setDouble(5, cartItem.getProductRate());
            preparedStatement.setDouble(6, cartItem.getLineTotal());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }


    }
    public List<CartItem> getCartItemsByCustomerID ( long customerID ) {

        List<CartItem> cartList = new ArrayList<>();
        String query = "select * from CartItems where customer_id=" + customerID;

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // id is not needed
                long id = resultSet.getLong("id");
                int sNo = resultSet.getInt("sNo");
                long product_id = resultSet.getLong("product_id");
                int quantity = resultSet.getInt("quantity");
                double productRate = resultSet.getDouble("productRate");
                double lineTotal = resultSet.getDouble("lineTotal");

                CartItem cartItem = new CartItem(sNo, customerID, product_id, quantity, productRate);
                cartList.add(cartItem);

            }

            preparedStatement.close();
            connection.close();

            return cartList;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

    }

    public void clearCartByCustomerID ( long customerID ) {

        List<CartItem> cartList = new ArrayList<>();

        String query = "delete from CartItems where customer_id=" + customerID;

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();


        }
        catch (SQLException exception) {
            exception.printStackTrace();

        }
    }


}

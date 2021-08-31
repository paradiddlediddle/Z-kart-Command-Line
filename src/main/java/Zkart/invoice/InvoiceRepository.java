package Zkart.invoice;

import Zkart.persistence.PersistenceLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository extends PersistenceLayer {

    public List<Invoice> fetchCustomerOrderHistory ( long customerID ) {

        List<Invoice> orderHistory = new ArrayList<>();

        String query = "select * from Invoices where customer_id="+customerID;
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long invoice_id =  resultSet.getLong("id");
                String date = resultSet.getString("date");
                double subtotal = resultSet.getDouble("subtotal");
                double discount = resultSet.getDouble("discount");
                double total = resultSet.getDouble("total");
                List<LineItem> lineItems = fetchLineItemsByInvoiceID(invoice_id);

                Invoice invoice = new Invoice(invoice_id, date, customerID, lineItems, discount);
                orderHistory.add(invoice);
            }

            preparedStatement.close();
            connection.close();

            return orderHistory;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

    }

    public List<LineItem> fetchLineItemsByInvoiceID ( long invoiceID ) {
        List<LineItem> lineItemsList = new ArrayList<>();
        String query = "select * from LineItems where invoice_id="+invoiceID;

        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long invoice_id = resultSet.getLong("invoice_id");
                long product_id = resultSet.getLong("product_id");
                int quantity = resultSet.getInt("quantity");
                double productRate = resultSet.getDouble("productRate");

                LineItem lineItem = new LineItem(id, invoice_id, product_id, quantity, productRate);
                lineItemsList.add(lineItem);
            }

            preparedStatement.close();
            connection.close();

            return lineItemsList;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void addLineItemsToDB (LineItem lineItem) {

        String query = "insert into LineItems (invoice_id, product_id, quantity, productRate, lineTotal) values (?,?,?,?,?)";

        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            preparedStatement.setLong(1,lineItem.getInvoiceID());
            preparedStatement.setLong(2,lineItem.getProductID());
            preparedStatement.setInt(3,lineItem.getQuantity());
            preparedStatement.setDouble(4, lineItem.getItemRate());
            preparedStatement.setDouble(5, lineItem.getLineItemTotal());

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println(rowsUpdated+" item added to cart");
            preparedStatement.close();
            connection.close();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void updateInvoiceInDBWithCompleteDetails (Invoice invoice) {
        Connection connection = null;
        try {
            String query = "update Invoices set date=?, customer_id=?, " +
                    "subTotal=?, discount=?, total=? where id = ?";

            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            preparedStatement.setString(1, invoice.getDate());
            preparedStatement.setLong(2, invoice.getCustomerID());
            preparedStatement.setDouble(3, invoice.getSubTotal());
            preparedStatement.setDouble(4, invoice.getDiscount());
            preparedStatement.setDouble(5, invoice.getTotal());
            preparedStatement.setLong(6, invoice.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Invoice addAndFetchInvoice (Invoice invoiceWithoutID) {

        Invoice invoiceWithID = null;
        Connection connection = null;
        try {
            String query = "insert into Invoices (date, customer_id) values (?,?)";

            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            preparedStatement.setString(1, invoiceWithoutID.getDate());
            preparedStatement.setLong(2, 0);

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();


        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            String query = "select * from Invoices where customer_id="+0;

            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String date = resultSet.getString("date");

                invoiceWithID = new Invoice(id, date, invoiceWithoutID.getCustomerID());

            }

            preparedStatement.close();
            connection.close();


        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {

            String query = "update Invoices set customer_id=?, date=? where id = ?";

            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            preparedStatement.setLong(1, invoiceWithoutID.getCustomerID());
            preparedStatement.setString(2, invoiceWithoutID.getDate());
            preparedStatement.setLong(3, invoiceWithID.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();


        }

        catch (SQLException exception) {
            exception.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return invoiceWithID;
    }



}

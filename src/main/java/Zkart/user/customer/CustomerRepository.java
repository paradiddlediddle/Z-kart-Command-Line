package Zkart.user.customer;

import Zkart.cart.CartItem;
import Zkart.cart.CartRepository;
import Zkart.deals.Deal;
import Zkart.deals.DealRepository;
import Zkart.invoice.Invoice;
import Zkart.invoice.InvoiceRepository;
import Zkart.persistence.PersistenceLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository extends PersistenceLayer {

    public Boolean isUserEmailPresent (String email)  {

        String valueFromDB = null;
        String query = "select * from Customers where email='"+email+"'";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
                valueFromDB = resultSet.getString("email");
            }
            preparedStatement.close();
            connection.close();

            return email.equals(valueFromDB);

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Customer> fetchAllCustomers ()  {
        List<Customer> customers = new ArrayList<>();
        Customer customer;
        //Get Customer basic details
        String query = "select * from Customers";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while  (resultSet.next()) {
                long customer_id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String encryptedPassword = resultSet.getString("password");
                long phoneNumber = resultSet.getLong("phoneNumber");

                DealRepository dealRepository = new DealRepository();
                CartRepository cartRepository = new CartRepository();
                InvoiceRepository invoiceRepository = new InvoiceRepository();

                List<Deal> customerDeals = dealRepository.getCustomerDealByCustomerID(customer_id);
                List<CartItem> cartItemList = cartRepository.getCartItemsByCustomerID(customer_id);
                List<Invoice> orderHistory = invoiceRepository.fetchCustomerOrderHistory(customer_id);

                customer = new Customer(customer_id, name, email, encryptedPassword,phoneNumber,
                        customerDeals, cartItemList, orderHistory);
                customers.add(customer);
            }
            preparedStatement.close();
            connection.close();
        }

        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return customers;
    }


    public Customer getCustomerByEmail (String email)  {
        Customer customer = null;
        //Get Customer basic details
        String query = "select * from Customers where email='"+email+"'";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
                Long customer_id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String encryptedPassword = resultSet.getString("password");
                Long phoneNumber = resultSet.getLong("phoneNumber");

                DealRepository dealRepository = new DealRepository();
                CartRepository cartRepository = new CartRepository();
                InvoiceRepository invoiceRepository = new InvoiceRepository();

                List<Deal> customerDeals = dealRepository.getCustomerDealByCustomerID(customer_id);
                List<CartItem> cartItemList = cartRepository.getCartItemsByCustomerID(customer_id);
                List<Invoice> orderHistory = invoiceRepository.fetchCustomerOrderHistory(customer_id);

                customer = new Customer(customer_id, name, email, encryptedPassword,phoneNumber,
                        customerDeals, cartItemList, orderHistory);
            }
            preparedStatement.close();
            connection.close();
        }

        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return customer;
    }

    public Customer getCustomerByID (long id)  {
        Customer customer = null;
        //Get Customer basic details
        String query = "select * from Customers where id='"+id+"'";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
                Long customer_id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String encryptedPassword = resultSet.getString("password");
                Long phoneNumber = resultSet.getLong("phoneNumber");

                DealRepository dealRepository = new DealRepository();
                CartRepository cartRepository = new CartRepository();
                InvoiceRepository invoiceRepository = new InvoiceRepository();

                List<Deal> customerDeals = dealRepository.getCustomerDealByCustomerID(customer_id);
                List<CartItem> cartItemList = cartRepository.getCartItemsByCustomerID(customer_id);
                List<Invoice> orderHistory = invoiceRepository.fetchCustomerOrderHistory(customer_id);

                customer = new Customer(customer_id, name, email, encryptedPassword,phoneNumber,
                        customerDeals, cartItemList, orderHistory);

            }
            preparedStatement.close();
            connection.close();
        }

        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return customer;
    }

    public Boolean isUserPasswordCorrect (String encryptedPassword ) {

        String valueFromDB = null;
        String query = "select * from Customers where password='"+encryptedPassword+"'";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
                valueFromDB = resultSet.getString("password");
            }
            preparedStatement.close();
            connection.close();

            return encryptedPassword.equals(valueFromDB);

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void insertCustomerIntoDB (String name, String email, String encryptedPassword, long phoneNumber)  {

        String query = "insert into Customers (name, email, password, phoneNumber) values (?,?,?,?)";
        int rowsUpdated = 0;
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, encryptedPassword);
            preparedStatement.setLong(4, phoneNumber);

            rowsUpdated += preparedStatement.executeUpdate();
            System.out.println(rowsUpdated);

            preparedStatement.close();
            connection.close();



        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}

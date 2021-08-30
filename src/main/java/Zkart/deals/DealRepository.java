package Zkart.deals;

import Zkart.persistence.PersistenceLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DealRepository extends PersistenceLayer {


    public void addDealToDB (Deal deal) {

        String query = "insert into Deals (name, discountCode, discountPercentage, ordersAfterDealExpires) values (?,?,?,?)";
        int rowsUpdated = 0;
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);
            preparedStatement.setString(1, deal.getDealName());
            preparedStatement.setString(2, deal.getDiscountCode());
            preparedStatement.setDouble(3, deal.getDiscountPercent());
            preparedStatement.setInt(4, deal.getOrdersAfterDealExpires());

            rowsUpdated += preparedStatement.executeUpdate();
            System.out.println(rowsUpdated);

            preparedStatement.close();
            connection.close();

        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public void addCustomerDealToDB (long customerID, Deal deal) {

        String query = "insert into CustomerDeals (customer_id, deal_id, lastValidInvoiceNumber, status) values (?,?,?,?)";
        int rowsUpdated = 0;
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);
            preparedStatement.setLong(1, customerID);
            preparedStatement.setLong(2, deal.getId());
            preparedStatement.setInt(3, deal.getLastValidInvoiceNumber());
            preparedStatement.setString(4, deal.getStatus());
            rowsUpdated += preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public List<Deal> getCustomerDealByCustomerID (long customerID)  {
        List<Deal> customerDeals = new ArrayList<>();
        //Get Customer basic details
        String query = "select * from CustomerDeals where customer_id='"+customerID+"'";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while  (resultSet.next()) {
                long deal_id = resultSet.getLong("deal_id");
                int lastValidInvoiceNumber = resultSet.getInt("lastValidInvoiceNumber");
                String status = resultSet.getString("status");
                Deal deal = getDealByDealID(deal_id);
                deal.setLastValidInvoiceNumber(lastValidInvoiceNumber);
                deal.setStatus(status);
                customerDeals.add(deal);
            }
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return customerDeals;
    }


    public Deal getDealByDealID (long id) {
        Deal deal = null;
        String query = "select * from Deals where id='"+id+"'";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
               String dealName = resultSet.getString("name");
               String discountCode = resultSet.getString("discountCode");
               double discountPercentage = resultSet.getDouble("discountPercentage");
               int ordersAfterDealExpires = resultSet.getInt("ordersAfterDealExpires");
               deal = new Deal(id, dealName, discountCode, discountPercentage, ordersAfterDealExpires);
            }
            preparedStatement.close();
            connection.close();
        }

        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return deal;
    }


    public List<Deal> fetchAllDeals () {
        List<Deal> allDeals = new ArrayList<>();

        String query = "select * from Deals";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while  (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String discountCode = resultSet.getString("discountCode");
                double discountPercentage = resultSet.getDouble("discountPercentage");
                int ordersAfterDealExpires = resultSet.getInt("ordersAfterDealExpires");

                Deal deal = new Deal(id, name, discountCode, discountPercentage, ordersAfterDealExpires);
                allDeals.add(deal);
            }
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

        return allDeals;
    }


    public void updateDealStatusInDB (long customerID, Deal deal, String status) {
        String query = "UPDATE CustomerDeals SET status= '"+status+"' " +
                "WHERE (deal_id = '"+deal.getId()+"' and customer_id = '"+customerID+"')";
        int rowsUpdated = 0;
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

    public Deal deleteDealByID (long id) {
        Deal deal = null;
        int deletedRows = 0;
        String query = "delete from Deals where id='"+id+"'";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            deletedRows += preparedStatement.executeUpdate();
            System.out.println("Number of rows deleted: "  + deletedRows);
            preparedStatement.close();
            connection.close();
            preparedStatement.close();
            connection.close();
        }

        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return deal;
    }








}

package Zkart.user.admin;

import Zkart.persistence.PersistenceLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository extends PersistenceLayer {

    public void addAdminToDB (String email, String encryptedPassword)  {

        String query = "insert into Admin (email, password) values (?,?)";
        int rowsUpdated = 0;
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, encryptedPassword);

            rowsUpdated += preparedStatement.executeUpdate();
            System.out.println(rowsUpdated);

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

    public void updatePassword (String email, String encryptedPassword) {

        String query = "UPDATE `Admin` SET `password` = '"+encryptedPassword+"' WHERE (`email` = '"+email+"')";
        int rowsUpdated = 0;
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            rowsUpdated += preparedStatement.executeUpdate();
            System.out.println(rowsUpdated);

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

    public Admin getAdminByAdminEmailID (String adminEmail)  {
        Admin admin = null;
        //Get Customer basic details
        String query = "select * from Admin where email='"+adminEmail+"'";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
                Long admin_id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String encryptedPassword = resultSet.getString("password");
                Long phoneNumber = resultSet.getLong("phoneNumber");

                admin = new Admin(admin_id, name, adminEmail, encryptedPassword, phoneNumber);
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
        return admin;
    }

    public Boolean isUserEmailPresent (String email)  {

        String valueFromDB = null;
        String query = "select * from Admin where email='"+email+"'";
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

    public List<Admin> fetchAllAdmins ()  {

        List<Admin> adminsList = new ArrayList<>();
        String query = "select * from Admin";
        Connection connection = null;
        try {
            connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String encryptedPassword = resultSet.getString("password");
                long phoneNumber = resultSet.getLong("phoneNumber");


                Admin admin = new Admin(id, name, email, encryptedPassword, phoneNumber);
                adminsList.add(admin);
            }

            preparedStatement.close();
            connection.close();

            return adminsList;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
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


    public Boolean isUserPasswordCorrect (String encryptedPassword ) {

        String valueFromDB = null;
        String query = "select * from Admin where password='"+encryptedPassword+"'";
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

    public String checkForAdminsPasswordChange (String email) {

        String valueFromDB = null;
        String query = "select * from Admin where email='"+email+"'";
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

            return valueFromDB;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
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

}

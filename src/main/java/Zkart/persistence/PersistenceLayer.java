package Zkart.persistence;

import java.sql.*;

public class PersistenceLayer {

    // Connection
    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/Zkart", "root", "Jan@5696");
        } catch (SQLException | ClassNotFoundException exception) {
            System.out.println(exception);
            return null;
        }

    }


    //READ
    public String specificStringFetch (String tableName, String columnName, String value)  {

        String valueFromDB = null;
        String query = "select * from"+tableName+"where "+columnName+"='"+value+"'";
        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connect().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if  (resultSet.next()) {
                valueFromDB = resultSet.getString(columnName);
            }
            preparedStatement.close();
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return valueFromDB;
    }


}
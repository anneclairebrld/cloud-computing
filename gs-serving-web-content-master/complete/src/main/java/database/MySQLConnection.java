package database;

import java.sql.*;
import java.util.List;

public class MySQLConnection{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

   //constructor: generates the connection and saves it
    public MySQLConnection(){
        try{

            System.out.println("in mysql connection");
            connection = DriverManager.getConnection("jdbc:mysql://35.195.54.162:3306/names","root","finalyear4us3");

            //statements allow to issue SQL queries to the database & results gets them back
            statement = connection.createStatement();

            System.out.println("i have created a connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void get_tables(){
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            resultSet = databaseMetaData.getTables(null, null, "%", types);

            //print them:
            while (resultSet.next()){
                System.out.println(resultSet.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String get_table_data(String tablename){
        return "data";
    }

    //closing db connection
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }


}



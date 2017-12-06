package database;

import java.sql.*;
import java.util.List;

public class MySQLConnection{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    // private String tableName; -- will i be using more than one table -- probably (example table colouring trace ? )

   //constructor: generates the connection and saves it
    public MySQLConnection(String databaseName){
        String jdbcUrl = "jdbc:mysql://35.195.54.162:3306/" + databaseName;
        try{
            connection = DriverManager.getConnection(jdbcUrl,"root","finalyear4us3");
            //do stuff with the certificates and all
            //statements allow to issue SQL queries to the database & results gets them back

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void get_tables(){
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            resultSet= databaseMetaData.getTables(null, null, "%", types);
            //print them:
            while (resultSet.next()){
                System.out.println(resultSet.getString("TABLE_NAME"));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }

    }

    //Executing query and returning the information
    private ResultSet execute_query(String query) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //Executing updates to db
    private Integer execute_updates(String update){
        Integer generated_id = 0;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(update);

            //get the image_id generated
            resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
            if (resultSet.next()) {
                generated_id = resultSet.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return generated_id;
    }

    //delete with id number
    public Integer delete(Integer id, String tableName) {
        String query = "delete from " + tableName + " where ID = " + id.toString();
        return execute_updates(query);
    }

    //add data into table
    public Integer post(String values, String tableName){
        String update = "insert into " + tableName + " (IMAGE_LOC) values (" + values + ")";
        return execute_updates(update);
    }

    //update a particular value
    public void put(Integer id, Integer imageLoc, String tableName){
        String update = "update " + tableName + " set IMAGE_LOC = " + imageLoc.toString() + " where ID = " + id.toString();
        execute_updates(update);
    }

    //gets data from what you know and what you want to get
    //ex: select imageloc from tablename where id = image_id
    public String get(String[] info, String tableName, String whatIknow){
        String query = "select " + info[2] + " from ." + tableName + " where " + whatIknow + " = " + info[1];
        ResultSet resultSet;
        resultSet = execute_query(query);
        String response = "";
        try {
            response = resultSet.getString(1);
            close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //close down connection when closing the instance
    private void close_connection() {
         try {
            if (connection != null ) { connection.close();}
         }catch (Exception e) {
             e.printStackTrace();
        }
    }

    public void close_all() {
        close();
        close_connection();
    }
}



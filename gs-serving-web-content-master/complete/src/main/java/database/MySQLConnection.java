package database;

import java.sql.*;
import java.util.List;

public class MySQLConnection{
    private Connection connection;
    private Statement statement;
    private String databaseName;
    private ResultSet resultSet;
    // private String tableName; -- will i be using more than one table -- probably (example table colouring trace ? )

   //constructor: generates the connection and saves it
    public MySQLConnection(String databaseName){
        this.databaseName = databaseName;
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

    //delete with id number
    public void delete(String id, String tableName) {
        String query = "delete from " + tableName + " where ID = " + id;
        execute_query(query);
    }

    //add data into table
    public void post(String values, String ColumnNames, String tableName){
        String query = "insert into " + tableName + " (" + ColumnNames + " ) values (" + values + ")";
        execute_query(query);
        //return the image_id to be saved
    }

    //update a particular value
    public void put(String id, String imageLoc, String tableName){
        String query = "update " + tableName + " set IMAGE_LOC = " + imageLoc + " where ID = " + id;
        execute_query(query);
    }

    public String get(String info, String tableName){
        String query = "";

        switch(info) {
            case "all":
                System.out.println("getting all the elements in the database");
                query = "select ID, IMAGE_LOC from ." + tableName;
                break;
            default:
                System.out.println("getting location of image with id " + info);
                query = "select IMAGE_LOC from ." + tableName + " where ID = " + info;
                break;
        }
        ResultSet resultSet = execute_query(query);

        //Find a more intelligent way of printing out stuff for yourself
        //right now prints the two columns of the data
        //do a list array or something to be returned
        try {
            while(resultSet.next()){
                switch (info) {
                    case "all" :
                        System.out.println("id: " + resultSet.getString(1) + " image_loc:  " +resultSet.getString(2));
                        break;
                    default:
                        System.out.println("image_loc: " + resultSet.getString(1));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return "success";
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



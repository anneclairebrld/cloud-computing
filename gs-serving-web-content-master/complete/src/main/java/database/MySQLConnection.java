package database;

import java.sql.*;
import java.util.List;

public class MySQLConnection{
    private Connection connection;
    private Statement statement;
    private String databaseName;
    private ResultSet resultSet;

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

    public void delete(String id) {
        //method to delete element id in db
    }

    public void put(String id){
        //method to change id data in db
    }

    public Integer post(Integer imageLoc){
        String query = "";

        return 0;
        //add method to save to db
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
//look up the best way of closing down a connection and when to do so  ..
//            if (connection != null) {
//                connection.close();
//            }
        } catch (Exception e) {

        }
    }
}



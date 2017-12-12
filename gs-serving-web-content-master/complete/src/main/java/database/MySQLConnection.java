package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Integer post(String values, Integer[] pixelsize, List<Integer> colours, Integer width, Integer height, String tableName){
        System.out.println("Colours array: " + colours);
        String update = "insert into " + tableName + " (IMAGE_LOC, WIDTH, HEIGHT, PIXELWIDTH, PIXELHEIGHT, COLOUR1, COLOUR2, COLOUR3, COLOUR4, COLOUR5, COLOUR6, COLOUR7, COLOUR8, COLOUR9, COLOUR10, COLOUR11, COLOUR12, COLOUR13, COLOUR14, COLOUR15, COLOUR16, COLOUR17, COLOUR18, COLOUR19, COLOUR20) values ( " + values + ", " + width.toString() + "," + height.toString() + "," + pixelsize[0].toString() + ", " + pixelsize[1].toString() + ", " + colours.get(0) + ", " + colours.get(1) + ", " + colours.get(2) + ", " + colours.get(3) + ", " + colours.get(4) + ", " + colours.get(5) + ", " + colours.get(6) +  ", " + colours.get(7) + ", " + colours.get(8) + ", " + colours.get(9) + ", " + colours.get(10) + ", " + colours.get(11) + ", " + colours.get(12) + ", " + colours.get(13) + ", " + colours.get(14) + ", " + colours.get(15) + ", " + colours.get(16) + ", " + colours.get(17) + ", " + colours.get(18) + ", " + colours.get(19) +  ")";
        Integer ID = execute_updates(update);

        return ID;
    }

    //update a particular value
    public void put(Integer id, Integer imageLoc, String tableName){
        String update = "update " + tableName + " set IMAGE_LOC = " + imageLoc.toString() + " where ID = " + id.toString();
        execute_updates(update);
    }

    //gets data from what you know and what you want to get
    //ex: select imageloc from tablename where id = image_id
    public String get(String[] info, String tableName, String whatIknow){
        String query = "select " + info[1] + " from ." + tableName + " where " + whatIknow + " = " + info[0];
        System.out.println(query);
        ResultSet resultSet;
        resultSet = execute_query(query);
        String response = "";
        try {
            while(resultSet.next()) {
                response = resultSet.getString(1);
            }
            close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public String getAll(String info, String tableName){
        String query = "select " + info + " from " + tableName ;
        ResultSet resultSet;
        resultSet = execute_query(query);
        String response = "";
        try {
            while(resultSet.next()) {
                if (response.equals("")){
                    response = resultSet.getString(1);
                }
                else response = response + "," + resultSet.getString(1);
            }
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



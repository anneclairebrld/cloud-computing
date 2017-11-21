package app;
import java.sql.*;

public class MySQLConnection {
    private final String jdbcURL;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    private String username= "annie_test2";
    private String password= "finalyear4us3";
    enum TestTableColumns{
        id,TEXT;
    }

    public MySQLConnection(String jdbcURL){
        this.jdbcURL = jdbcURL;
    }

    public void readData() throws Exception {
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from javaTestDB.test_table;");
            getResultSet(resultSet);
            preparedStatement = connection.prepareStatement("insert into javaTestDB.test_table values (default,?)");
            preparedStatement.setString(1,"insert test from java");
            preparedStatement.executeUpdate();
        }finally{
            close();
        }
    }
    private void getResultSet(ResultSet resultSet) throws Exception {
        while(resultSet.next()){
            Integer id = resultSet.getInt(TestTableColumns.id.toString());
            String text = resultSet.getString(TestTableColumns.TEXT.toString());
            System.out.println("id: "+id);
            System.out.println("text: "+text);
        }
    }

    private void close(){
        try {
            if(resultSet!=null) resultSet.close();
            if(statement!=null) statement.close();
            if(connection!=null) connection.close();
        } catch(Exception e){}
    }
//    public void connectToDataBase() throws Exception{
//
//        System.out.println("got to the connectToDataBase function");
//        String jdbcUrl = String.format(
//                "jdbc:mysql://google/%s?cloudSqlInstance=%s&"
//                        + "socketFactory=com.google.cloud.sql.mysql.SocketFactory",
//                databaseName,
//                instanceConnectionName);
//        System.out.println("created a jdbcUrl");
//        connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//        System.out.println("blehhh to database");
//        try (Statement statement = connection.createStatement()) {
//            System.out.println("tconnected to database");
//            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
//            if (resultSet.equals(null) ){
//                System.out.println("there is nothhing in the database");
//            }
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1));
//            }
//        }catch (Exception e){
//            throw e;
//        }
//    }

}



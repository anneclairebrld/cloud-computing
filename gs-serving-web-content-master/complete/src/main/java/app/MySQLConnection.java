package app;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private String instanceConnectionName = null;
    private String username = null;
    private String password= null;
    private String databaseName = null;

    public void MySQLConnection(){
        this.instanceConnectionName = "tap-estry-186513:europe-west1:back-end";
        this.username = "annie_test";
        this.password = "deletefile69";
        this.databaseName =  "images";
    }

    public void connectToDataBase() throws Exception, SQLException{
        System.out.println("got to the connectToDataBase function");
        String jdbcUrl = String.format(
                "jdbc:mysql://google/%s?cloudSqlInstance=%s&"
                        + "socketFactory=com.google.cloud.sql.mysql.SocketFactory",
                databaseName,
                instanceConnectionName);
        System.out.println("created a jdbcUrl");
        connection = DriverManager.getConnection(jdbcUrl, username, password);

        System.out.println("blehhh to database");
        try (Statement statement = connection.createStatement()) {
            System.out.println("tconnected to database");
            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
            if (resultSet.equals(null) ){
                System.out.println("there is nothhing in the database");
            }
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        }catch (Exception e){
            throw e;
        }
    }

    public void readDatabase()  {
//        try{
//            // This will load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.jdbc.Driver");
//
//            // Setup the connection with the DB -- db calles images
//            //change the path to connect to the db on the cloud
//            connection = DriverManager
//                    .getConnection("jdbc:mysql://localhost/images?"
//                            + "user=sqluser&password=sqluserpw");
//
//            // Statements allow to issue SQL queries to the database
//            statement = connection.createStatement();
//            // Result set get the result of the SQL query
//            resultSet = statement
//                    .executeQuery("select * from feedback.comments");
//            writeResultSet(resultSet);
//
//            // PreparedStatements can use variables and are more efficient
//            preparedStatement = connection
//                    .prepareStatement("insert into  feedback.comments values (default, ?)");
//            // "imageLocation");
//            // Parameters start with 1
//            preparedStatement.setString(1, "ImageLocation");
//            preparedStatement.executeUpdate();
//
//            preparedStatement = connection
//                    .prepareStatement("SELECT ImageLocation, COMMENTS from feedback.comments");
//            resultSet = preparedStatement.executeQuery();
//            writeResultSet(resultSet);
//
//            // Remove again the insert comment
//            preparedStatement = connection
//                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
//            preparedStatement.setString(1, "ImageLocation");
//            preparedStatement.executeUpdate();
//
//            resultSet = statement
//                    .executeQuery("select * from feedback.comments");
//            writeMetaData(resultSet);
//
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            close();
//        }
    }

//    public void getImage(String imageId) throws Exception{
//        try{
//            //get the precise image asked
//        }catch (Exception e){
//            throw e;
//        }
//    }

}

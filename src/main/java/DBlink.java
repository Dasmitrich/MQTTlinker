import java.sql.*;

public class DBlink {
    String connectionUrl = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false";
    /**
     * JDBC Driver and database url
     */
    Connection connection = null;

    public DBlink(){
        try {
            connection = DriverManager.getConnection(connectionUrl, "root", "1234");
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }


    public void executeQuery(String query){
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        }
    }
}

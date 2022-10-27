package simple_bank_app;

import java.sql.Connection;
import java.sql.DriverManager;
// Global connection Class
public class connection {
    static Connection con; // Global Connection Object
    public static Connection getConnection()
    {
        try {
        	String mysqlJDBCDriver = "com.mysql.cj.jdbc.Driver"; //jdbc driver
            String url = "jdbc:mysql://localhost:3306/bank"; //mysql url
            String user = "root";        //mysql username
            String pass = "";  //mysql passcode
            Class.forName(mysqlJDBCDriver);
            con = DriverManager.getConnection(url, user,pass);
        }
        catch (Exception e) {
            System.out.println("Connection Failed!");
        }
        return con;
    }
}
package app.sql.java.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;

public class Connect {
    public static Connection connect() {
        Connection conn = null;

        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            String url = SetDatabaseUrl.getUrl();
            conn = DriverManager.getConnection(url, config.toProperties());
            System.out.println("Yay");
            
        } catch (SQLException e) {
            System.out.println(":(");
        } 

        return conn;
    }
}
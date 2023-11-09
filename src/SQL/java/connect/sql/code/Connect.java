package src.SQL.java.connect.sql.code;

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
            conn = DriverManager.getConnection("jdbc:sqlite:src/SQL/databases/FoodRecipeDatabase.db", config.toProperties());
            System.out.println("Yay");
            
        } catch (SQLException e) {
            System.out.println(":(");
        } 

        return conn;
    }
}
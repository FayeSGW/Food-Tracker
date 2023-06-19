package src.SQL.java.connect.sql.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import src.db.*;

public class RetrieveDB {
    public static void main (String [] args) {
        retrieveFoods();
        Database d = new Database("");
    }

    public static void retrieveFoods() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/fayes/OneDrive/Documents/GitHub/FoodTrackerJava/SQL/db/FoodRecipeDatabase.db");
            System.out.println("Yay");
            
        } catch (SQLException e) {
            System.out.println(":(");
        } finally {
            try {
                if (conn != null) {
                conn.close();
                }
            } catch (SQLException e) {
                System.out.println(":(");
            }
        }
    }
}
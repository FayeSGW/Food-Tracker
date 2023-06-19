package sql.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import db.*;

public class RetrieveDB {
    public static void main (String [] args) {
        retrieveFoods();
    }

    public static void retrieveFoods() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/fayes/OneDrive/Documents/GitHub/Food-Tracker_Java/SQL/db/FoodRecipeDatabase.db");
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
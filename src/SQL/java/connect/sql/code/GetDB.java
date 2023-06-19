package src.SQL.java.connect.sql.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
//import src.db.*;

public class GetDB {
    /*public static void main (String [] args) {
        getFoods();
        
    }*/

    public static Connection connect() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/fayes/OneDrive/Documents/GitHub/FoodTrackerJava/src/SQL/databases/FoodRecipeDatabase.db");
            System.out.println("Yay");
            
        } catch (SQLException e) {
            System.out.println(":(");
        } 

        return conn;
    }

    public static void getFoods(Database d) {
        //Database d = new Database("");
        //System.out.println("..");
        String sql = "SELECT Name, Weight, Unit, Calories, Fat, SaturatedFat, Carbohydrates, Sugar, Fibre, Protein, Salt, Barcode FROM Foods";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("Name");
                int weight = rs.getInt("Weight");
                String unit = rs.getString("Unit");
                double calories = rs.getDouble("Calories");
                double fat = rs.getDouble("Fat");
                double satfat = rs.getDouble("SaturatedFat");
                double carbs = rs.getDouble("Carbohydrates");
                double sugar = rs.getDouble("Sugar");
                double fibre = rs.getDouble("Fibre");
                double protein = rs.getDouble("Protein");
                double salt = rs.getDouble("Salt");
                String barcode = rs.getString("Barcode");

                //System.out.println(name + " " + weight + "" + unit + " " + calories + " " + fat + " " + satfat + " " + carbs + " " + sugar + " " + fibre + " " + protein + " " + salt + " " + barcode);
                d.addFood(name, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            }
        } catch (SQLException e) {
            System.out.println("):");
        } finally {
            System.out.println(d);
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }
}
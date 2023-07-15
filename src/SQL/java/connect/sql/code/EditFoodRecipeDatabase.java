package src.SQL.java.connect.sql.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.sqlite.SQLiteConfig;
import java.time.LocalDate;

import src.db.*;
import src.diary.*;

public class EditFoodRecipeDatabase {

    public static Connection connect() {
        Connection conn = null;

        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/fayes/OneDrive/Documents/GitHub/FoodTrackerJava/src/SQL/databases/FoodRecipeDatabase.db", config.toProperties());
            System.out.println("Yay");
            
        } catch (SQLException e) {
            System.out.println(":(");
        } 

        return conn;
    }

    public static void addRecipeIngredient(String recipeName, String foodName, double amount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newString = "INSERT INTO RecipeIngredients(RecipeName, FoodName, IngredientAmount) VALUES(?,?,?)";

        try {
            conn = connect();
            stmt = conn.prepareStatement(newString);

            stmt.setString(1, recipeName);
            stmt.setString(2, foodName);
            stmt.setDouble(3, amount);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("aa nei " + e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }
    

    public static void addRecipe(String name, double servings) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newString = "INSERT INTO Recipes(RecipeName, Servings) VALUES(?, ?)";

        try {
            conn = connect();
            stmt = conn.prepareStatement(newString);

            stmt.setString(1, name);
            stmt.setDouble(2, servings);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("aa nei " + e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void addFood(String oldName, String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newString = "INSERT INTO Foods(FoodName, DisplayName, Weight, Unit, Calories, Fat, SaturatedFat, Carbohydrates, Sugar, Fibre, Protein, Salt, Barcode) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String exString = "UPDATE Foods SET FoodName = ?, DisplayName = ?, Weight = ?, Unit = ?, Calories = ?, Fat = ?, SaturatedFat = ?, Carbohydrates = ?, Sugar = ?, Fibre = ?, Protein = ?, Salt = ?, Barcode = ? WHERE FoodName  = ?";

        try {
            conn = connect();

            if (oldName == null) {
                stmt = conn.prepareStatement(newString);
            } else {
                stmt = conn.prepareStatement(exString);

                stmt.setString(14, oldName);
            }
            stmt.setString(1, name);
            stmt.setString(2, displayName);
            stmt.setDouble(3, amount);
            stmt.setString(4, unit);
            stmt.setDouble(5, calories);
            stmt.setDouble(6, fat);
            stmt.setDouble(7, satfat);
            stmt.setDouble(8, carbs);
            stmt.setDouble(9, sugar);
            stmt.setDouble(10, fibre);
            stmt.setDouble(11, protein);
            stmt.setDouble(12, salt);
            stmt.setString(13, barcode);

            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("aa nei " + e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void deleteFood(String name, String type) {
        Connection conn = null;
        PreparedStatement stmt = null, stmt2 = null;
        String foodString = "DELETE FROM Foods WHERE FoodName = ?";
        String ingredientsString = "DELETE FROM RecipeIngredients WHERE RecipeName = ?";
        String recipeString = "DELETE FROM Recipes WHERE RecipeName = ?";

        try {
            conn = connect();

            if (type.equals("food")) {
                stmt = conn.prepareStatement(foodString);
            } else {
                stmt = conn.prepareStatement(ingredientsString);
            }
            
            stmt.setString(1, name);
            stmt.executeUpdate();

            if (type.equals("recipe")) {
                stmt2 = conn.prepareStatement(recipeString);
                stmt2.setString(1, name);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("aa nei " + e.getMessage());
        } finally {
            try {
                stmt.close();

                if (stmt2 != null) {
                    stmt2.close();
                }
                
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            } 
        }
    }

    public static void deleteRecipe(String name) {

    }

}
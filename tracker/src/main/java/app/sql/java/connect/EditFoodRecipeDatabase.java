package app.sql.java.connect;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.db.*;
import app.diary.*;

public class EditFoodRecipeDatabase {

    public static void addRecipeIngredient(String recipeName, String foodName, double amount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newString = "INSERT INTO RecipeIngredients(RecipeName, FoodName, IngredientAmount) VALUES(?,?,?) ON CONFLICT(RecipeName, FoodName) DO UPDATE SET IngredientAmount = ?";
        try {
            conn = Connect.connect();

            stmt = conn.prepareStatement(newString);
            stmt.setString(1, recipeName);
            stmt.setString(2, foodName);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, amount);

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

    public static void addRecipeInstructions(String name, String instruct) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String strng = "UPDATE Recipes SET Instructions = ? WHERE RecipeName = ?";
        try {
            conn = Connect.connect();
            stmt = conn.prepareStatement(strng);

            stmt.setString(1, instruct);
            stmt.setString(2, name);

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

    public static void removeIngredient(String recipeName, String foodName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String strng = "DELETE FROM RecipeIngredients WHERE RecipeName = ? AND FoodName = ?";
        try {
            conn = Connect.connect();
            stmt = conn.prepareStatement(strng);

            stmt.setString(1, recipeName);
            stmt.setString(2, foodName);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":(" + e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void addRecipe(int index, String oldName, String newName, double servings) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newString = "INSERT INTO Recipes(RecipeName, Servings, RecipeID) VALUES(?, ?, ?)";
        String exString = "UPDATE Recipes SET RecipeName = ?, Servings = ? WHERE RecipeID = ?";
        try {
            conn = Connect.connect();
            if (oldName == null) {
                stmt = conn.prepareStatement(newString);
            } else {
                stmt = conn.prepareStatement(exString);
            }
            stmt.setString(1, newName);
            stmt.setDouble(2, servings);
            stmt.setInt(3, index);

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

    public static void addFood(int index, String oldName, String name, String displayName, double amount, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newString = "INSERT INTO Foods(FoodName, DisplayName, Weight, Unit, Calories, Fat, SaturatedFat, Carbohydrates, Sugar, Fibre, Protein, Salt, Barcode, FoodID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String exString = "UPDATE Foods SET FoodName = ?, DisplayName = ?, Weight = ?, Unit = ?, Calories = ?, Fat = ?, SaturatedFat = ?, Carbohydrates = ?, Sugar = ?, Fibre = ?, Protein = ?, Salt = ?, Barcode = ? WHERE FoodID  = ?";
        try {
            conn = Connect.connect();

            if (oldName == null) {
                stmt = conn.prepareStatement(newString);
            } else {
                stmt = conn.prepareStatement(exString);
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
            stmt.setInt(14, index);

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

    public static boolean checkIfContains(Connection conn, String itemName, String itemType) {
        PreparedStatement stmtDiary = null, stmtRecipes = null;
        String searchRecipes = null, searchDiary = null;
        ResultSet rsRecipes = null, rsDiary = null;
        boolean contains = true;
        try {
            if (conn == null) {
                conn = Connect.connect();
            }

            if (itemType.equals("food")) {
                searchDiary = "SELECT COUNT(*) FROM FoodsInDiary WHERE FoodName = ?"; 
            } else if (itemType.equals("recipe")) {
                searchDiary = "SELECT COUNT(*) FROM FoodsInDiary WHERE RecipeName = ?"; 
            } 

            searchRecipes = "SELECT COUNT(*) FROM RecipeIngredients WHERE FoodName = ?";
            stmtRecipes = conn.prepareStatement(searchRecipes);
            stmtRecipes.setString(1, itemName);
            rsRecipes = stmtRecipes.executeQuery();

            stmtDiary = conn.prepareStatement(searchDiary);
            stmtDiary.setString(1, itemName);
            rsDiary = stmtDiary.executeQuery();

            if (rsDiary.getInt(1) == 0 && rsRecipes.getInt(1) == 0) {
                contains = false;
            }
        } catch (SQLException e) {
                System.out.println(e.getMessage());
        } finally {
            try {
                rsDiary.close();
                rsRecipes.close();
                stmtDiary.close();
            } catch (SQLException e) {}
        }
        return contains;
    }

    public static boolean deleteFood(String name, String type) {
        boolean actuallyDeleted = false;
        
        Connection conn = null;
        PreparedStatement stmt = null, stmt2 = null;
        //Deletion queries
        String foodString = "DELETE FROM Foods WHERE FoodName = ?";
        String ingredientsString = "DELETE FROM RecipeIngredients WHERE RecipeName = ?";
        String recipeString = "DELETE FROM Recipes WHERE RecipeName = ?";

        //Update queries to mark as "deleted"
        String foodUpdate = "UPDATE Foods SET Deleted = ? WHERE FoodName = ?";
        String recipeUpdate = "UPDATE Recipes SET Deleted = ? WHERE RecipeName = ?";
        try {
            conn = Connect.connect();

            if (checkIfContains(conn, name, type)) {
                //Don't delete from database, mark as "deleted" instead
                // This means that if foods/recipes have already been entered into the diary
                // or as a recipe ingredient, those entries are not affected
                // But the "deleted" item no longer shows up in searches, so as far as the user
                // is concerned, it has been deleted.
                if (type.equals("food")) {
                    stmt = conn.prepareStatement(foodUpdate);
                } else if (type.equals("recipe")) {
                    stmt = conn.prepareStatement(recipeUpdate);
                }
                stmt.setInt(1, 1);
                stmt.setString(2, name);
                stmt.executeUpdate();
            } else {
                actuallyDeleted = true;
                if (type.equals("food")) {
                    stmt = conn.prepareStatement(foodString);
                } else if (type.equals("recipe")) {
                    stmt = conn.prepareStatement(ingredientsString);
                }

                stmt.setString(1, name);
                stmt.executeUpdate();

                if (type.equals("recipe")) {
                    stmt2 = conn.prepareStatement(recipeString);
                    stmt2.setString(1, name);
                    stmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("aa nei " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            } 
        }
        //If the item was able to be deleted from the saved database, pass this to the 
        // in-memory database to update it too
        return actuallyDeleted;
    }
}
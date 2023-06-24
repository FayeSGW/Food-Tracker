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

public class AddToDiary {

    /*public static void main (String [] args) {
        User user = GetFoodsDB.getUser();
        //addUser(user);

        Day day = new Day(LocalDate.now(), user);
        //addDay(day);
        Database db = user.accessDatabase();
        System.out.println(db);

        //addFood(user, day, "Dinner", "Banana", 250);
        //addFood(user, day, "Snacks", "Cup of Tea", 2);
    }*/

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

    public static void addUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "INSERT INTO UserData(Name, Gender, Weight, Height, DOB, Goal, Rate, Water) VALUES(?,?,?,?,?,?,?,?)";

        try {
            conn = connect();
            stmt = conn.prepareStatement(string);

            stmt.setString(1, user.showName());
            stmt.setString(2, user.showGender());
            stmt.setDouble(3, user.showWeight());
            stmt.setInt(4, user.showHeight());
            stmt.setString(5, user.showDOB());
            stmt.setString(6, user.showGoal());
            stmt.setDouble(7, user.showRate());
            stmt.setInt(8, user.showWater());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":(");
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void addWater(Day day) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "UPDATE Days SET Water = ? WHERE Date = ?";

        try {
            conn = connect();
            stmt = conn.prepareStatement(string);

            stmt.setInt(1, day.showWaterDrunk());
            stmt.setString(2, day.showDate().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":(( " +  e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
        
    }

    public static void addDay(Day day) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "INSERT INTO Days(Date, Water) VALUES(?,?)";

        try {
            conn = connect();
            stmt = conn.prepareStatement(string);

            stmt.setString(1, day.showDate().toString());
            stmt.setInt(2, day.showWaterDrunk());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(";(" + e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void addFood(User user, Day day, String meal, String name, int amount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "";
        int nameIndex = 0;
        String date = day.showDate().toString();

        src.SQL.java.connect.sql.code.Database data = user.accessDatabase();
        if (data.isRecipe(name)) {
            string = "INSERT INTO FoodsInDiary(Meal, RecipeName, Amount, Date) VALUES (?,?,?,?)";
            //System.out.println(name + " " + data.isRecipe(name));
            //nameIndex = 5;
        } else {
            string = "INSERT INTO FoodsInDiary(Meal, FoodName, Amount, Date) VALUES (?,?,?,?)";
            //nameIndex = 4;
        }

        try {
            conn = connect();
            stmt = conn.prepareStatement(string);

            stmt.setString(1, meal);
            stmt.setString(2, name);
            stmt.setInt(3, amount);
            stmt.setString(4, date);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":( "+ e.getErrorCode());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }

        
    }
}
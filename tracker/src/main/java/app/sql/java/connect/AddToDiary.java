package app.sql.java.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.sqlite.SQLiteConfig;
import java.time.LocalDate;

import app.db.*;
import app.diary.*;

public class AddToDiary {


    public static void addUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "INSERT INTO UserData(Name, Gender, Weight, Height, DOB, Goal, Rate, Water) VALUES(?,?,?,?,?,?,?,?)";

        try {
            conn = Connect.connect();
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


    public static void updateWeight(Day day, User user) {
        Connection conn = null;
        PreparedStatement dayStmt = null, userStmt = null;
        String dayWeight = "UPDATE Days SET Weight = ? WHERE Date = ?";
        String userWeight = "UPDATE UserData SET Weight = ?";

        try {
            conn = Connect.connect();
            dayStmt = conn.prepareStatement(dayWeight);
            
            dayStmt.setDouble(1, day.showWeight());
            dayStmt.setString(2, day.showDate().toString());

            dayStmt.executeUpdate();

            userStmt = conn.prepareStatement(userWeight);

            userStmt.setDouble(1, user.showWeight());

            userStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":( weight " + e.getMessage());
        } finally {
            try {
                userStmt.close();
                dayStmt.close();
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
            conn = Connect.connect();
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
            conn = Connect.connect();
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

    public static void addFood(User user, String date, String meal, String name, double amount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "";
        int nameIndex = 0;
        //String date = day.showDate().toString();

        Database data = user.accessDatabase();
        if (data.isRecipe(name)) {
            string = "INSERT INTO FoodsInDiary(Meal, RecipeName, Amount, Date) VALUES (?,?,?,?)";
            //System.out.println(name + " " + data.isRecipe(name));
            //nameIndex = 5;
        } else {
            string = "INSERT INTO FoodsInDiary(Meal, FoodName, Amount, Date) VALUES (?,?,?,?)";
            //nameIndex = 4;
        }

        try {
            conn = Connect.connect();
            stmt = conn.prepareStatement(string);

            stmt.setString(1, meal);
            stmt.setString(2, name);
            stmt.setDouble(3, amount);
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

    public static void removeFood(String date, String meal, String name, String type) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String foodString = "DELETE FROM FoodsInDiary WHERE Date = ? AND Meal = ? AND FoodName = ?";
        String recipeString = "DELETE FROM FoodsInDiary WHERE Date = ? AND Meal = ? AND RecipeName = ?";

        try {
            conn = Connect.connect();

            if (type.equals("food")) {
                stmt = conn.prepareStatement(foodString);
            } else {
                stmt = conn.prepareStatement(recipeString);
            }

            stmt.setString(1, date);
            stmt.setString(2, meal);
            stmt.setString(3, name);
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

    public static void addExercise(int index, String date, String name, int minutes, int seconds, int calories, String oldName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "INSERT INTO Workouts(WorkoutName, Minutes, Seconds, Calories, Date, ID) VALUES (?,?,?,?,?,?)";
        String update = "UPDATE Workouts SET WorkoutName = ?, Minutes = ?, Seconds = ?, Calories = ?, Date = ?, ID = ? WHERE WorkoutName = ?";

        try {
            conn = Connect.connect();

            if (oldName == null) {
                stmt = conn.prepareStatement(string);
            } else {
                stmt = conn.prepareStatement(update);

                stmt.setString(7, oldName);
            }
            
            
            stmt.setString(1, name);
            stmt.setInt(2, minutes);
            stmt.setInt(3, seconds);
            stmt.setInt(4, calories);
            stmt.setString(5, date);
            stmt.setInt(6, index);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":( " + e.getErrorCode());
    
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void removeWorkout(String date, Integer index) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String strng = "DELETE FROM Workouts WHERE ID = ?";
        
        try {
            conn = Connect.connect();
            stmt = conn.prepareStatement(strng);

            //stmt.setString(1, date);
            stmt.setInt(1, index);

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
}
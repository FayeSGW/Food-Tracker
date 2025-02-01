package app.sql.java.connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.db.*;
import app.diary.*;

public class AddToDiary {

    public static void addUser(User user, String oldName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String newstring = "INSERT INTO UserData(Name, Gender, Weight, Height, DOB, Goal, Rate, Water) VALUES(?,?,?,?,?,?,?,?)";
        String updateString = "UPDATE UserData SET Name = ?, Gender = ?, Weight = ?, Height = ?, DOB = ?, GOal = ?, Rate = ?, Water = ? WHERE Name = ?";

        try {
            conn = Connect.connect();

            if (oldName == null) {
                stmt = conn.prepareStatement(newstring);
            } else {
                stmt = conn.prepareStatement(updateString);
                stmt.setString(9, oldName);
            }
            
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

    public static void updateMeasurements(Day day, User user) {
        Connection conn = null;
        PreparedStatement dayStmt = null, userStmt = null;
        String dayMeasurements = "UPDATE Days SET Waist = ?, Hips = ?, Calf = ?, Thigh = ?, UpperArm = ?, Chest = ?, Underwire = ?, BodyFat = ? WHERE Date = ?";
        String userMeasurements = "UPDATE UserData SET Waist = ?, Hips = ?, Calf = ?, Thigh = ?, UpperArm = ?, Chest = ?, Underwire = ?, BodyFat = ?";
        String[] types = {"Waist", "Hips", "Calf", "Thigh", "Upper Arm", "Chest", "Underwire", "Body Fat"};

        try {
            conn = Connect.connect();
            dayStmt = conn.prepareStatement(dayMeasurements);
            userStmt = conn.prepareStatement(userMeasurements);

            dayStmt.setString(9, day.showDate().toString());

            for (int i = 0; i < types.length; i++) {
                dayStmt.setDouble(i+1, day.getSingleMeasurement(types[i]));
                userStmt.setDouble(i+1, user.getSingleMeasurement(types[i]));
            }
            dayStmt.setDouble(1, day.getSingleMeasurement("Waist"));
            //dayStmt.setDouble(8, day.getBodyFatPercentage());
            //userStmt.setDouble(8, user.showBodyFat());
            dayStmt.executeUpdate();
            userStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":( measurements" + e.getMessage());
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

            userStmt.setDouble(1, day.showWeight());

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
            System.out.println(":( addDayToDiary" + e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void addFood(Database data, String type, String date, String meal, String name, int id, double amount) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "";

        if (data.isRecipe(id)) {
            if (type.equals("new")) {
                string = "INSERT INTO FoodsInDiary(Meal, RecipeName, Amount, Date, RecipeID) VALUES (?,?,?,?,?)";
            } else {
                string = "UPDATE FoodsInDiary SET Amount = ? WHERE Meal = ? AND RecipeID = ? AND Date = ?";
            }
        } else {
            if (type.equals("new")) {
                string = "INSERT INTO FoodsInDiary(Meal, FoodName, Amount, Date, FoodID) VALUES (?,?,?,?,?)";
            } else {
                string = "UPDATE FoodsInDiary SET Amount = ? WHERE Meal = ? AND FoodID = ? AND Date = ?";
            }
        }

        try {
            conn = Connect.connect();
            stmt = conn.prepareStatement(string);
            if (type.equals("new")) {
                stmt.setString(1, meal);
                stmt.setString(2, name);
                stmt.setDouble(3, amount);
                stmt.setString(4, date);
                stmt.setInt(5, id);
            } else {
                stmt.setDouble(1, amount);
                stmt.setString(2, meal);
                stmt.setInt(3, id);
                stmt.setString(4, date);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(":( "+ e.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void removeFood(Database data, String date, String meal, int index) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String foodString = "DELETE FROM FoodsInDiary WHERE Date = ? AND Meal = ? AND FoodID = ?";
        String recipeString = "DELETE FROM FoodsInDiary WHERE Date = ? AND Meal = ? AND RecipeID = ?";

        try {
            conn = Connect.connect();

            if (!data.isRecipe(index)) {
                stmt = conn.prepareStatement(foodString);
            } else {
                stmt = conn.prepareStatement(recipeString);
            }

            stmt.setString(1, date);
            stmt.setString(2, meal);
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

    public static void addExercise(int index, String date, String name, int minutes, int seconds, int calories, String oldName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String string = "INSERT INTO Workouts(WorkoutName, Minutes, Seconds, Calories, Date, ID) VALUES (?,?,?,?,?,?)";
        String update = "UPDATE Workouts SET WorkoutName = ?, Minutes = ?, Seconds = ?, Calories = ?, Date = ?, ID = ? WHERE ID = ?";

        try {
            conn = Connect.connect();
            
            if (oldName == null) {
                stmt = conn.prepareStatement(string);
            } else {
                stmt = conn.prepareStatement(update);

                stmt.setInt(7, index);
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
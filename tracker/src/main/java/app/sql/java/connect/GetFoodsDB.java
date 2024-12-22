package app.sql.java.connect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;

import app.db.*;
import app.diary.*;
import exceptions.NoNegativeException;

public class GetFoodsDB {
    public static User getUser() {
        String userSearch = "SELECT Name, Gender, Weight, Height, DOB, Goal, Rate, Water, Waist, Hips, Calf, Thigh, UpperArm, Chest, Underwire, BodyFat FROM UserData";
        Connection conn = null;
        Statement userStmt = null;
        ResultSet urs = null;
        User user = null;

        try {
            conn = Connect.connect();
            userStmt = conn.createStatement();
            urs = userStmt.executeQuery(userSearch);

            while (urs.next()) {
                String name = urs.getString("Name");
                String gender = urs.getString("Gender");
                double weight = urs.getDouble("Weight");
                int height = urs.getInt("Height");
                String DOB = urs.getString("DOB");
                String goal = urs.getString("Goal");
                double rate = urs.getDouble("Rate");
                int water = urs.getInt("Water");

                String[] measurementTypes = {"Waist", "Hips", "Calf", "Thigh", "UpperArm", "Chest", "Underwire", "BodyFat"};
                HashMap<String, Double> measurements = new HashMap<>();

                for (String type: measurementTypes) {
                    measurements.put(type, urs.getDouble(type));
                }

                user = new User(name, gender, weight, height, DOB, goal, rate, water, measurements);
                Database data = user.accessDatabase();
                Diary diary = user.accessDiary();

                getFoods(conn, data);
                getDiary(conn, diary);

                return user;
            } 
        } catch (NoNegativeException | SQLException e) {
            System.out.println(":( getuser " + e.getMessage());
        } finally {
            try {
                urs.close();
                userStmt.close();
                conn.close();
            } catch (SQLException | NullPointerException e) {
                System.out.println("!");
            }
        }
        return user;
    }

    public static void getFoods(Connection conn, Database d) {
        String foods = "SELECT FoodID, FoodName, Deleted, DisplayName, Weight, Unit, Calories, Fat, SaturatedFat, Carbohydrates, Sugar, Fibre, Protein, Salt, Barcode FROM Foods";
        String recipes = "SELECT RecipeID, FoodID, FoodName, Deleted, Servings, IngredientAmount, Recipes.RecipeName, Instructions FROM RecipeIngredients RIGHT JOIN Recipes USING (RecipeID)";
        Statement stmt = null;
        ResultSet frs = null, rrs = null;

        try {
            stmt = conn.createStatement();
            frs = stmt.executeQuery(foods);
            
            //Adding foods to database
            while (frs.next()) {
                int index = frs.getInt("FoodID");
                String name = frs.getString("FoodName");
                int deleted = frs.getInt("Deleted");
                String displayName = frs.getString("DisplayName");
                if (displayName == null) {
                    displayName = name;
                }
                int weight = frs.getInt("Weight");
                String unit = frs.getString("Unit");
                double calories = frs.getDouble("Calories");
                double fat = frs.getDouble("Fat");
                double satfat = frs.getDouble("SaturatedFat");
                double carbs = frs.getDouble("Carbohydrates");
                double sugar = frs.getDouble("Sugar");
                double fibre = frs.getDouble("Fibre");
                double protein = frs.getDouble("Protein");
                double salt = frs.getDouble("Salt");
                String barcode = frs.getString("Barcode");

                d.addFood(index, deleted, name, displayName, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
                
            }

            //Adding recipes and their ingredients to database
            rrs = stmt.executeQuery(recipes);
            while (rrs.next()) {
                int index = rrs.getInt("RecipeID");
                String recipeName = rrs.getString("RecipeName");
                int deleted = frs.getInt("Deleted");
                double servings = rrs.getDouble("Servings");
                //String ingredient = rrs.getString("FoodName");
                int foodIndex = rrs.getInt("FoodID");
                double ingredientAmount = rrs.getDouble("IngredientAmount");
                String instructs = rrs.getString("Instructions");
                
                Recipe rec = (Recipe)d.getItemFromIndex(index);

                if (rec == null) {
                    rec = d.addRecipe(index, deleted, recipeName, servings);
                }

                if (foodIndex > 0) {
                    rec.addNonTempIngredient(foodIndex, ingredientAmount);
                }
                
                rec.addInstructions(instructs);
            }
        } catch (SQLException e) {
            System.out.println(":( getdb" + e.getErrorCode());
        } finally {
            try {
                frs.close();
                rrs.close();
                stmt.close();
            } catch (SQLException | NullPointerException e) {
                System.out.println("!");
            }
        }
    }

    public static void getDiary(Connection conn, Diary diary) {
        String dayString = "SELECT Date, Water , Weight FROM Days ORDER BY Date ASC";
        String mealStrng = "SELECT Meal, FoodID, FoodName, RecipeID, RecipeName, Amount, Days.Date FROM FoodsInDiary INNER JOIN Days ON FoodsInDiary.Date = Days.Date";
        String exercises = "SELECT ID, WorkoutName, Minutes, Seconds, Calories, Days.Date FROM Workouts INNER JOIN Days ON Workouts.Date = Days.Date";

        Statement dayStmt = null, foodStmt = null, exStmt = null;
        ResultSet drs = null, frs = null, ers = null;

        try { 
            dayStmt = conn.createStatement();
            drs = dayStmt.executeQuery(dayString);

            while (drs.next()) {
                String dateString = drs.getString("Date");
                int water = drs.getInt("Water");
                double weight = drs.getDouble("Weight");
                LocalDate date = LocalDate.parse(dateString);
                Day dayObj = diary.addSavedDays(date);

                dayObj.addWater(water);
                if (weight > 0) {
                    dayObj.addWeight(weight);
                }
                
            }
            foodStmt = conn.createStatement();
            frs = foodStmt.executeQuery(mealStrng);
            
            while (frs.next()) {
                String meal = frs.getString("Meal");
                int foodID = frs.getInt("FoodID");
                String food = frs.getString("FoodName");
                int recipeID = frs.getInt("RecipeID");
                String recipe = frs.getString("RecipeName");
                double amount = frs.getDouble("Amount");
                String dateString = frs.getString("Date");
                
                LocalDate date = LocalDate.parse(dateString);
                Day dayObj = diary.goToDay(date);
                
                if (recipe == null) {
                    dayObj.addByIndex(meal, foodID, amount);
                } else {
                    dayObj.addByIndex(meal, recipeID, amount);
                }
            }
            exStmt = conn.createStatement();
            ers = exStmt.executeQuery(exercises);

            while (ers.next()) {
                int index = ers.getInt("ID");
                String exercise = ers.getString("WorkoutName");
                int exMins = ers.getInt("Minutes");
                int exSecs = ers.getInt("Seconds");
                int exCals = ers.getInt("Calories");
                String dateString = ers.getString("Date");
                LocalDate date = LocalDate.parse(dateString);
                Day dayObj = diary.goToDay(date);
                
                dayObj.addExercise(index, exercise, exMins, exSecs, exCals);
            }
        } catch (SQLException e) {
            System.out.println(":( getdiary" + e.getErrorCode());
        } finally {
            try {
                ers.close();
                frs.close();
                drs.close();
                exStmt.close();
                foodStmt.close();
                dayStmt.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }
}
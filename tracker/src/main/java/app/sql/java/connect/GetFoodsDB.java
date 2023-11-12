package app.sql.java.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.sqlite.SQLiteConfig;
import java.time.LocalDate;

import app.db.*;
import app.diary.*;

public class GetFoodsDB {

    public static User getUser() {
        String userSearch = "SELECT Name, Gender, Weight, Height, DOB, Goal, Rate, Water FROM UserData";
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

                user = new User(name, gender, weight, height, DOB, goal, rate, water);

                Database data = user.accessDatabase();
                Diary diary = user.accessDiary();

                getFoods(conn, data);
                getDiary(conn, diary);

                return user;
            } 
        } catch (SQLException e) {
            System.out.println(":( noes " + e.getMessage());
        } finally {
            try {
                urs.close();
                userStmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
        return user;
    }

    public static void getFoods(Connection conn, Database d) {
        String foods = "SELECT FoodName, DisplayName, Weight, Unit, Calories, Fat, SaturatedFat, Carbohydrates, Sugar, Fibre, Protein, Salt, Barcode FROM Foods";
        String recipes = "SELECT FoodName, Servings, IngredientAmount, Recipes.RecipeName, Instructions FROM RecipeIngredients INNER JOIN Recipes ON RecipeIngredients.RecipeName = Recipes.RecipeName";
        Statement stmt = null;
        //Statement stmt2 = null;
        ResultSet frs = null;
        ResultSet rrs = null;

        try {
            //conn = connect();
            stmt = conn.createStatement();
            frs = stmt.executeQuery(foods);
            
            //Adding foods to database
            while (frs.next()) {
                String name = frs.getString("FoodName");
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

                d.addFood(name, displayName, weight, unit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, barcode);
            }

            //Adding recipes to database
            //stmt = conn.createStatement();
            rrs = stmt.executeQuery(recipes);
            while (rrs.next()) {
                String recipeName = rrs.getString("RecipeName");
                double servings = rrs.getDouble("Servings");
                String ingredient = rrs.getString("FoodName");
                double ingredientAmount = rrs.getDouble("IngredientAmount");
                String instructs = rrs.getString("Instructions");
                
                Recipe rec = (Recipe)d.findItem(recipeName);
                if (rec == null) {
                    rec = d.addRecipe(recipeName, servings);
                }
                
                rec.addIngredient(ingredient, ingredientAmount);
                rec.addInstructions(instructs);
            }
        } catch (SQLException e) {
            System.out.println(":( " + e.getErrorCode());
        } finally {
            try {
                frs.close();
                rrs.close();
                stmt.close();
            } catch (SQLException e) {
                System.out.println("!");
            }
        }
    }

    public static void getDiary(Connection conn, Diary diary) {
        String dayString = "SELECT Date, Water , Weight FROM Days";
        String mealStrng = "SELECT Meal, FoodName, RecipeName, Amount, Days.Date FROM FoodsInDiary INNER JOIN Days ON FoodsInDiary.Date = Days.Date";
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

                //LocalDate date = LocalDate.of(year, month, day);
                LocalDate date = LocalDate.parse(dateString);
                Day dayObj = diary.addSavedDays(date);

                dayObj.addWater(water);
                dayObj.addWeight(weight);
            }

            foodStmt = conn.createStatement();
            frs = foodStmt.executeQuery(mealStrng);
            
            while (frs.next()) {
                String meal = frs.getString("Meal");
                String food = frs.getString("FoodName");
                String recipe = frs.getString("RecipeName");
                double amount = frs.getDouble("Amount");

                String dateString = frs.getString("Date");
                
                LocalDate date = LocalDate.parse(dateString);
                Day dayObj = diary.getDay(date);

                if (recipe == null) {
                    dayObj.addFood(meal, food, amount);
                } else {
                    dayObj.addFood(meal, recipe, amount);
                }
            }

            exStmt = conn.createStatement();
            ers = exStmt.executeQuery(exercises);

            while (ers.next()) {
                int index = ers.getInt("ID");
                String exercise = ers.getString("WorkoutName");
                System.out.println(exercise);
                int exMins = ers.getInt("Minutes");
                int exSecs = ers.getInt("Seconds");
                int exCals = ers.getInt("Calories");

                String dateString = ers.getString("Date");
                
                LocalDate date = LocalDate.parse(dateString);
                Day dayObj = diary.getDay(date);
                
                dayObj.addExercise(index, exercise, exMins, exSecs, exCals);
            }
        } catch (SQLException e) {
            System.out.println("Noes" + e.getErrorCode());
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
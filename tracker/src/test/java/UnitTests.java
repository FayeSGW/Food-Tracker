import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.lang.Exception;

import app.db.*;
import app.diary.*;
import app.sql.java.connect.Config;
import exceptions.NoNegativeException;


public class UnitTests {

    Database stubDB = new Database("DB");
    Recipe stubRecipe1 = new Recipe(stubDB, null, "Recipe Name1", 4);
    static UnitTestMethods.Stubs stubs;
    
    
    @BeforeAll
    public static void setTestingEnv() {
        Config.setTesting(true);
    }

    @BeforeAll
    public static void getStubs() {
        stubs = UnitTestMethods.getStubs();
    }

    //-------------------FOOD TESTS------------------
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjects")
    public void test001_AssertGettersForNewFoodObject(Food food, String expectedName, String expectedDisplayName, int expectedID, String expectedAmountString, double[] expectedNutritionArray, double[] expectedUnitNutritionArray, String expectedBarcode, int expectedNumberFoodTypes) {
        assertEquals(expectedName, food.showName());
        assertEquals(expectedDisplayName, food.showDisplayName()); //Should show the given display name, or the full name if no display name given
        assertEquals(expectedID, food.showIndex());
        assertEquals(expectedAmountString, food.showWeight());
        assertArrayEquals(expectedNutritionArray, food.showNutrition());
        assertArrayEquals(expectedUnitNutritionArray, food.showUnitNutrition());
        assertEquals(expectedBarcode, food.showBarcode());
        assertEquals(expectedNumberFoodTypes, food.showFoodTypes().size());
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesEditedFoodObjects")
    public void test002_AssertEditsApplyCorrectly(Food food, String newName, String newDisplayName, double newWeight, String newUnit, String newAmountString, 
        double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein,
        double salt, double[] expectedNutritionArray, double[] expectedUnitNutritionArray, String newBarcode, int expectedNumberFoodTypes) {

        food.edit(newName, newDisplayName, newWeight, newUnit, calories, fat, satfat, carbs, sugar, fibre, protein, salt, newBarcode);

        assertEquals(newName, food.showName());
        assertEquals(newDisplayName, food.showDisplayName());
        assertEquals(newBarcode, food.showBarcode());
        assertEquals(newAmountString, food.showWeight());
        assertEquals(newWeight, food.showAmount());
        assertEquals(newUnit, food.showUnit());
        assertArrayEquals(expectedNutritionArray, food.showNutrition());
        assertArrayEquals(expectedUnitNutritionArray, food.showUnitNutrition());
        assertEquals(expectedNumberFoodTypes, food.showFoodTypes().size());

        //Reset food stubs for use in other tests
        if (food == stubs.stubFoodWithDisplayName) {
            stubs.stubFoodWithDisplayName.edit("Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
        } else if (food == stubs.stubFoodWithNullDisplayName) {
            stubs.stubFoodWithNullDisplayName.edit("Full Name2", null, 50, "ml", 350, 12.3, 3, 24, 10, 14, 37, 0.4, null);
        }
    }


    @Nested
    @DisplayName("Adding and Removing Food Types")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class TestingFoodTypes {
        @Test
        public void test003_AssertFoodTypeAddedCorrectly() {
            ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Food Type"));
            Food food = stubs.stubFoodWithDisplayName;
            food.addFoodType("Food Type");
            HashSet<String> actual = food.showFoodTypes();
            assertEquals(1, actual.size());
            assertIterableEquals(expected, actual);
        }

        @Test
        public void test003_AssertIfSameFoodTypeAddedTwiceItIsOnlyListedOnce() {
            ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Food Type"));
            Food food = stubs.stubFoodWithDisplayName;
            food.addFoodType("Food Type");
            HashSet<String> actual = food.showFoodTypes();
            assertEquals(1, actual.size());
            assertIterableEquals(expected, actual);
        }
        //Also check adding second food type

        @Test
        public void test003_AssertIfNonExistentFoodTypeRemovedNothingHappens() {
            ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Food Type"));
            Food food = stubs.stubFoodWithDisplayName;
            food.removeFoodType("Nothing");
            HashSet<String> actual = food.showFoodTypes();
            assertEquals(1, actual.size());
            assertIterableEquals(expected, actual);
        }
    
        @Test
        public void test004_AssertFoodTypeRemovedCorrectly() {
            Food food = stubs.stubFoodWithDisplayName;

            food.removeFoodType("Food Type");
            HashSet<String> actual = food.showFoodTypes();
            HashSet<String> expected = new HashSet<String>();

            assertEquals(0, actual.size());
            assertIterableEquals(expected, actual);
        }
        //Check removing food type from empty list?

        //Check inRecipes list when adding to recipes too
    }


    //----------------------RECIPE TESTS--------------------
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesNewRecipeObjects")
    public void test005_AssertGettersForNewRecipe(Recipe recipe, String expectedName, int expectedID, int expectedServings, int expectedNumberIngredients, 
                double[] expectedNutrition) {
        assertEquals(expectedName, recipe.showName());
        assertEquals(expectedID, recipe.showIndex());
        assertEquals(expectedServings, recipe.weight());
        assertEquals(expectedNumberIngredients, recipe.showIngredientList().size());
        assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
    }

    @Test
    public void test006_AssertRecipeEditedCorrectly() {
        Recipe stubRecipe = new Recipe(stubDB, null, "Recipe Name1", 4);
        stubRecipe.edit("EditName", 10);
        
        assertEquals("EditName", stubRecipe.showName());
        assertEquals(10, stubRecipe.weight());
        assertEquals(0, stubRecipe.showIngredientList().size());        
    }

    @Nested
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class TestingIngredientsAddingandRemoving {

        //Create database mock and recipe object to use in these tests
        /*Database mockDB = mock();
        Recipe rec = new Recipe(mockDB, null, "Elderberries", 4);
        Food ingredient1 = stubs.stubFoodWithDisplayName;
        Food ingredient2 = stubs.stubFoodWithNullDisplayName;*/

        @ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithOneIngredientAdded")
        public void test007_AssertSingleIngredientAddedCorrectly2(Database mockDB, Recipe recipe, Food ingredient, int ingredientAmount, String expectedRecipeName, 
                    int expectedNumberIngredients, Set<Map.Entry<Integer, ArrayList<Object>>> expectedIngredientsList, double[] expectedNutrition, 
                    int expectedNumberFoodTypes, HashSet<String> expectedFoodTypes) {
            when(mockDB.getItemFromIndex(ingredient.showIndex())).thenReturn(ingredient);
            recipe.addNonTempIngredient(ingredient.showIndex(), ingredientAmount);

            assertEquals(expectedRecipeName, recipe.showName());
            assertEquals(expectedNumberIngredients, recipe.showIngredientList().size());
            assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
            assertEquals(expectedNumberFoodTypes, recipe.showFoodTypes().size());
            assertIterableEquals(expectedFoodTypes, recipe.showFoodTypes());
        }

        @ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithTwoIngredientsAdded")
        public void test007_AssertTwoIngredientsAddedCorrectly2(Database mockDB, Recipe recipe, Food ingredient1, int ingredient1Amount, Food ingredient2, int ingredient2Amount, String expectedRecipeName, 
                    int expectedNumberIngredients, Set<Map.Entry<Integer, ArrayList<Object>>> expectedIngredientsList, double[] expectedNutrition, 
                    int expectedNumberFoodTypes, HashSet<String> expectedFoodTypes) {
                     
            /* First test checks that if the same ingredient is added more than once, they are combined into one, not treated as separate ingredients.
             * Second test checks that different ingredients are treated separately.
             * Both check that the recipe's nutrition and recipe list is updated correctly.
             */
            when(mockDB.getItemFromIndex(ingredient1.showIndex())).thenReturn(ingredient1);
            when(mockDB.getItemFromIndex(ingredient2.showIndex())).thenReturn(ingredient2);
            recipe.addNonTempIngredient(ingredient1.showIndex(), ingredient1Amount);
            recipe.addNonTempIngredient(ingredient2.showIndex(), ingredient2Amount);

            assertEquals(expectedNumberIngredients, recipe.showIngredientList().size());
            assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
            assertEquals(expectedNumberFoodTypes, recipe.showFoodTypes().size());
            assertIterableEquals(expectedFoodTypes, recipe.showFoodTypes());

            
        }

        /*@Test
        public void test007_AssertSingleIngredientAddedCorrectly() {
            //Add food types to ingredient to check that they get added to recipe            
            ingredient1.addFoodType("Meat"); ingredient1.addFoodType("Chicken");
            
            when(mockDB.getItemFromIndex(ingredient1.showIndex())).thenReturn(ingredient1);

            //Exected nutrition values per serving of the recipe, based on the ingredient added
            double[] nutritionPerServing = {350/4.0, 12.3/4.0, 3/4.0, 24/4.0, 10/4.0, ((14.0/100)*100)/4.0, 37/4.0, 0.4/4.0};
            //Creating the expected ingredient list for the recipe after the ingredient is added, and converting to to a Set for testing
            HashMap<Integer, ArrayList<Object>> list1 = new HashMap<>(Map.of(ingredient1.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name1", 100.0))));
            Set<Map.Entry<Integer, ArrayList<Object>>> expectedIngredientsList = list1.entrySet();
            //Creating the list of recipes that the ingredient is in
            HashSet<Recipe> expectedIngredientRecipeList = new HashSet<>();
            Collections.addAll(expectedIngredientRecipeList, rec);
            //Creating the expected food types for the recipe after ingredient is added
            HashSet<String> expectedFoodTypes = new HashSet<>();
            Collections.addAll(expectedFoodTypes, "Meat", "Chicken");
            
            rec.addNonTempIngredient(ingredient1.showIndex(), 100);
            
            assertEquals("Elderberries", rec.showName());
            assertEquals(1, rec.showIngredientList().size());
            assertIterableEquals(expectedIngredientsList, rec.showIngredientList().entrySet());
            assertArrayEquals(nutritionPerServing, rec.showUnitNutrition());
            assertEquals(2, rec.showFoodTypes().size());
            assertIterableEquals(expectedFoodTypes, rec.showFoodTypes());

            assertEquals(ingredient1.showRecipes().size(), 1);
            assertIterableEquals(expectedIngredientRecipeList, ingredient1.showRecipes());
        }

        @Test
        public void test008_AssertTwoIngredientAddedCorrectly() {
            ingredient2.addFoodType("Pasta");

            when(mockDB.getItemFromIndex(ingredient1.showIndex())).thenReturn(ingredient1);
            when(mockDB.getItemFromIndex(ingredient2.showIndex())).thenReturn(ingredient2);

            //Exected nutrition values per serving of the recipe, based on the ingredient added
            double[] nutritionPerServing = {(350 + 350*1.5)/4.0, (12.3 + 12.3*1.5)/4.0, (3 + 3*1.5)/4.0, (24 + 24*1.5)/4.0, (10 + 10*1.5)/4.0, (((14.0/100)*100) + ((14.0/50)*75))/4.0, (37 + 37*1.5)/4.0, (0.4 + 0.4*1.5)/4.0};
            //Creating the expected ingredient list for the recipe after the ingredient is added, and converting to to a Set for testing
            HashMap<Integer, ArrayList<Object>> list1 = new HashMap<>(Map.of(ingredient1.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name1", 100.0)), ingredient2.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name2", 75.0))));
            Set<Map.Entry<Integer, ArrayList<Object>>> expectedIngredientsList = list1.entrySet();
            //Creating the expected food types for the recipe after ingredient is added
            HashSet<String> expectedFoodTypes = new HashSet<>();
            Collections.addAll(expectedFoodTypes, "Meat", "Chicken", "Pasta");

            rec.addNonTempIngredient(ingredient1.showIndex(), 100);
            rec.addNonTempIngredient(ingredient2.showIndex(), 75);
            
            assertEquals("Elderberries", rec.showName());
            assertEquals(2, rec.showIngredientList().size());
            assertIterableEquals(expectedIngredientsList, rec.showIngredientList().entrySet());
            assertArrayEquals(nutritionPerServing, rec.showUnitNutrition());
            assertEquals(3, rec.showFoodTypes().size());
            assertIterableEquals(expectedFoodTypes, rec.showFoodTypes());

            ingredient1.removeFoodType("Meat"); ingredient1.removeFoodType("Chicken");
            }

        /*@ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithIngredientsRemoved")
        public void test009_AssertIngredientRemovedCorrectly(Recipe recipe, Food ingredient, String expectedRecipeName, int expectedNumberIngredients, 
                        Set<Map.Entry<String,Double>> expectedIngredientsList, double[] expectedNutrition, HashSet<Recipe> expectedIngredientRecipeList, 
                        int expectedNumRecipesForIngredient) {
            recipe.removeIngredientPermanently(ingredient, ingredient.showIndex());
            assertEquals(expectedRecipeName, recipe.showName());
            assertEquals(expectedNumberIngredients, recipe.showIngredientList().size());
            //assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());

            assertEquals(ingredient.showRecipes().size(), expectedNumRecipesForIngredient);
            assertIterableEquals(expectedIngredientRecipeList, ingredient.showRecipes());

            //Reset stubs
            stubs.stubFoodWithDisplayName.edit("Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
            stubs.stubFoodWithNullDisplayName.edit("Full Name2", null, 50, "ml", 350, 12.3, 3, 24, 10, 14, 37, 0.4, null);
            stubs.stubRecipe1.edit("Recipe Name1", 4);
            stubs.stubRecipe2.edit("Recipe Name2", 1);

        }*/   
    }


    //-------------------------------------USER TESTS----------------------
    /*@ParameterizedTest
    @MethodSource("UnitTestMethods#providesUserObjects")
    public void test010_AssertSimpleGettersForNewUser(User user, String expectedName, String expectedGender, double expectedWeight, int expectedHeight, 
        String expectedDateOfBirth, String expectedGoal, double expectedRate, int expectedWater) {
        assertEquals(expectedName, user.showName());
        assertEquals(expectedGender, user.showGender());
        assertEquals(expectedWeight, user.showWeight());
        assertEquals(expectedHeight, user.showHeight());
        assertEquals(expectedDateOfBirth, user.showDOB());
        assertEquals(expectedGoal, user.showGoal());
        assertEquals(expectedRate, user.showRate());
        assertEquals(expectedWater, user.showWater());
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesUserAges")
    public void test011_AssertAgeCalculatedCorrectly(User user, LocalDate today, int expectedAge) throws Exception {
        assertEquals(expectedAge, user.calculateAge(today));
    }

    //Will probably get rid of these in favour of exception checking in the GUI
    @Test
    public static void test012_AssertNegativeAgeThrowsException() {
        User user = stubs.stubUserMaintain;
        LocalDate now = LocalDate.of(1995, 01, 01);

        assertThrows(NoNegativeException.class, () -> user.calculateAge(now));
        //reset stub
        try {
            stubs.stubUserMaintain = new User("Edmund", "M", 90.0, 200, "2000-01-01", "M", 0.0, 8);
        } catch (NoNegativeException e) {}
        
    }
        
    @Test
    public void test013_AssertNegativeParametersInConstructorThrowException() {
        assertThrows(NoNegativeException.class, () -> new User("Baldrick", "M", 90.0, -200, "2000-01-01", "M", 0.0, 8), "Weight cannot have negative value!");
        assertThrows(NoNegativeException.class, () -> new User("Baldrick", "M", 90.0, -200, "2000-01-01", "M", 0.0, 8), "Height cannot have negative value!");
        assertThrows(NoNegativeException.class, () -> new User("Baldrick", "M", 90.0, 200, "2000-01-01", "M", -1.0, 8), "Rate cannot have negative value!");
        assertThrows(NoNegativeException.class, () -> new User("Baldrick", "M", 90.0, 200, "2000-01-01", "M", 0.0, -8), "Water cannot have negative value!");
        assertThrows(NoNegativeException.class, () -> new User("Baldrick", "M", 90.0, 200, "2050-01-01", "M", 0.0, 8), "DOB can't be after the current date!");
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesUserCaloriesForWeightGoal")
    public void test014_AssertCaloriesForWeightGoalCalculatedCorrectly(User user, int expectedCals) throws Exception {
        assertEquals(expectedCals, user.caloriesForWeightGoal());
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesUserNutrition")
    public void test015_AssertNutritionCalculatedCorrectly(User user, double[] expectedNutrition) throws Exception {
        assertArrayEquals(expectedNutrition, user.showNutrition());
    }

    @Test
    public void test016_AssertSimpleUserSetters() {
        try {
            User stubUser = new User("Edmund", "M", 90.0, 200, "2000-01-01", "M", 0.0, 8);
            stubUser.changeName("Blackadder");
            stubUser.changeGender("F");
            stubUser.changeHeight(180);
            stubUser.changeDOB("1970-11-04");
            stubUser.setWaterGoal(15);

            assertEquals("Blackadder", stubUser.showName());
            assertEquals("F", stubUser.showGender());
            assertEquals(180, stubUser.showHeight());
            assertEquals("1970-11-04", stubUser.showDOB());
            assertEquals(15, stubUser.showWater());
        } catch (NoNegativeException n) {}
        
    }
    
    /*@Test
    public void test017_AssertWeightUpdatedCorrectly() {
        try {
            User stubUser = new User("Edmund", "M", 90.0, 200, "2000-01-01", "M", 0.0, 8);
            //Dates to use
            LocalDate firstDate = LocalDate.of(2023, 10, 01);
            LocalDate secondDate = LocalDate.of(2023, 9, 15);
            LocalDate thirdDate = LocalDate.of(2023, 10, 30);

            //Original values
            double OGWeight = 90.0;
            double[] OGNutrition = {2498, ((2498*0.25)/9), 30, ((2498*0.5)/4), 50, 33, ((2498*0.25)/4), 6};

            //Edit on 
        } catch (NoNegativeException n) {}
    }*/
    //Assert when weight updated, nutrition updated properly
    //Assert weight updated properly according to date (needs multiples)
    //Assert nutrition updates properly when goal updated
    

}
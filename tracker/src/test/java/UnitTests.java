import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
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
        assertEquals(false, food.isDeleted());
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesEditedFoodObjects")
    public void test002_AssertEditsApplyCorrectly(Food food, String newName, String newDisplayName, double newWeight, String newUnit, String newAmountString, 
        double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein,
        double salt, double[] expectedNutritionArray, double[] expectedUnitNutritionArray, String newBarcode, int expectedNumberFoodTypes,
        boolean expectedDeletionStatus) {

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
        assertEquals(expectedDeletionStatus, food.isDeleted());

        //Reset food stubs for use in other tests
        if (food == stubs.stubFoodWithDisplayName) {
            stubs.stubFoodWithDisplayName.edit("Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
            stubs.stubFoodWithDisplayName.setNotDeleted();
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
        assertEquals(expectedName, recipe.showDisplayName());
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
        public void test008_AssertTwoIngredientsAddedCorrectly2(Database mockDB, Recipe recipe, Food ingredient1, int ingredient1Amount, Food ingredient2, int ingredient2Amount, String expectedRecipeName, 
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

        @ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithIngredientsRemoved")
        public void test009_AssertIngredientsRemovedCorrectly(Recipe recipe, Food ingredient,  
                    int expectedNumberIngredients, Set<Map.Entry<Integer, ArrayList<Object>>> expectedIngredientsList, double[] expectedNutrition, 
                    int expectedNumberFoodTypes, HashSet<String> expectedFoodTypes) {
                     
            /* First and third tests check that ingredient removed correctly.
             * Second and fourth tests check that if we attempt to remove an ingredient not in the recipe, nothing happens.
             * All check that the recipe's nutrition and recipe list is updated correctly.
             */
            
            recipe.removeIngredientPermanently(ingredient, ingredient.showIndex());
            assertEquals(expectedNumberIngredients, recipe.showIngredientList().size());
            assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
            assertEquals(expectedNumberFoodTypes, recipe.showFoodTypes().size());
            assertIterableEquals(expectedFoodTypes, recipe.showFoodTypes()); 
            
            //Reset ingredient food types
            if (ingredient == stubs.stubFoodWithDisplayName && recipe.showIngredientList().size() == 0) {
                stubs.stubFoodWithDisplayName.removeFoodType("Meat");
                stubs.stubFoodWithDisplayName.removeFoodType("Chicken");
                stubs.stubFoodWithNullDisplayName.removeFoodType("Pasta");
            }
        }
    }


    //-------------------------------------USER TESTS----------------------
    @ParameterizedTest
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
    public void test015_AssertNutritionCalculatedCorrectly(User user, double[] expectedNutrition, LocalDate date) throws Exception {
        /*
         * First three tests assert that nutrition is expected for different users
         * Fourth test checks that when age changes, the expected nutrition also changes
         * - so that as users get older, their values update
         */
        
        user.updateNutrition(date);
        assertArrayEquals(expectedNutrition, user.showNutrition());
    }

    @Test
    public void test016_AssertSimpleUserSetters() {
        try {
            User stubUser = new User("Edmund", "Male", 90.0, 200, "2000-01-01", "M", 0.0, 8);
            stubUser.changeName("Blackadder");
            stubUser.changeGender("Female");
            stubUser.changeHeight(180);
            stubUser.changeDOB("1970-11-04");
            stubUser.setWaterGoal(15);

            assertEquals("Blackadder", stubUser.showName());
            assertEquals("Female", stubUser.showGender());
            assertEquals(180, stubUser.showHeight());
            assertEquals("1970-11-04", stubUser.showDOB());
            assertEquals(15, stubUser.showWater());
        } catch (NoNegativeException n) {}
        
    }
    
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesUserWeightUpdates")
    public void test017_AssertWeightUpdatedCorrectly(User user, LocalDate date, HashSet<LocalDate> diary, double weight, double expectedWeight, double[] expectedNutrition) {
        /*
         * First test asserts that "standard" update works as expected
         * Second test checks that if a weight it added on a previous date, the user's current values for weight and 
         * nutrition are not changed
         * Third tests checks standard update again
         */

        Diary mockDiary = mock();
        Database mockDB = mock();
        Day mockDay = mock();

        user.setTesting(mockDiary, mockDB);
        when(mockDiary.addDay(date)).thenReturn(mockDay);
        when(mockDiary.showDays()).thenReturn(diary);
        
        for (LocalDate d: diary) { //Make sure we create a mock Day object for each entry in the diary
            when(mockDiary.getDay(d)).thenReturn(mockDay);
        }
        when(mockDay.showWeight()).thenReturn(1.0);

        user.updateWeight(date, weight);
        assertEquals(expectedWeight, user.showWeight());
        assertArrayEquals(expectedNutrition, user.showNutrition());
    }

    //Assert nutrition updates properly when goal updated
    

}
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.maven.plugin.surefire.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;

import app.db.*;

public class UnitTests {

    Database stubDB = new Database("DB");
    Food stubFoodWithDisplayName = new Food(stubDB, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    Food stubFoodWithNullDisplayName = new Food(stubDB, "Full Name2", null, 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");

    //-------------------FOOD TESTS------------------
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjects")
    public void test001_AssertGettersForNewFoodObject(Food food, String expectedName, String expectedDisplayName, String expectedAmountString, double[] expectedNutritionArray, double[] expectedUnitNutritionArray, String expectedBarcode, int expectedNumberFoodTypes) {
        assertEquals(expectedName, food.showName());
        assertEquals(expectedDisplayName, food.showDisplayName());
        assertEquals(expectedAmountString, food.showWeight());
        assertArrayEquals(expectedNutritionArray, food.showNutrition());
        assertArrayEquals(expectedUnitNutritionArray, food.showUnitNutrition());
        assertEquals(expectedBarcode, food.showBarcode());
        assertEquals(expectedNumberFoodTypes, food.showFoodTypes().size());
    }

    @Test
    public void test002_AssertEditsApplyCorrectly() {
        stubFoodWithDisplayName.edit("New Name", "New Display Name", 0, "ml", 0, 0, 0, 0, 0, 0, 0, 0, "New Barcode");
        double[] nutrition = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        assertEquals("New Name", stubFoodWithDisplayName.showName());
        assertEquals("New Display Name", stubFoodWithDisplayName.showDisplayName());
        assertEquals("New Barcode", stubFoodWithDisplayName.showBarcode());
        assertEquals("0.0 ml", stubFoodWithDisplayName.showWeight());
        assertEquals(0.0, stubFoodWithDisplayName.showAmount());
        assertEquals("ml", stubFoodWithDisplayName.showUnit());
        assertArrayEquals(nutrition, stubFoodWithDisplayName.showNutrition());

        stubFoodWithDisplayName = new Food(stubDB, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    }


    @Nested
    @DisplayName("Adding and Removing Food Types")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class TestingFoodTypes {
        @Test
        public void test003_AssertFoodTypeAddedCorrectly() {
            ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Food Type"));

            stubFoodWithDisplayName.addFoodType("Food Type");
            ArrayList<String> actual = stubFoodWithDisplayName.showFoodTypes();
            assertEquals(1, actual.size());
            assertIterableEquals(expected, actual);
        }

        @Test
        public void test004_AssertFoodTypeRemovedCorrectly() {
            stubFoodWithDisplayName.removeFoodType("Food Type");
            ArrayList<String> actual = stubFoodWithDisplayName.showFoodTypes();
            ArrayList<String> expected = new ArrayList<String>();

            assertEquals(0, actual.size());
            assertIterableEquals(expected, actual);
        }
    }


    //----------------------RECIPE TESTS--------------------
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesNewRecipeObjects")
    public void test005_AssertGettersForNewRecipe(Recipe recipe, String expectedName, int expectedServings, int expectedNumberIngredients) {
        assertEquals(expectedName, recipe.showName());
        assertEquals(expectedServings, recipe.weight());
        assertEquals(expectedNumberIngredients, recipe.numberIngredients());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class TestingIngredientsAddingandRemoving {

        @ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithOneIngredientAdded")
        public void test006_AssertSingleIngredientAddedCOrrectly(Recipe recipe, Food ingredient, int ingredientAmount, String expectedRecipeName, int expectedNumberIngredients, Set<Map.Entry<String,Double>> expectedIngredientsList, double[] expectedNutrition) {
            Recipe rec = recipe;
            recipe.addFoodIngredient(ingredient, ingredient.showName(), ingredientAmount);
            
            assertEquals(expectedRecipeName, recipe.showName());
            assertEquals(expectedNumberIngredients, recipe.numberIngredients());
            assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
        }

        @ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithTwoIngredientsAdded")
        public void test007_AssertSecondIngredientAddedCorrectly(Recipe recipe, Food ingredient1, Food ingredient2, int ingredientAmount, String expectedRecipeName, int expectedNumberIngredients, Set<Map.Entry<String,Double>> expectedIngredientsList, double[] expectedNutrition) {
            Recipe rec = recipe;
            recipe.addFoodIngredient(ingredient2, ingredient2.showName(), ingredientAmount);
            assertEquals(expectedRecipeName, recipe.showName());
            assertEquals(expectedNumberIngredients, recipe.numberIngredients());
            assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
        }

        @ParameterizedTest
        @MethodSource("UnitTestMethods#providesRecipeObjectsWithIngredientsRemoved")
        public void test008_AssertIngredientRemovedCorrectly(Recipe recipe, Food ingredient, String expectedRecipeName, int expectedNumberIngredients, Set<Map.Entry<String,Double>> expectedIngredientsList, double[] expectedNutrition) {
            recipe.removeIngredient1(ingredient, ingredient.showName());
            assertEquals(expectedRecipeName, recipe.showName());
            assertEquals(expectedNumberIngredients, recipe.numberIngredients());
            assertIterableEquals(expectedIngredientsList, recipe.showIngredientList().entrySet());
            assertArrayEquals(expectedNutrition, recipe.showUnitNutrition());
        }
    }
    

}
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
import org.apache.maven.plugin.surefire.*;
import java.util.ArrayList;
import java.util.Arrays;

import app.db.*;

public class UnitTests {

    Database stubDB = new Database("DB");
    Food stubFoodWithDisplayName = new Food(stubDB, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    Food stubFoodWithNullDisplayName = new Food(stubDB, "Full Name2", null, 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");

    //-------------------FOOD TESTS------------------
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjects")
    public void test001_AssertGettersForNewFoodObject(Food food, String expectedName, String expectedDisplayName, String expectedAmountString, double[] expectedNutritionArray, double[] expectedUnitNutritionArray, String expectedBarcode) {
        assertEquals(expectedName, food.showName());
        assertEquals(expectedDisplayName, food.showDisplayName());
        assertEquals(expectedAmountString, food.showWeight());
        assertArrayEquals(expectedNutritionArray, food.showNutrition());
        assertArrayEquals(expectedUnitNutritionArray, food.showUnitNutrition());
        assertEquals(expectedBarcode, food.showBarcode());
    }
    
    /*@ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithNames")
    public void test001_AssertCorrectFoodNameReturned(Food food, String expectedName) {
        String actualName = food.showName();
        assertEquals(expectedName, actualName);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithDisplayNames")
    public void test002_AssertCorrectDisplayNameReturned(Food food, String expectedName) {
        String displayName = food.showDisplayName();
        assertEquals(expectedName, displayName);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithBarcodes")
    public void test003_AssertCorrectBarcodeReturned(Food food, String expected) {
        String barcode = food.showBarcode();
        assertEquals(expected, barcode);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithAmountString")
    public void test004_AsertCorrectAmountString(Food food, String expected) {
        String strng = food.showWeight();
        assertEquals(expected, strng);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithAmounts")
    public void test005_AssertCorrectAmountReturned(Food food, double expected) {
        double weight = food.showAmount();
        assertEquals(expected, weight);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithUnits")
    public void test006_AssertCorrectUnitReturned(Food food, String expected) {
        String unit = food.showUnit();
        assertEquals(expected, unit);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithNutrition")
    public void test007_AssertCorrectNutritionArrayReturned(Food food, double[] expected) {
        double[] nutrition = food.showNutrition();
        assertArrayEquals(expected, nutrition);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithUnitNutrition")
    public void test012_AssertCorrectUnitNutritionArrayReturned(Food food, double[] expected) {
        double[] nutrition = food.showUnitNutrition();
        assertArrayEquals(expected, nutrition);
    }*/

    @Test
    public void test008_AssertEditsApplyCorrectly() {
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

    @Test
    public void test009_ShowFoodTypes() {
        ArrayList<String> list = stubFoodWithDisplayName.showFoodTypes();
        assertEquals(0, list.size());
    }

    @Nested
    @DisplayName("Adding and Removing Food Types")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class TestingFoodTypes {
        @Test
        public void test010_AssertFoodTypeAddedCorrectly() {
            ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Food Type"));

            stubFoodWithDisplayName.addFoodType("Food Type");
            ArrayList<String> actual = stubFoodWithDisplayName.showFoodTypes();
            assertEquals(1, actual.size());
            assertIterableEquals(expected, actual);
        }

        @Test
        public void test011_AssertFoodTypeRemovedCorrectly() {
            stubFoodWithDisplayName.removeFoodType("Food Type");
            ArrayList<String> actual = stubFoodWithDisplayName.showFoodTypes();
            ArrayList<String> expected = new ArrayList<String>();

            assertEquals(0, actual.size());
            assertIterableEquals(expected, actual);
        }
    }


    //----------------------RECIPE TESTS--------------------
    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesRecipeObjectsWithNames")
    public void test012_AssertCorrectRecipeNameReturned(Recipe recipe, String expectedName) {
        String actualName = recipe.showName();
        assertEquals(expectedName, actualName);
    }

}
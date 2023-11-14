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
    @MethodSource("UnitTestMethods#providesFoodObjectsWithNames")
    public void test001_FoodName(Food food, String expectedName) {
        String actualName = food.showName();
        assertEquals(expectedName, actualName);
    }

    @ParameterizedTest
    @MethodSource("UnitTestMethods#providesFoodObjectsWithDisplayNames")
    public void test002_DisplayName(Food food, String expectedName) {
        String displayName = food.showDisplayName();
        assertEquals(expectedName, displayName);
    }

    @Test
    public void test003_Barcode() {
        String barcode = stubFoodWithDisplayName.showBarcode();
        assertEquals("barcode", barcode);
    }

    @Test
    public void test004_AmountString() {
        String strng = stubFoodWithDisplayName.showWeight();
        assertEquals("100.0 g", strng);
    }

    @Test
    public void test005_Amount() {
        double weight = stubFoodWithDisplayName.showAmount();
        assertEquals(100.0, weight);
    }

    @Test
    public void test006_Unit() {
        String unit = stubFoodWithDisplayName.showUnit();
        assertEquals("g", unit);
    }

    @Test
    public void test007_Nutrition() {
        double[] nutrition = {350, 12.3, 3, 24, 10, 14, 37, 0.4};
        assertArrayEquals(nutrition, stubFoodWithDisplayName.showNutrition());
    }

    @Test
    public void test008_Edit() {
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
        public void test010_AddFoodType() {
            ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Food Type"));

            stubFoodWithDisplayName.addFoodType("Food Type");
            ArrayList<String> actual = stubFoodWithDisplayName.showFoodTypes();
            assertEquals(1, actual.size());
            assertIterableEquals(expected, actual);
        }

        @Test
        public void test011_RemoveFoodType() {
            stubFoodWithDisplayName.removeFoodType("Food Type");
            ArrayList<String> actual = stubFoodWithDisplayName.showFoodTypes();

            assertEquals(0, actual.size());
        }
    }

}
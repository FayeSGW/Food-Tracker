import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import app.db.*;

class UnitTestMethods {

    static Database stubDB = new Database("DB");
    static Food stubFoodWithDisplayName = new Food(stubDB, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    static Food stubFoodWithNullDisplayName = new Food(stubDB, "Full Name2", null, 50, "ml", 350, 12.3, 3, 24, 10, 14, 37, 0.4, null);

    static Recipe stubRecipe1 = new Recipe(stubDB, "Recipe Name1", 4);
    static Recipe stubRecipe2 = new Recipe(stubDB, "Recipe Name2", 1);

    //------------------Food Arguments-------------------
    protected static Stream<Arguments> providesFoodObjects() {
        //test_001
        double[] nutrition = {350, 12.3, 3, 24, 10, 14, 37, 0.4};
        double[] unit_food1 = {350/100.0, 12.3/100, 3/100.0, 24/100.0, 10/100.0, 14/100.0, 37/100.0, 0.4/100};
        double[] unit_food2 = {350/50.0, 12.3/50, 3/50.0, 24/50.0, 10/50.0, 14/50.0, 37/50.0, 0.4/50};
        
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "Full Name1", "Display Name", "100.0 g", nutrition, unit_food1, "barcode"),
        Arguments.of(stubFoodWithNullDisplayName, "Full Name2", "Full Name2", "50.0 ml", nutrition, unit_food2, null)
        );
    }
    
    /*protected static Stream<Arguments> providesFoodObjectsWithNames() {
        //test_001
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "Full Name1"),
        Arguments.of(stubFoodWithNullDisplayName, "Full Name2")
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithDisplayNames() {
        //test_002
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "Display Name"),
        Arguments.of(stubFoodWithNullDisplayName, "Full Name2")
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithBarcodes() {
        //test_003
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "barcode"),
        Arguments.of(stubFoodWithNullDisplayName, null)
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithAmountString() {
        //test_004
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "100.0 g"),
        Arguments.of(stubFoodWithNullDisplayName, "50.0 ml")
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithAmounts() {
        //test_005
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, 100.0),
        Arguments.of(stubFoodWithNullDisplayName, 50.0)
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithUnits() {
        //test_006
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "g"),
        Arguments.of(stubFoodWithNullDisplayName, "ml")
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithNutrition() {
        //test_007
        double[] array = {350, 12.3, 3, 24, 10, 14, 37, 0.4};
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, array),
        Arguments.of(stubFoodWithNullDisplayName, array)
        );
    }

    protected static Stream<Arguments> providesFoodObjectsWithUnitNutrition() {
        //test_012
        double[] unit_food1 = {350/100.0, 12.3/100, 3/100.0, 24/100.0, 10/100.0, 14/100.0, 37/100.0, 0.4/100};
        double[] unit_food2 = {350/50.0, 12.3/50, 3/50.0, 24/50.0, 10/50.0, 14/50.0, 37/50.0, 0.4/50};
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, unit_food1),
        Arguments.of(stubFoodWithNullDisplayName, unit_food2)
        );
    }*/

    //---------------------Recipe Arguments-----------------
    protected static Stream<Arguments> providesRecipeObjectsWithNames() {
        //test_012
        return Stream.of(
        Arguments.of(stubRecipe1, "Recipe Name1"),
        Arguments.of(stubRecipe2, "Recipe Name2")
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithNumberOfIngredients() {
        //test_012
        return Stream.of(
        Arguments.of(stubRecipe1, 0),
        Arguments.of(stubRecipe2, 0)
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithIngredients() {
        //test_012
        return Stream.of(
        Arguments.of(stubRecipe1, stubFoodWithDisplayName, "Recipe Name1"),
        Arguments.of(stubRecipe2, "Recipe Name2")
        );
    }

}
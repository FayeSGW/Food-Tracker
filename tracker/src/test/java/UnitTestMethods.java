import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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
        Arguments.of(stubFoodWithDisplayName, "Full Name1", "Display Name", "100.0 g", nutrition, unit_food1, "barcode", 0),
        Arguments.of(stubFoodWithNullDisplayName, "Full Name2", "Full Name2", "50.0 ml", nutrition, unit_food2, null, 0)
        );
    }

    protected static Stream<Arguments> providesFoodEditWithNonNumbers() {
        //test_001
        
        return Stream.of(
        Arguments.of(stubFoodWithDisplayName, "one hundred", 350, 12.3, 3, 24, 10, 14, 37, 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, "350", 12.3, 3, 24, 10, 14, 37, 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, 350, "a", 3, 24, 10, 14, 37, 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, 350, 12.3, true, 24, 10, 14, 37, 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, 350, 12.3, 3, 24, false, 14, 37, 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, 350, 12.3, 3, 24, 10, "xx", 37, 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, 350, 12.3, 3, 24, 10, 14, "37", 0.4),
        Arguments.of(stubFoodWithDisplayName, 100, 350, 12.3, 3, 24, 10, 14, 37, "*")

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
    protected static Stream<Arguments> providesNewRecipeObjects() {
        //test_012
        return Stream.of(
        Arguments.of(stubRecipe1, "Recipe Name1", 4, 0),
        Arguments.of(stubRecipe2, "Recipe Name2", 1, 0)
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithOneIngredientAdded() {
        //test_012

        //Recipe 1
        //100 g of ingredient, 4 servings
        double[] ingredient1_perServing = {350/4.0, 12.3/4.0, 3/4.0, 24/4.0, 10/4.0, ((14.0/100)*100)/4.0, 37/4.0, 0.4/4.0};
        HashMap<String, Double> list1 = new HashMap<>(Map.of("Full Name1", 100.0));
        Set<Map.Entry<String,Double>> list1Set = list1.entrySet();

        //Recipe 2
        //100 g of ingredient, 1 serving
        double[] ingredient2_perServing = {350*2.0, 12.3*2.0, 3*2.0, 24*2.0, 10*2.0, ((14.0/50)*100), 37*2.0, 0.4*2.0};
        HashMap<String, Double> list2 = new HashMap<>(Map.of("Full Name2", 100.0));
        Set<Map.Entry<String,Double>> list2Set = list2.entrySet();

        //Recipe Object - Ingredient Object - Expected Recipe Name - Expected Number of Ingredients - Expected Ingredients List (Keys) - Expected Ingredients List (Values) - Expected Nutrition
        return Stream.of(
        Arguments.of(stubRecipe1, stubFoodWithDisplayName, 100, "Recipe Name1", 1, list1Set, ingredient1_perServing),
        Arguments.of(stubRecipe2, stubFoodWithNullDisplayName, 100,  "Recipe Name2", 1, list2Set, ingredient2_perServing)
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithTwoIngredientsAdded() {
        //test_012

        //Recipe3
        //100 g of ingredient 1, 75 g of ingredient 2, 4 servings
        double[] nutrition_perServing = {(350 + 350*1.5)/4.0, (12.3 + 12.3*1.5)/4.0, (3 + 3*1.5)/4.0, (24 + 24*1.5)/4.0, (10 + 10*1.5)/4.0, (((14.0/100)*100) + ((14.0/50)*75))/4.0, (37 + 37*1.5)/4.0, (0.4 + 0.4*1.5)/4.0};
        HashMap<String, Double> list3 = new HashMap<>(Map.of("Full Name1", 100.0, "Full Name2", 75.0));
        Set<Map.Entry<String,Double>> list3Set = list3.entrySet();

        //Recipe Object - Ingredient Object - Ingredient Object - Expected Recipe Name - Expected Number of Ingredients - Expected Ingredients List - Expected Nutrition
        return Stream.of(
        Arguments.of(stubRecipe1, stubFoodWithDisplayName, stubFoodWithNullDisplayName, 75, "Recipe Name1", 2, list3Set, nutrition_perServing)
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithIngredientsRemoved() {
        //test_012

        //Recipe3
        //75 g of ingredient 2, 4 servings
        double[] nutrition_perServing = {(350*1.5)/4.0, (12.3*1.5)/4.0, (3*1.5)/4.0, (24*1.5)/4.0, (10*1.5)/4.0, 5.250000000000002, (37*1.5)/4.0, ((0.4/50)*75)/4.0};
        HashMap<String, Double> list3 = new HashMap<>(Map.of("Full Name2", 75.0));
        Set<Map.Entry<String,Double>> list3Set = list3.entrySet();

        //Recipe 1
        //No ingredients
        double[] nutrition_perServing2 = {0.0, 0.0,0.0,0.0,0.0,8.881784197001252E-16,0.0,0.0};
        HashMap<String, Double> list1 = new HashMap<>();
        Set<Map.Entry<String,Double>> list1Set = list1.entrySet();

        //Recipe Object - Expected Recipe Name - Expected Number of Ingredients - Expected Ingredients List - Expected Nutrition
        return Stream.of(
        Arguments.of(stubRecipe1, stubFoodWithDisplayName, "Recipe Name1", 1, list3Set, nutrition_perServing),
        Arguments.of(stubRecipe1, stubFoodWithNullDisplayName, "Recipe Name1", 0, list1Set, nutrition_perServing2)
        );
    }

}
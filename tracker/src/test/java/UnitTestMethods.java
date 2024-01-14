
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import org.junit.jupiter.params.provider.Arguments;

import app.db.*;
import app.diary.*;
import exceptions.NoNegativeException;

class UnitTestMethods {

    static class Stubs {
        public static Food stubFoodWithDisplayName = null;
        public static Food stubFoodWithNullDisplayName = null;
        public static Recipe stubRecipe1 = null;
        public static Recipe stubRecipe2 = null;
        public static User stubUserMaintain = null;
        public static User stubUserLose = null;
        public static User stubUserGain = null;

        static {
            try {
                Database mockDB = new Database("DB");
                stubFoodWithDisplayName = new Food(mockDB, null, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
                stubFoodWithNullDisplayName = new Food(mockDB, null, "Full Name2", null, 50, "ml", 350, 12.3, 3, 24, 10, 14, 37, 0.4, null);

                stubRecipe1 = new Recipe(mockDB, null, "Recipe Name1", 4);
                stubRecipe2 = new Recipe(mockDB, null, "Recipe Name2", 1);

                stubUserMaintain = new User("Edmund", "M", 90.0, 200, "2000-01-01", "M", 0.0, 8);
                stubUserLose = new User("Elizabeth", "F", 60.0, 160, "1990-12-12", "L", 0.5, 10);
                stubUserGain = new User("Percy", "M", 65.5, 200, "1995-06-07", "G", 1.0, 7);
            } catch (NoNegativeException e) {}
        }
        
    }

    protected static Stubs getStubs() {
        Stubs stubs = new Stubs();
        return stubs;
    }

    //------------------Food Arguments-------------------
    protected static Stream<Arguments> providesFoodObjects() {
        //test_001
        double[] nutrition = {350, 12.3, 3, 24, 10, 14, 37, 0.4};
        double[] unit_food1 = {350/100.0, 12.3/100, 3/100.0, 24/100.0, 10/100.0, 14/100.0, 37/100.0, 0.4/100};
        double[] unit_food2 = {350/50.0, 12.3/50, 3/50.0, 24/50.0, 10/50.0, 14/50.0, 37/50.0, 0.4/50};
        
        return Stream.of(
        Arguments.of(Stubs.stubFoodWithDisplayName, "Full Name1", "Display Name", 1, "100.0 g", nutrition, 
            unit_food1, "barcode", 0),
        Arguments.of(Stubs.stubFoodWithNullDisplayName, "Full Name2", "Full Name2", 2, "50.0 ml", nutrition, 
            unit_food2, null, 0)
        );
    }
    
    protected static Stream<Arguments> providesEditedFoodObjects() {
        //test_002
        double[] nutrition_food1 = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        double[] unit_food1 = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        double[] nutrition_food2 = {100.0, 50.0, 1.0, 0.0, 100.0, 50.0, 1.0, 0.0};
        double[] unit_food2 = {1.0, 0.5, 0.01, 0.0, 1.0, 0.5, 0.01, 0.0};
        
        return Stream.of(
        Arguments.of(Stubs.stubFoodWithDisplayName, "New Name1", "New Display Name", 250.0, "x", "250.0 x", 
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, nutrition_food1, unit_food1, "barcode", 0),
        Arguments.of(Stubs.stubFoodWithNullDisplayName, "New Name2", null, 100.0, "g", "100.0 g", 
            100.0, 50.0, 1.0, 0.0, 100.0, 50.0, 1.0, 0.0, nutrition_food2, unit_food2, null, 0)
        );
    }


    //---------------------Recipe Arguments-----------------
    protected static Stream<Arguments> providesNewRecipeObjects() {
        double[] expectedNutrition = {0, 0, 0, 0, 0, 0, 0, 0};
        HashMap<String, Double> ingList = new HashMap<>();
        Set<Map.Entry<String,Double>> expectedIngredientList = ingList.entrySet();
        
        return Stream.of(
        Arguments.of(Stubs.stubRecipe1, "Recipe Name1", 3, 4, 0, expectedNutrition, expectedIngredientList),
        Arguments.of(Stubs.stubRecipe2, "Recipe Name2", 4, 1, 0, expectedNutrition, expectedIngredientList)
        );
    }


    protected static Stream<Arguments> providesRecipeObjectsWithOneIngredientAdded() {
        //test_012
        Database mockDB = mock();
        Recipe rec1 = new Recipe(mockDB, null, "Elderberries", 4);
        Recipe rec2 = new Recipe(mockDB, null, "Hamster", 1);
        Food ingredient1 = Stubs.stubFoodWithDisplayName;
        ingredient1.addFoodType("Meat"); ingredient1.addFoodType("Chicken");
        Food ingredient2 = Stubs.stubFoodWithNullDisplayName;
        ingredient2.addFoodType("Pasta");

        //Recipe 1
        //100 g of ingredient1, 4 servings
        double[] ingredient1PerServing = {350/4.0, 12.3/4.0, 3/4.0, 24/4.0, 10/4.0, ((14.0/100)*100)/4.0, 37/4.0, 0.4/4.0};
        HashMap<Integer, ArrayList<Object>> list1 = new HashMap<>(Map.of(Stubs.stubFoodWithDisplayName.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name1", 100.0))));
        Set<Map.Entry<Integer, ArrayList<Object>>> list1Set = list1.entrySet();
        HashSet<Recipe> ingredient1RecipeList = new HashSet<>();
        Collections.addAll(ingredient1RecipeList, Stubs.stubRecipe1);
        HashSet<String> expectedFoodTypes1 = new HashSet<>();
        Collections.addAll(expectedFoodTypes1, "Meat", "Chicken");

        //Recipe 2
        //100 g of ingredient2, 1 serving
        double[] ingredient2PerServing = {350*1.5, 12.3*1.5, 3*1.5, 24*1.5, 10*1.5, ((14.0/50)*75), 37*1.5, ((0.4/50)*75)};
        HashMap<Integer, ArrayList<Object>> list2 = new HashMap<>(Map.of(Stubs.stubFoodWithNullDisplayName.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name2", 75.0))));
        Set<Map.Entry<Integer, ArrayList<Object>>> list2Set = list2.entrySet();  
        HashSet<Recipe> ingredient2RecipeList = new HashSet<>();
        Collections.addAll(ingredient2RecipeList, Stubs.stubRecipe2);
        HashSet<String> expectedFoodTypes2 = new HashSet<>();
        Collections.addAll(expectedFoodTypes2, "Pasta");

        //Recipe Object - Ingredient Object - Expected Recipe Name - Expected Number of Ingredients - Expected Ingredients List (Keys) - Expected Ingredients List (Values) - Expected Nutrition
        return Stream.of(
        Arguments.of(mockDB, rec1, ingredient1, 100, "Elderberries", 1, list1Set, ingredient1PerServing, 2, expectedFoodTypes1),
        Arguments.of(mockDB, rec2, ingredient2, 75,  "Hamster", 1, list2Set, ingredient2PerServing, 1, expectedFoodTypes2)
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithTwoIngredientsAdded() {
        Database mockDB = mock();
        Recipe rec1 = new Recipe(mockDB, null, "Elderberries", 4);
        Recipe rec2 = new Recipe(mockDB, null, "Hamster", 1);

        Food ingredient1 = Stubs.stubFoodWithDisplayName;
        Food ingredient2 = Stubs.stubFoodWithNullDisplayName;
        
        //Recipe1
        //100 g of ingredient 1, 75 g of ingredient 1, 4 servings
        double[] nutritionPerServing1 = {(350 + 350*0.75)/4.0, (12.3 + 12.3*0.75)/4.0, (3 + 3*0.75)/4.0, (24 + 24*0.75)/4.0, (10 + 10*0.75)/4.0, (((14.0/100)*100) + ((14.0/100)*75))/4.0, (37 + 37*0.75)/4.0, (0.7)/4.0};
        HashMap<Integer, ArrayList<Object>> list1 = new HashMap<>(Map.of(ingredient1.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name1", 175.0))));        
        Set<Map.Entry<Integer, ArrayList<Object>>> list1Set = list1.entrySet();
        HashSet<String> expectedFoodTypes1 = new HashSet<>();
        Collections.addAll(expectedFoodTypes1, "Meat", "Chicken");

        //Recipe2
        //75g of ingredient 2, 100g of ingredient 1, 1 serving
        double[] nutritionPerServing2 = {(350 + 350*1.5), (12.3 + 12.3*1.5), (3 + 3*1.5), (24 + 24*1.5), (10 + 10*1.5), 35.00000000000001, (37 + 37*1.5), (0.4 + (0.4/50)*75)};
        HashMap<Integer, ArrayList<Object>> list2 = new HashMap<>(Map.of(ingredient2.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name2", 75.0)), ingredient1.showIndex(), new ArrayList<Object>(Arrays.asList("Full Name1", 100.0))));
        Set<Map.Entry<Integer, ArrayList<Object>>> list2Set = list2.entrySet();  
        HashSet<Recipe> ingredient2RecipeList = new HashSet<>();
        Collections.addAll(ingredient2RecipeList, rec2, rec1);
        HashSet<String> expectedFoodTypes2 = new HashSet<>();
        Collections.addAll(expectedFoodTypes2, "Pasta", "Meat", "Chicken");

        return Stream.of(
        Arguments.of(mockDB, rec1, ingredient1, 100, ingredient1, 75, "Elderberries", 1, list1Set, nutritionPerServing1, 2, expectedFoodTypes1),
        Arguments.of(mockDB, rec2, ingredient2, 75, ingredient1, 100, "Hamster", 2, list2Set, nutritionPerServing2, 3, expectedFoodTypes2)
        );
    }

    protected static Stream<Arguments> providesRecipeObjectsWithIngredientsRemoved() {
        //test_012

        //Recipe3
        //75 g of ingredient 2, 4 servings
        double[] nutrition_perServing = {(350*1.5)/4.0, (12.3*1.5)/4.0, (3*1.5)/4.0, (24*1.5)/4.0, (10*1.5)/4.0, 5.250000000000002, (37*1.5)/4.0, ((0.4/50)*75)/4.0};
        HashMap<String, Double> list3 = new HashMap<>(Map.of("Full Name2", 75.0));
        Set<Map.Entry<String,Double>> list3Set = list3.entrySet();
        HashSet<Recipe> ingredient1RecipeList = new HashSet<>();

        //Recipe 1
        //No ingredients
        double[] nutrition_perServing2 = {0.0, 0.0,0.0,0.0,0.0,8.881784197001252E-16,0.0,0.0};
        HashMap<String, Double> list1 = new HashMap<>();
        Set<Map.Entry<String,Double>> list1Set = list1.entrySet();
        HashSet<Recipe> ingredient2RecipeList = new HashSet<>();
        Collections.addAll(ingredient2RecipeList, Stubs.stubRecipe2);

        //Recipe Object - Expected Recipe Name - Expected Number of Ingredients - Expected Ingredients List - Expected Nutrition
        return Stream.of(
        Arguments.of(Stubs.stubRecipe1, Stubs.stubFoodWithDisplayName, "Recipe Name1", 1, list3Set, nutrition_perServing, ingredient1RecipeList, 0),
        Arguments.of(Stubs.stubRecipe1, Stubs.stubFoodWithNullDisplayName, "Recipe Name1", 0, list1Set, nutrition_perServing2, ingredient2RecipeList, 1)
        );
    }

    //--------------------User Arguments----------------------
    protected static Stream<Arguments> providesUserObjects() {
        return Stream.of(
        Arguments.of(Stubs.stubUserMaintain, "Edmund", "M", 90.0, 200, "2000-01-01", "M", 0.0, 8),
        Arguments.of(Stubs.stubUserLose, "Elizabeth", "F", 60.0, 160, "1990-12-12", "L", 0.5, 10),
        Arguments.of(Stubs.stubUserGain, "Percy", "M", 65.5, 200, "1995-06-07", "G", 1.0, 7)
        );
    }

    protected static Stream<Arguments> providesUserAges() {
        return Stream.of(
        Arguments.of(Stubs.stubUserMaintain, LocalDate.of(2000, 06, 06), 0),
        Arguments.of(Stubs.stubUserMaintain, LocalDate.of(2010, 04, 01), 10),
        Arguments.of(Stubs.stubUserMaintain, LocalDate.of(2033, 06, 06), 33)
        );
    }

    protected static Stream<Arguments> providesUserCaloriesForWeightGoal() {
        return Stream.of(
        Arguments.of(Stubs.stubUserMaintain, 0),
        Arguments.of(Stubs.stubUserLose, -550),
        Arguments.of(Stubs.stubUserGain, 1100)
        );
    }

    protected static Stream<Arguments> providesUserNutrition() {
        double[] user1Nutrition = {2498, ((2498*0.25)/9), 30, ((2498*0.5)/4), 50, 33, ((2498*0.25)/4), 6};
        double[] user2Nutrition = {1028, ((1028*0.25)/9), 20, ((1028*0.5)/4), 50, 27, ((1028*0.25)/4), 6};
        double[] user3Nutrition = {3222, ((3222*0.25)/9), 30, ((3222*0.5)/4), 50, 33, ((3222*0.25)/4), 6};

        return Stream.of(
        Arguments.of(Stubs.stubUserMaintain, user1Nutrition),
        Arguments.of(Stubs.stubUserLose, user2Nutrition),
        Arguments.of(Stubs.stubUserGain, user3Nutrition)
        );
    }

}
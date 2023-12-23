import app.db.*;

class UnitTestStubs {
    static Database stubDB = new Database("DB");
    public static Food stubFoodWithDisplayName = new Food(stubDB, "Full Name1", "Display Name", 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    static Food stubFoodWithNullDisplayName = new Food(stubDB, "Full Name2", null, 100, "g", 350, 12.3, 3, 24, 10, 14, 37, 0.4, "barcode");
    static Recipe stubRecipe1 = new Recipe(stubDB, "Recipe Name1", 4);
}
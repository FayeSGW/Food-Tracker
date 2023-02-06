import java.util.ArrayList;
import java.util.HashMap;

class Recipe {
    private String name;
    private HashMap<String, Food> ingredients;
    private HashMap<String, Integer> ingredientsList;
    private double nutrition[];
    private int servings;

    public Recipe(String name, int servings) {
        this.name = name;
        this.ingredients = new HashMap<>();
        this.ingredientsList = new HashMap<>();
        this.nutrition = new double[8];

        this.servings = servings;
    }

    public String showName() {
        return this.name;
    }

    public double[] showNutrition() {
        return this.nutrition;
    }

    public void addIngredient(String name, int weight, FoodDatabase data) {
        try {
            Food ingredient = data.addFromDatabase(name);
            double[] nutr = ingredient.unitNutrition();
            this.ingredients.put(name, ingredient);
            this.ingredientsList.put(name, weight);
            double[] weighted = new double[8];
            for (int i = 0; i < this.nutrition.length; i++) {
                weighted[i] = nutr[i] * weight;
                this.nutrition[i] = this.nutrition[i] + weighted[i];
            }   
        } catch (NullPointerException e) {
            System.out.println("Not in database! ");
        }
    }

    public void removeIngredient(String name) {
        Food food = this.ingredients.get(name);
        double[] weighted = new double[8];
        int weight = this.ingredientsList.get(name);
        for (int i = 0; i < this.nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight; 
            this.nutrition[i] = this.nutrition[i] - weighted[i];
        }
        this.ingredients.remove(name);
        this.ingredientsList.remove(name);
    }

    public String perServing() {
        double[] perServ = unitNutrition();
        String text = String.format("%s \nCalories: %.0f \nFat: %.1f g \nSaturated Fat: %.1f g \nCarbs: %.1f g \nSugar: %.1f g \nFibre: %.1f g \nProtein: %.1f g \nSalt: %.1f g", 
        this.name, perServ[0], perServ[1], perServ[2], perServ[3], perServ[4], perServ[5], perServ[6], perServ[7]);
        return text;
    }

    public double[] unitNutrition() {
        double[] unit = new double[8];
        for (int i = 0; i < this.nutrition.length; i++) {
            unit[i] = this.nutrition[i] / this.servings;
        }
        return unit;
    }

    public HashMap<String, Food> showIngredients() {
        return this.ingredients;
    }

    public String toString() {
        ArrayList<String> ingredients = new ArrayList<>();
        for (String name : this.ingredients.keySet()) {
            Food ingredient = this.ingredients.get(name);
            String strng = this.ingredientsList.get(name) + " " + ingredient.showUnit() + " " + name;
            ingredients.add(strng);
        }
        String list = this.name + ": " + String.join("," , ingredients);
        return list;
    }


    
}
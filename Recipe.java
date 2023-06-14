import java.util.ArrayList;
import java.util.HashMap;

class Recipe extends SupFood {
    private HashMap<String, Food> ingredients;
    private HashMap<String, Integer> ingredientsList;
    //private double nutrition[];

    public Recipe(String name, int servings) {
        super(name, servings);
        this.ingredients = new HashMap<>();
        this.ingredientsList = new HashMap<>();
        //this.nutrition = new double[8];

    }

    public String showUnit() {
        return "servings";
    }


    public void addIngredient(String name, int weight, Database data) {
        SupFood ingredient;
        try {
            ingredient = data.addFromDatabase(name);
            if (ingredient instanceof Food) {
                Food ingr = (Food) ingredient;
                double[] nutr = ingredient.unitNutrition();
                ingredients.put(name, ingr);
                ingredientsList.put(name, weight);
                double[] weighted = new double[8];
                for (int i = 0; i < nutrition.length; i++) {
                    weighted[i] = nutr[i] * weight;
                    nutrition[i] = nutrition[i] + weighted[i];
                }   
            } else {
                Recipe rec = (Recipe) ingredient;
                addFromRecipe(rec, weight, data);
            }
        } catch (NullPointerException e) {
            System.out.println("Not in database! ");
        } 
    }

    public void addFromRecipe(Recipe rec, int servings, Database data) {
        for (String name: rec.showIngredients().keySet()) {
            System.out.println(name);
            int weight = rec.showIngredientList().get(name);
            int totalServings = rec.weight();
            double newWeight = weight * ((double) servings / totalServings);
            weight = (int) newWeight;
            addIngredient(name, weight, data);
        }
    }

    public void removeIngredient(String name) {
        Food food = this.ingredients.get(name);
        double[] weighted = new double[8];
        int weight = ingredientsList.get(name);
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


    public HashMap<String, Food> showIngredients() {
        return ingredients;
    }

    public HashMap<String, Integer> showIngredientList() {
        return ingredientsList;
    }

    public String toString() {
        ArrayList<String> ingrdnts = new ArrayList<>();
        for (String name : ingredients.keySet()) {
            Food ingredient = ingredients.get(name);
            String strng = ingredientsList.get(name) + " " + ingredient.showUnit() + " " + name;
            ingrdnts.add(strng);
        }
        String list = name + ": " + String.join(", " , ingrdnts);
        return list;
    }


    
}
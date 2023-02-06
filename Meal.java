import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;

import java.util.ArrayList;

class Meal {
    private String name, date;
    private HashMap<String, Food> foods;
    private HashMap<String, Recipe> recipes;
    private HashMap<String, Integer> list;
    private HashMap<String, String> units;

    private double[] nutrition;

    public Meal(String name, String date) {
        this.name = name;
        this.date = date;
        this.foods = new HashMap<>();
        this.recipes = new HashMap<>();
        this.list = new HashMap<>();
        this.units = new HashMap<>();

        this.nutrition = new double[8];
    }
    
    public double[] addFood(String name, int weight, FoodDatabase database) {
        double [] weighted = new double[8];
        try {
            Food food = database.addFromDatabase(name);
            String unit = food.showUnit();
            double[] unitNutrition = food.unitNutrition();
            if (foods.containsKey(name)) {
                int oldWeight = this.list.get(name);
                int newWeight = oldWeight + weight;
                this.list.put(name, newWeight);
            } else {
                this.foods.put(name, food);
                this.list.put(name, weight);
                this.units.put(name, unit);
            }
            for (int i = 0; i < this.nutrition.length; i++) {
                weighted[i] = unitNutrition[i] * weight;
                this.nutrition[i] = this.nutrition[i] + weighted[i];
            }
        } catch (NullPointerException e) {
            System.out.println("Food not in database! ");
        }
        return weighted;
    }

    public double[] addRecipe(String name, int servings, RecipeDatabase database) {
        double[] weighted = new double[8];
        try {
            int newServings = servings;
            Recipe recipe = database.addFromDatabase(name);
            double[] unitNutrition = recipe.unitNutrition();
            if (this.recipes.containsKey(name)) {
                int oldServings = this.list.get(name);
                newServings = oldServings + servings;
                this.list.put(name, newServings);
            } else {
                this.recipes.put(name, recipe);
                this.list.put(name, servings);
                this.units.put(name, "serving");
            }
            for (int i = 0; i < this.nutrition.length; i++) {
                weighted[i] = unitNutrition[i] * servings;
                this.nutrition[i] = this.nutrition[i] + weighted[i];
            }
            
        } catch (NullPointerException e) {
            System.out.println("Recipe not in database!");
        }
        return weighted;
    }
    /*
    public void editFood(String name, int weight) {
        Food food = this.foods.get(name);
        double[] weighted = new double[8];
        double[] unit = food.unitNutrition();
        for (int i = 0; i < this.nutrition.length; i++) {
            weighted[i] = unit[i] * weight;
            this.nutrition[i] = this.nutrition[i] - weighted[i];
        }
        int oldWeight = this.list.get(name);
        int newWeight = oldWeight - weight;
        this.list.put(name, newWeight);
    }

    public void editRecipe(String name, int servings) {
        Recipe recipe = this.recipes.get(name);
        double[] weighted = new double[8];
        double[] unit = recipe.unitNutrition();
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = unit[i] * servings;
            nutrition[i] = nutrition[i] - weighted[i];
        }
        int oldServings = list.get(name);
        int newServings = oldServings - servings;
        list.put(name, newServings);
    }
    */

    public void edit(String name, int weight) {
        double[] weighted = new double[8];
        int oldWeight = list.get(name);
        int newWeight = oldWeight - weight;
        if (foods.containsKey(name)) {
            Food food = foods.get(name);
            for (int i = 0; i < nutrition.length; i++) {
                weighted[i] = food.unitNutrition()[i] * weight;
                nutrition[i] = nutrition[i] - weighted[i];
            }
        } else if (recipes.containsKey(name)) {
            Recipe recipe = recipes.get(name);
            for (int i = 0; i < nutrition.length; i++) {
                weighted[i] = recipe.unitNutrition()[i] * weight;
                nutrition[i] = nutrition[i] - weighted[i];
            }
        }
        list.put(name, newWeight);
    }

    /*
    public void removeFood(String name) {
        Food food = this.foods.get(name);
        double[] weighted = new double[8];
        int weight = this.list.get(name);
        for (int i = 0; i < this.nutrition.length; i++) {
            weighted[i] = food.unitNutrition()[i] * weight;
            this.nutrition[i] = this.nutrition[i] - weighted[i];
        }
        foods.remove(name); list.remove(name); units.remove(name);
    }
    */

    public double[] remove(String name) {
        double[] weighted = new double[8];
        int weight = list.get(name);
        if (foods.containsKey(name)) {
            Food food = this.foods.get(name);
            for (int i = 0; i < nutrition.length; i++) {
                weighted[i] = food.unitNutrition()[i] * weight;
                nutrition[i] = nutrition[i] - weighted[i];
            }
            foods.remove(name);
        } else if (recipes.containsKey(name)) {
            Recipe recipe = recipes.get(name);
            for (int i = 0; i < nutrition.length; i++) {
                weighted[i] = recipe.unitNutrition()[i] * weight;
                nutrition[i] = nutrition[i] - weighted[i];
            }
            recipes.remove(name);
        }
        list.remove(name); units.remove(name);
        return weighted;
    }
    /*
    public void removeRecipe(String name) {
        Recipe recipe = recipes.get(name);
        double[] weighted = new double[8];
        int servings = list.get(name);
        for (int i = 0; i < nutrition.length; i++) {
            weighted[i] = recipe.unitNutrition()[i] * servings;
            nutrition[i] = nutrition[i] - weighted[i];
        }
        foods.remove(name); list.remove(name); units.remove(name);
    }  */

    public void removeAll() {
        foods.clear();
        recipes.clear();
        list.clear();
        units.clear();
        nutrition = new double[8];
    }

    public double[] showNutrition() {
        return nutrition;
    }

    public String showName() {
        return name;
    }

    public String toString() {
        ArrayList<String> stringList = new ArrayList<>();
        for (String food: this.foods.keySet()) {
            int weight = this.list.get(food);
            String unit = this.units.get(food);
            String strng = String.valueOf(weight) + " " + unit + " " + food;
            stringList.add(strng);
        }
        for (String recipe: this.recipes.keySet()) {
            int weight = this.list.get(recipe);
            String strng = String.valueOf(weight) + " servings " + recipe;
            stringList.add(strng);
        }
        String foods = String.join(",", stringList);
        String nutrition = String.format("\nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        return foods + nutrition;
    }
}
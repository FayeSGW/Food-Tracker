package db;

import java.util.ArrayList;
import java.util.Scanner;

public class Food extends SupFood{
    Scanner scanner = new Scanner(System.in);
    private String unit;
    private String barcode;
    private ArrayList<String> foodType;
    //double[] nutrition = new double[8];

    public Food(Database data, String name, int weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt, String barcode) {
        super(data, name, weight);
        this.barcode = barcode;
        this.unit = unit;
        foodType = new ArrayList<>();
        this.nutrition[0] = calories; this.nutrition[1] = fat; this.nutrition[2] = satfat; this.nutrition[3] = carbs; this.nutrition[4] = sugar; this.nutrition[5] = fibre; this.nutrition[6] = protein; this.nutrition[7] = salt;
        }

    @Override
    public String toString() {
        String text = String.format("%s: \n%s \n%.0f calories \n%.1f g fat \n%.1f g saturated fat \n%.1f g carbohydrates\n%.1f g sugar \n%.1f g fibre \n%.1f g protein \n%.1f g salt", name, showWeight(), this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        return text;
    }

    public void addFoodType(String type) {
        foodType.add(type);
        for (SupFood food: data.access().values()) {
            if (food instanceof Recipe) {
                Recipe recipe = (Recipe) food;
                if (recipe.showIngredients().keySet().contains(name)) {
                    recipe.addFoodType(type);
                }
            }
        }
    }

    public void removeFoodType(String type) {
        foodType.remove(type);
        for (SupFood food: data.access().values()) {
            if (food instanceof Recipe) {
                Recipe recipe = (Recipe) food;
                if (!recipe.checkIngredientFoodTypes(name, type)) {
                    recipe.removeFoodType(type);
                }
            }
        }
    }

    public ArrayList<String> showFoodTypes() {
        return foodType;
    }

    public String showBarcode() {
        return barcode;
    }

    public String showWeight() {
        String show = weight +" " + unit;
        return show;
    }

    @Override
    public String showUnit() {
        return unit;
    }


    public void editNutrition() {
        System.out.println("Which nutrient would you like to change? ");
        String choice = scanner.nextLine();
        System.out.println("Give new amount: ");
        double amount = Double.valueOf(scanner.nextLine());
        System.out.println("Per how much? ");
        int weight = Integer.valueOf(scanner.nextLine());
        double nut = (amount/weight)*this.weight;
        String[] nutrients = {"Calories", "Fat", "Sat Fat", "Carbs", "Sugar", "Fibre", "Protein", "Salt"};
        for (int i = 0; i < nutrients.length; i++) {
            if (choice.equals(nutrients[i])) {
                nutrition[i] = nut;
            }
        }
    }
 
}

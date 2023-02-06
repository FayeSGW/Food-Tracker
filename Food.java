
import java.util.Scanner;

class Food {
    Scanner scanner = new Scanner(System.in);
    private String name, unit;
    private int weight;
    double[] nutrition = new double[8];

    public Food(String name, int weight, String unit, double calories, double fat, double satfat, double carbs, double sugar, double fibre, double protein, double salt) {
        this.name = name;
        this.weight = weight;
        this.unit = unit;
        this.nutrition[0] = calories; this.nutrition[1] = fat; this.nutrition[2] = satfat; this.nutrition[3] = carbs; this.nutrition[4] = sugar; this.nutrition[5] = fibre; this.nutrition[6] = protein; this.nutrition[7] = salt;
        }

    @Override
    public String toString() {
        String text = String.format("%s: \n%s \n%f calories \n%.1f g fat \n%.1f g saturated fat \n%.1f g carbohydrates\n%.1f g sugar \n%.1f g fibre \n%.1f g protein \n%.1f g salt", this.name, showWeight(), this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        return text;
    }

    public String showName() {
        return this.name;
    }

    public int weight() {
        return this.weight;
    }

    public String showWeight() {
        String show = this.weight + this.unit;
        return show;
    }

    public String showUnit() {
        return this.unit;
    }

    public double[] showNutrition() {
        return this.nutrition;
    }

    public void editName() {
        System.out.println("Give new name: ");
        String name = scanner.nextLine();
        this.name = name;
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
                this.nutrition[i] = nut;
            }
        }
    }

    public double[] unitNutrition() {
        double[] unitN = new double[8];
        for (int i = 0; i < this.nutrition.length; i++) {
            unitN[i] = (this.nutrition[i])/this.weight;
        }
        return unitN;
    }    
}


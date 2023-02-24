import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

abstract class SupFood {
    Scanner scanner = new Scanner(System.in);
    protected String name;
    protected double nutrition[] = new double[8];
    protected int servings;

    public SupFood(String name, int servings) {
        this.name = name;
        this.servings = servings;
    }

    public String showName() {
        return name;
    }

    abstract String showUnit();

    abstract double[] showNutrition();

    public int servings() {
        return servings;
    }

    public void editName() {
        System.out.println("Give new name: ");
        String name = scanner.nextLine();
        this.name = name;
    }

    /*public String perServing() {
        double[] perServ = unitNutrition();
        String text = String.format("%s \nCalories: %.0f \nFat: %.1f g \nSaturated Fat: %.1f g \nCarbs: %.1f g \nSugar: %.1f g \nFibre: %.1f g \nProtein: %.1f g \nSalt: %.1f g", 
        this.name, perServ[0], perServ[1], perServ[2], perServ[3], perServ[4], perServ[5], perServ[6], perServ[7]);
        return text;
    }*/

    public double[] unitNutrition() {
        double[] unit = new double[8];
        for (int i = 0; i < nutrition.length; i++) {
            unit[i] = nutrition[i] / servings;
        }
        return unit;
    }


    public String toString() {
        return "";
    }


    
}
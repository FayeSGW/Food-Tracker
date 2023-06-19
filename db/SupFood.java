package db;

import java.util.Scanner;
import SQL.java.connect.sql.code.*;

public abstract class SupFood {
    Scanner scanner = new Scanner(System.in);
    protected String name;
    protected double nutrition[];
    protected int weight;
    protected Database data;

    public SupFood(Database data, String name, int weight) {
        this.name = name;
        this.weight = weight;
        nutrition = new double[8];
        this.data = data;
    }

    public String showName() {
        return name;
    }

    public abstract String showUnit();

    public double[] showNutrition() {
        return nutrition;
    }

    public int weight() {
        return weight;
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
            unit[i] = nutrition[i] / weight;
        }
        return unit;
    }


    public String toString() {
        return "";
    }


    
}
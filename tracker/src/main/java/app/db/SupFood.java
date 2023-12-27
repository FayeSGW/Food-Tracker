package app.db;

import java.util.Scanner;


public abstract class SupFood {
    Scanner scanner = new Scanner(System.in);
    protected String name;
    protected double nutrition[];
    protected double weight;
    protected Database data;
    protected boolean deleted = false;

    public SupFood(Database data, String name, double weight) {
        this.name = name;
        this.weight = weight;
        nutrition = new double[8];
        this.data = data;
    }

    public abstract String showName();

    public String showDisplayName() {
        return name;
    }

    public abstract String showUnit();

    public double[] showNutrition() {
        return nutrition;
    }

    public double weight() {
        return weight;
    }

    public void editName() {
        System.out.println("Give new name: ");
        String name = scanner.nextLine();
        this.name = name;
    }

    public double[] showUnitNutrition() {
        double[] unit = new double[8];
        for (int i = 0; i < nutrition.length; i++) {
            unit[i] = nutrition[i] / weight;
        }
        return unit;
    }

    public void setDeleted() {
        deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String toString() {
        return "";
    }
}
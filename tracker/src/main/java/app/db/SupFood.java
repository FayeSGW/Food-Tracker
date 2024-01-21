package app.db;

import java.util.Scanner;


public abstract class SupFood {
    Scanner scanner = new Scanner(System.in);
    protected String name;
    protected double nutrition[];
    protected double weight;
    protected Database data;
    protected boolean deleted = false;
    protected int index;
    static int indexCounter = 0;

    public SupFood(Database data, Integer index, String name, double weight) {
        if (index != null) {
            this.index = index;
            if (index > indexCounter) {
                indexCounter = index;
            } 
        } else {
            indexCounter++;
            this.index = indexCounter;
        }
        this.name = name;
        this.weight = weight;
        nutrition = new double[8];
        this.data = data;
    }

    public int showIndex() {
        return index;
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

    public void setNotDeleted() {
        deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
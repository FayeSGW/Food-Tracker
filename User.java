import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class User implements java.io.Serializable {
    private String name, gender, goal, dateOfBirth;
    private int height, age;
    private double weight, rate;
    private double[] nutrition = new double[8];
    private Database data;


    public User(String name, String gender, double weight, int height, String dateOfBirth, String goal, double rate) {
        this.name = name;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.dateOfBirth = dateOfBirth;
        this.goal = goal;
        this.rate = rate;
        this.data = new Database(name + "'s Database");
        this.calculateAge();
        this.updateNutrition();

        
    }

    public void calculateAge() {
        LocalDate today = LocalDate.now();
        String[] dobSplit = dateOfBirth.split("\\.");
        int[] dobInt = {Integer.parseInt(dobSplit[0]), Integer.parseInt(dobSplit[1]), Integer.parseInt(dobSplit[2])};
        LocalDate dob = LocalDate.of(dobInt[2], dobInt[1], dobInt[0]);
        long age = ChronoUnit.YEARS.between(dob, today);
        int ageInt = Math.toIntExact(age);
        age = ageInt;

    }


    public int weightGoal(double rate, String goal) {
        int dailyCalories = (int) (rate * 7700) / 7;
        int calories = 0;
        if (goal.toUpperCase().equals("L")) { //Calorie defecit for weight loss
            calories = -1 * dailyCalories;
        } else if (goal.toUpperCase().equals("G")) { //Calorie excess for weight gain
            calories = dailyCalories;
        } else if (goal.toUpperCase().equals("M")) { //Calories for weight maintenance
            calories = 0;
        }
        return calories;
    }

    public void updateNutrition() {
        double msj = 0;
        double hb = 0;
        double fibre = 0;
        double satfat = 0;
        if (gender.toUpperCase().equals("M")) {
            msj = (10 * weight) + (6.25 * height) - (5 * age) + 5; //Mifflin-St Jeor Equation
            hb = (13.397 * weight) + (4.799 * height) - (5.677 * age) + 88.362; //Revised Harris-Benedict Equation
            fibre = 33;
            satfat = 30;
        } else if (gender.toUpperCase().equals("F")) {
            msj = (10 * weight) + (6.26 * height) - (5 * age) - 161; //Mifflin-St Jeor Equation
            hb = (9.247 * weight) + (3.098 * height) - (4.33 * age) + 447.593; //Revised Harris-Benedict Equation
            fibre = 27;
            satfat = 20;
        }
        double sugar = 50;
        double salt = 6;
        double restingCalories = ((msj + hb) / 2) * 1.2;
        int calories = (int)restingCalories + weightGoal(rate, goal);
        double protein = (calories * 0.25) / 4;
        double carbs = (calories * 0.5) / 4;
        double fat = (calories * 0.25) / 9;

        nutrition[0] = calories;
        nutrition[1] = fat;
        nutrition[2] = satfat;
        nutrition[3] = carbs;
        nutrition[4] = sugar;
        nutrition[5] = fibre;
        nutrition[6] = protein;
        nutrition[7] = salt;
    }



    public void updateWeight(double weight) {
        this.weight = weight;
        updateNutrition();
    }

    public void updateGoal(double rate, String goal) {
        this.rate = rate;
        this.goal = goal;
        updateNutrition();
    }

    public double[] showNutrition() {
        return nutrition;
    }

    public String showName() {
        return name;
    }

    public int showAge() {
        return age;
    }

    public Database accessDatabase() {
        return data;
    }

    @Override
    public String toString() {
        String nutrition = String.format("\nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        String strng = String.format("Name: %s \nGender: %s \nCurrent weight: %d \nHeight: %d \nAge: %d \nRecommended food intake: %s", this.name, this.gender, this.weight, this.height, this.age, nutrition);
        return strng;
    }
}
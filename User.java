import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class User implements java.io.Serializable {
    private String name, gender, goal, dateOfBirth;
    private int weight, height, age, rate;
    private double[] nutrition = new double[8];
    private FoodDatabase fdata;
    private RecipeDatabase rdata;


    public User(String name, String gender, int weight, int height, String dateOfBirth, String goal, int rate) {
        this.name = name;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.dateOfBirth = dateOfBirth;
        this.goal = goal;
        this.rate = rate;
        this.fdata = new FoodDatabase(name + "'s Food Database");
        this.rdata = new RecipeDatabase(name + "'s Recipe Database");
        this.calculateAge();
        this.updateNutrition();

        
    }

    public void calculateAge() {
        LocalDate today = LocalDate.now();
        String[] dobSplit = this.dateOfBirth.split("-");
        int[] dobInt = {Integer.parseInt(dobSplit[0]), Integer.parseInt(dobSplit[1]), Integer.parseInt(dobSplit[2])};
        LocalDate dob = LocalDate.of(dobInt[0], dobInt[1], dobInt[2]);
        long age = ChronoUnit.YEARS.between(dob, today);
        int ageInt = Math.toIntExact(age);
        this.age = ageInt;

    }


    public int weightGoal(int rate, String goal) {
        int dailyCalories = (rate * 7700) / 7;
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
        if (this.gender.toUpperCase().equals("M")) {
            msj = (10 * this.weight) + (6.25 * this.height) - (5 * this.age) + 5; //Mifflin-St Jeor Equation
            hb = (13.397 * this.weight) + (4.799 * this.height) - (5.677 * this.age) + 88.362; //Revised Harris-Benedict Equation
            fibre = 33;
            satfat = 30;
        } else if (this.gender.toUpperCase().equals("F")) {
            msj = (10 * this.weight) + (6.26 * this.height) - (5 * this.age) - 161; //Mifflin-St Jeor Equation
            hb = (9.247 * this.weight) + (3.098 * this.height) - (4.33 * this.age) + 447.593; //Revised Harris-Benedict Equation
            fibre = 27;
            satfat = 20;
        }
        double sugar = 50;
        double salt = 6;
        double restingCalories = ((msj + hb) / 2) * 1.2;
        int calories = (int)restingCalories + this.weightGoal(this.rate, this.goal);
        double protein = (calories * 0.25) / 4;
        double carbs = (calories * 0.5) / 4;
        double fat = (calories * 0.25) / 9;

        this.nutrition[0] = calories;
        this.nutrition[1] = fat;
        this.nutrition[2] = satfat;
        this.nutrition[3] = carbs;
        this.nutrition[4] = sugar;
        this.nutrition[5] = fibre;
        this.nutrition[6] = protein;
        this.nutrition[7] = salt;
    }



    public void updateWeight(int weight) {
        this.weight = weight;
        this.updateNutrition();
    }

    public void updateGoal(int rate, String goal) {
        this.rate = rate;
        this.goal = goal;
        this.updateNutrition();
    }

    public double[] showNutrition() {
        return this.nutrition;
    }

    public String showName() {
        return this.name;
    }

    public int showAge() {
        return this.age;
    }

    @Override
    public String toString() {
        String nutrition = String.format("\nCalories: %.0f \nFat: %.1f \nSaturated Fat: %.1f \nCarbohydrates: %.1f \nSugar: %.1f \nFibre: %.1f \nProtein: %.1f \nSalt: %.1f", this.nutrition[0], this.nutrition[1], this.nutrition[2], this.nutrition[3], this.nutrition[4], this.nutrition[5], this.nutrition[6], this.nutrition[7]);
        String strng = String.format("Name: %s \nGender: %s \nCurrent weight: %d \nHeight: %d \nAge: %d \nRecommended food intake: %s", this.name, this.gender, this.weight, this.height, this.age, nutrition);
        return strng;
    }
}
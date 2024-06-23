package app.diary;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import exceptions.*;
import app.db.*;
import app.sql.java.connect.*;

public class User implements java.io.Serializable {
    private String name, gender, goal, dateOfBirth;
    private int height, age, water;
    private double weight, rate, bodyFat = 0;
    private double[] nutrition = new double[8];
    private Database data;
    private Diary diary;
    private HashMap<String, Double> measurements;
    public LocalDate today = LocalDate.now();
    private String[] measurementTypes = {"Waist", "Hips", "Calf", "Thigh", "Upper Arm", "Chest", "Underwire", "Body Fat"};


    //Gender = Male/Female, weight in kg, height in cm, DOB as YYYY-MM-DD, goal = loss,gain, maintain
    public User(String name, String gender, double weight, int height, String dateOfBirth, 
        String goal, double rate, int water, HashMap<String, Double> measurement) throws NoNegativeException {
        //No exception checking for these because their values are limited by the GUI
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.goal = goal;

        age = calculateAge(today);
        this.weight = checkForNegativeValues("Weight", weight);
        this.height = (int) checkForNegativeValues("Height", height);
        this.rate = checkForNegativeValues("Rate", rate);
        this.water = (int) checkForNegativeValues("Water", water);

        this.data = new Database(name + "'s Database");
        this.diary = new Diary(name, this);    
        
        updateNutrition(today);

        measurements = measurement;
        if (measurements != null) {
            measurements.put("Upper Arm", measurements.get("UpperArm"));
            measurements.put("Body Fat", measurements.get("BodyFat"));
            measurements.remove("UpperArm"); measurements.remove("BodyFat");
        } else {
            measurements = new HashMap<>();
            for (String type: measurementTypes) {
                measurements.put(type, 0.0);
            }
        }
        
        
    }

    //In order to pass mocks
    public void setTesting(Diary diary, Database db) {
        data = db;
        this.diary = diary;
    }

    //Throws exception if user tries to input negative value for certain parameters
    public double checkForNegativeValues(String parameter, double value) throws NoNegativeException {
        if (value < 0) {
            throw new NoNegativeException(parameter + " cannot have negative value!");
        }
        return value;
    }
 
    public Database accessDatabase() {
        return data;
    }

    public Diary accessDiary() {
        return diary;
    }

    public void edit(String oldName, String name, String gender, double weight, int height, String dob, String goal, double rate, int water,
        double[] measurements) {
        changeName(name);
        changeGender(gender);
        changeHeight(height);
        changeDOB(dob);
        updateGoal(rate, goal);
        setWaterGoal(water);

        Day day = diary.getDay(today);
        day.addWeightFromGUI(weight);
        for (int i = 0; i < measurements.length-1; i++) {
            day.setMeasurementFromGUI(measurementTypes[i], measurements[i], false);
        }
        day.setMeasurementFromGUI("Body Fat", measurements[measurements.length-1], true);
        AddToDiary.addUser(this, oldName);
    }

    public int calculateAge(LocalDate now) throws NoNegativeException {
        LocalDate dob = LocalDate.parse(dateOfBirth);
        long age = ChronoUnit.YEARS.between(dob, now);
        int ageInt = Math.toIntExact(age);
        
        if (dob.isAfter(now)) {
            throw new NoNegativeException("DOB can't be after the current date!");
        }
        return ageInt;
    }


    public int caloriesForWeightGoal() {
        int dailyCalories = (int) (rate * 7700) / 7;
        int calories = 0;
        if (goal.toLowerCase().equals("lose")) { //Calorie defecit for weight loss
            calories = -1 * dailyCalories;
        } else if (goal.toLowerCase().equals("gain")) { //Calorie excess for weight gain
            calories = dailyCalories;
        } else if (goal.toLowerCase().equals("maintain")) { //Calories for weight maintenance
            calories = 0;
        }
        return calories;
    }

    public double[] calculateNutrition(LocalDate date, double weight) {
        int age = this.age;
        double[] nutrition = new double[8];
        try {
            age = calculateAge(date);
        } catch (NoNegativeException e) {}
        double msj = 0;
        double hb = 0;
        double fibre = 0;
        double satfat = 0;
        //Calculates BMR using two formulae for each gender
        if (gender.toLowerCase().equals("male")) {
            msj = (10 * weight) + (6.25 * height) - (5 * age) + 5; //Mifflin-St Jeor Equation
            hb = (13.397 * weight) + (4.799 * height) - (5.677 * age) + 88.362; //Revised Harris-Benedict Equation
            fibre = 33;
            satfat = 30;
        } else if (gender.toLowerCase().equals("female")) {
            msj = (10 * weight) + (6.26 * height) - (5 * age) - 161; //Mifflin-St Jeor Equation
            hb = (9.247 * weight) + (3.098 * height) - (4.33 * age) + 447.593; //Revised Harris-Benedict Equation
            fibre = 27;
            satfat = 20;
        }
        double sugar = 50;
        double salt = 6;
        double restingCalories = ((msj + hb) / 2) * 1.2;
        double calories = Math.rint(restingCalories + caloriesForWeightGoal());
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

        return nutrition;
    }

    public void updateNutrition(LocalDate date) {
        nutrition = calculateNutrition(date, weight);
    }

    public void updateWeight(LocalDate date, double weight) {
        diary.addDay(date); //Ensures that if day doesn't already exist, we create it
        this.weight = weight;
        updateNutrition(date);
        // This ensures that if a more recent weight has been entered in the diary, then 
        // we don't overwrite it.
        for (LocalDate day: diary.showDays()) {
            if (day.isAfter(date) == true && diary.getDay(day).showWeight() > 0) {
                return;
            }
        }
        
        //This should now be handled in the GUI
        /*try {
            this.weight = checkForNegativeValues("Weight", weight);
            updateNutrition();
        } catch (NoNegativeException e) {
            e.getMessage();
        }*/
        
    }

    public void updateGoal(double rate, String goal) {
        this.rate = rate;
        this.goal = goal;
        updateNutrition(today);
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeGender(String gender) {
        this.gender = gender;
    }

    public void changeHeight(int height) {
        this.height = height;
    }

    public void changeDOB(String dob) {
        dateOfBirth = dob;
    }

    public String showName() {
        return name;
    }

    public String showGender() {
        return gender;
    }

    public int showHeight() {
        return height;
    }

    public double showWeight() {
        return weight;
    }

    public String showDOB() {
        return dateOfBirth;
    }

    public String showGoal() {
        return goal;
    }

    public double showRate() {
        return rate;
    }

    public double[] showNutrition() {
        return nutrition;
    }

    public void setWaterGoal(int amount) {
        water = amount;
    }

    public int showWater() {
        return water;
    }

    public void addNewMeasurementType(String type) {
        if (!measurements.containsKey(type)) {
            measurements.put(type, null);
        }
    }

    public void setMeasurement(LocalDate date, String type, double measurement) {
        if (measurements.containsKey(type)) {
            diary.addDay(date);

            for (LocalDate day: diary.showDays()) {
                if (day.isAfter(date) && diary.getDay(day).getSingleMeasurement(type) > 0) {
                    return;
                }
            }
            measurements.put(type, measurement);
        }
    }

    public HashMap<String, Double> getMeasurements() {
        return measurements;
    }

    public double getSingleMeasurement(String type) {
        return measurements.get(type);
    }

}
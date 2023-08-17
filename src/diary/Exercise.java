package src.diary;

public class Exercise {
    private String name;
    private int time, calories;

    public Exercise(String name, int time, int calories) {
        this.name = name;
        this.time = time;
        this.calories = calories;
    }

    public String showName() {
        return name;
    }

    public void alter(int time, int calories) {
        this.time = time;
        this.calories = calories;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public int showCalories() {
        return calories;
    }

    public int showTime() {
        return time;
    }

    public String toString() {
        return this.name + ": " + this.time + " minutes, " + this.calories + " calories burned.";
    }
}
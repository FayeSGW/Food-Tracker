package src.diary;

public class Exercise {
    private String name;
    private int minutes, seconds, calories;

    public Exercise(String name, int minutes, int seconds, int calories) {
        this.name = name;
        this.minutes = minutes;
        this.seconds = seconds;
        this.calories = calories;
    }

    public String showName() {
        return name;
    }

    public void alter(int minutes, int seconds, int calories) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.calories = calories;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public int showCalories() {
        return calories;
    }

    public String showTime() {
        String mins = Integer.toString(minutes);
        String secs = Integer.toString(seconds);
        if (minutes < 10) {
            mins = "0" + mins;
        }
        if (seconds < 10) {
            secs = "0" + secs;
        }
        return mins + ":" + secs;
    }

    public String toString() {
        return this.name + ": "  + " minutes, " + this.calories + " calories burned.";
    }
}
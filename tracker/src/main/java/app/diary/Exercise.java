package app.diary;

public class Exercise {
    private String name;
    private int minutes, seconds, calories, index;
    static int indexCounter = 0;

    public Exercise(Integer index, String name, int minutes, int seconds, int calories) {
        if (index != null) {
            this.index = index;
            indexCounter = index;
        } else {
            indexCounter++;
            this.index = indexCounter;
        }
        this.name = name;
        this.minutes = minutes;
        this.seconds = seconds;
        this.calories = calories;
    }

    public String showName() {
        return name;
    }

    public int showIndex() {
        return index;
    }

    public void edit(String name, int minutes, int seconds, int calories) {
        this.name = name;
        this.minutes = minutes;
        this.seconds = seconds;
        this.calories = calories;
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
}
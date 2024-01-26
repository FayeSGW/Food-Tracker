package app.diary;

import java.time.LocalDate;
import java.util.TreeMap;
import java.util.Set;

import app.sql.java.connect.*;

public class Diary implements java.io.Serializable {
    private String name;
    private TreeMap<LocalDate, Day> diary;
    private User user;
    private LocalDate current;
    
    public Diary(String name, User user) {
        this.name = name;
        this.user = user;
        diary = new TreeMap<>();
        current = LocalDate.now();
    }

    public Set<LocalDate> showDays() {
        return diary.keySet();
    }

    public String showName() {
        return name;
    }

    public LocalDate showCurrentDate() {
        return current;
    }

    //For adding days from the database
    public Day addSavedDays(LocalDate date) {
        Day day = new Day(date, user);
        diary.put(date, day);
        return day;
    }

    //For adding new days from GUI
    public Day addDay(LocalDate date) {
        if (!diary.keySet().contains(date)) {
            Day day = new Day(date, user);
            diary.put(date, day);
            AddToDiary.addDay(day);
            return day;
        }
        Day d = diary.get(date);
        return d;
    }

    public Day goToDay(LocalDate date) {
        Day day = this.addDay(date);
        current = date;
        return day;
    }

    public Day getDay(LocalDate date) {
        return diary.get(date);
    }

    public Day goToToday() {
        LocalDate today = LocalDate.now();
        current = today;
        return diary.get(today);
    }
}
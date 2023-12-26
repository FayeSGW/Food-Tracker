package app.diary;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Set;
import java.time.LocalDate;

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

    public int showWater() {
        return 8;
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

    /*public String showDay(LocalDate date) {
        Day d = this.addDay(date);
        //Day d = diary.get(date);
        current = date;
        return d.toString();
    }*/

    public Day getDay(LocalDate date) {
        Day day = this.addDay(date);
        current = date;
        //return diary.get(date);
        return day;
    }

    /*public Day next(LocalDate date) {
        LocalDate nxt = diary.higherKey(date);
        try {
            diary.get(nxt);
        } catch (NullPointerException e) {
            /*String[] day = date.split("-");
            int[] dayInt = {Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2])};
            LocalDate dayDate = LocalDate.of(dayInt[0], dayInt[1], dayInt[2]);
            LocalDate nextDay = dayDate.plusDays(1);
            nxt = nextDay.toString();
            this.addDay(nxt);
         }
        //System.out.println(nxt);
        current = nxt;
        return diary.get(nxt);
    }*/

    /*public Day previous(LocalDate date) {
        LocalDate prev = diary.lowerKey(date);
        try {
            diary.get(prev);
        } catch (NullPointerException e) {
            String[] day = date.split("-");
            int[] dayInt = {Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2])};
            LocalDate dayDate = LocalDate.of(dayInt[0], dayInt[1], dayInt[2]);
            LocalDate prevDay = dayDate.minusDays(1);
            prev = prevDay.toString();
            this.addDay(prev);
        }
        current = prev;
        return diary.get(prev);
    }*/

    public Day goToToday() {
        LocalDate today = LocalDate.now();
        //String todayS = today.toString();
        current = today;
        return diary.get(today);
    }

    public String toString() {
        String s = "";
        for (LocalDate date: diary.keySet()) {
            s += diary.get(date).showDate() + " " + diary.get(date).showWaterDrunk() + " ";
        }
        return s;
    }

}
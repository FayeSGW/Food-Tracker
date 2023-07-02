package src.diary;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Set;
import java.time.LocalDate;

import src.SQL.java.connect.sql.code.*;

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

    public Day addSavedDays(LocalDate date) {
        Day day = new Day(date, user);
        diary.put(date, day);
        return day;
    }

    public void addDay(LocalDate date) {
        if (!diary.keySet().contains(date)) {
            Day day = new Day(date, user);
            diary.put(date, day);
            AddToDiary.addDay(day);
        }
    }

    public String showDay(LocalDate date) {
        this.addDay(date);
        Day d = diary.get(date);
        current = date;
        return d.toString();
    }

    public Day getDay(LocalDate date) {
        this.addDay(date);
        current = date;
        return diary.get(date);
    }

    public Day next(LocalDate date) {
        LocalDate nxt = diary.higherKey(date);
        try {
            diary.get(nxt);
        } catch (NullPointerException e) {
            /*String[] day = date.split("-");
            int[] dayInt = {Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2])};
            LocalDate dayDate = LocalDate.of(dayInt[0], dayInt[1], dayInt[2]);
            LocalDate nextDay = dayDate.plusDays(1);
            nxt = nextDay.toString();
            this.addDay(nxt);*/
         }
        //System.out.println(nxt);
        current = nxt;
        return diary.get(nxt);
    }

    public Day previous(LocalDate date) {
        LocalDate prev = diary.lowerKey(date);
        try {
            diary.get(prev);
        } catch (NullPointerException e) {
            /*String[] day = date.split("-");
            int[] dayInt = {Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2])};
            LocalDate dayDate = LocalDate.of(dayInt[0], dayInt[1], dayInt[2]);
            LocalDate prevDay = dayDate.minusDays(1);
            prev = prevDay.toString();
            this.addDay(prev);*/
        }
        current = prev;
        return diary.get(prev);
    }

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
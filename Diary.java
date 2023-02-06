import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Set;

class Diary {
    private String name;
    private TreeMap<String, Day> diary;
    private User user;
    
    public Diary(String name, User user) {
        this.name = name;
        this.user = user;
        this.diary = new TreeMap<>();
    }

    @Override
    public String toString() {
        return String.join(", ", diary.keySet());
    }

    public Set<String> showDays() {
        return diary.keySet();
    }

    public String showName() {
        return name;
    }

    public void addDay(String date) {
        if (!diary.keySet().contains(date)) {
            Day day = new Day(date, user);
            diary.put(date, day);
        }
    }

    public String showDay(String date) {
        this.addDay(date);
        Day d = diary.get(date);
        return d.toString();
    }

    public Day getDay(String date) {
        this.addDay(date);
        return diary.get(date);
    }

    public Day next(String date) {
        String nxt = diary.higherKey(date);
        try {
            diary.get(nxt);
        } catch (NullPointerException e) {
            String[] day = date.split("-");
            int[] dayInt = {Integer.parseInt(day[0]), Integer.parseInt(day[1]), Integer.parseInt(day[2])};
            LocalDate dayDate = LocalDate.of(dayInt[0], dayInt[1], dayInt[2]);
            LocalDate nextDay = dayDate.plusDays(1);
            nxt = nextDay.toString();
            this.addDay(nxt);
        }
        System.out.println(nxt);
        return diary.get(nxt);
    }

    public Day previous(String date) {
        String prev = diary.lowerKey(date);
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
        System.out.println(prev);
        return diary.get(prev);
    }

    public Day goToToday() {
        LocalDate today = LocalDate.now();
        String todayS = today.toString();
        return diary.get(todayS);
    }

}
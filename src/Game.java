import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Game {
    private String name;
    private DayOfWeek resetDoW;
    private int resetHour;
    private ArrayList<Todo> todo;

    public Game(String name, DayOfWeek resetDoW, int resetHour) {
        this.name = name;
        this.resetDoW = resetDoW;
        this.resetHour = resetHour;
    }

    public Game(String selectedItem) {
    }

    public void register(Todo todo) {
        this.todo.add(todo);
    }

    public LocalDate getAdjustedDate() {
        LocalDateTime now = LocalDateTime.now();
        if (resetHour != 0) {
            if (now.getHour() < resetHour) {
                now = now.minusDays(1);
            }
        }
        return now.toLocalDate();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

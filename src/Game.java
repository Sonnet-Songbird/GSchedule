import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.UUID;

public class Game {
    private String name; // 식별자
    private DayOfWeek resetDoW;
    private int resetHour;
    private ArrayList<Todo> todo;
    private ArrayList<UUID> todoIDs;

    public Game(String name, DayOfWeek resetDoW, int resetHour) {
        this.name = name;
        this.resetDoW = resetDoW;
        this.resetHour = resetHour;
        this.todo = new ArrayList<>();
        this.todoIDs = new ArrayList<>();
    }

    public Game(String name, DayOfWeek resetDoW, int resetHour, ArrayList<UUID> todoIDs) { // 로딩용 생성자
        this.name = name;
        this.resetDoW = resetDoW;
        this.resetHour = resetHour;
        this.todo = new ArrayList<>();
        this.todoIDs = todoIDs;
    }

    public void register(Todo todo) {
        this.todo.add(todo);
        this.todoIDs.add(todo.getTodoID());
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

    public DayOfWeek getResetDoW() {
        return resetDoW;
    }

    public int getResetHour() {
        return resetHour;
    }

    public ArrayList<Todo> getTodo() {
        return todo;
    }

    public ArrayList<UUID> getTodoIDs() {
        return todoIDs;
    }

    public String getTodoIDsAsString() {
        StringJoiner joiner = new StringJoiner(",");
        for (UUID todoID : todoIDs) {
            joiner.add(todoID.toString());
        }
        return joiner.toString();
    }

    public void matchTodoWithID() {
        for (UUID todoID : todoIDs) {
            //todo: todo 로드 떄 todoID를 키로 하는 Map 넘겨받아서 순회하면서 todo에 추가
        }
    }
}

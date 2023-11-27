import java.time.LocalDate;

public class Todo {
    Game game;
    String name;
    String detail;
    int goal;
    int count;
    LocalDate recentReset;
    ResetType resetType;


    public Todo(Game game, String name, String detail, int goal, ResetType resetType) {
        this.game = game;
        this.detail = detail;
        this.goal = goal;
        this.count = 0;
        this.recentReset = game.getAdjustedDate();
        this.resetType = resetType;
    }

    public void reset() {
        count = 0;
        recentReset = game.getAdjustedDate();
    }

    public void resetAll() {

    }

    public enum ResetType {
        DAILY,      // 일일 초기화
        WEEKLY,     // 주간 초기화
        MONTHLY,    // 월간 초기화
        ALWAYS,     // 항상 초기화
        MANUAL      // 수동 초기화
    }

}

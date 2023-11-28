//Todo: 선택 입력 사항 폰트 수정

import java.time.LocalDate;

public class Todo {
    private Game game;
    private String name;
    private String detail;
    private int goal;
    private int count;
    private LocalDate recentReset;
    private ResetType resetType;


    public Todo(Game game, String name, int goal, ResetType resetType) {
        this.game = game;
        this.name = name;
        this.detail = "";
        this.goal = goal;
        this.count = 0;
        this.recentReset = game.getAdjustedDate();
        this.resetType = resetType;
        game.register(this);
    }

    public void reset() {
        count = 0;
        recentReset = game.getAdjustedDate();
    }

    public void resetAll() {

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getRecentReset() {
        return recentReset;
    }

    public void setRecentReset(LocalDate recentReset) {
        this.recentReset = recentReset;
    }

    public ResetType getResetType() {
        return resetType;
    }

    public void setResetType(ResetType resetType) {
        this.resetType = resetType;
    }

    public enum ResetType {
        DAILY,      // 일일 초기화
        WEEKLY,     // 주간 초기화
        MONTHLY,    // 월간 초기화
        ALWAYS,     // 항상 초기화
        MANUAL      // 수동 초기화
    }

    @Override
    public String toString() {
        return name;
    }
}

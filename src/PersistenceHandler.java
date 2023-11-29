//Todo: 직렬화 / 대상 : Game, Todo클래스, DefaultListModel,JList initializer참조 복원, 5초마다 자동 저장,


import org.apache.commons.csv.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersistenceHandler {
    private final MainFrame mainFrame;
    private static PersistenceHandler instance;

    private static final String GAME_FILE_PATH = "Game.csv";
    private static final String TODO_FILE_PATH = "Todo.csv";

    private PersistenceHandler(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public static PersistenceHandler getInstance(MainFrame mainFrame) {
        if (instance == null) {
            instance = new PersistenceHandler(mainFrame);
        }
        return instance;
    }

    private static CSVPrinter gameCSVPrinter() throws IOException {
        FileWriter fileWriter = new FileWriter(GAME_FILE_PATH);
        return new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Name", "ResetDayOfWeek", "ResetHour", "TodoIDs"));
    }

    public static void writeGamesToCSV(List<Game> games) {
        try (CSVPrinter csvPrinter = gameCSVPrinter()) {
            for (Game game : games) {
                csvPrinter.printRecord(game.getName(), game.getResetDoW(), game.getResetHour(), game.getTodoIDsAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo: readGamesFromCSV, parseGames try문 정리
    public static List<Game> readGamesFromCSV() {
        List<Game> games = new ArrayList<>();

        try (FileReader fileReader = new FileReader(GAME_FILE_PATH); CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {
            for (CSVRecord record : csvParser) {
                Game game = parseGames(record);
                games.add(game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return games;
    }

    private static Game parseGames(CSVRecord record) {
        String name = record.get("Name");
        DayOfWeek resetDoW = DayOfWeek.valueOf(record.get("ResetDayOfWeek"));
        int resetHour = Integer.parseInt(record.get("ResetHour"));
        ArrayList<UUID> todoIDs = parseTodoIDs(record.get("TodoIDs"));
        return new Game(name, resetDoW, resetHour, todoIDs);
    }

    private static ArrayList<UUID> parseTodoIDs(String string) {

        ArrayList<UUID> todoIDs = new ArrayList<>();

        if (string != null && !string.isEmpty()) {
            String[] idStrings = string.split(",");

            for (String idString : idStrings) {
                UUID todoID = UUID.fromString(idString.trim());
                todoIDs.add(todoID);
            }
        }
        return todoIDs;
    }

    private static CSVPrinter todoCSVPrinter() throws IOException {
        FileWriter fileWriter = new FileWriter(TODO_FILE_PATH);
        return new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Name", "GameName", "detail", "goal", "count", "recentReset", "resetType", "todoID"));
    }

    public static void writeTodosToCSV(List<Todo> todos) {
        try (CSVPrinter csvPrinter = todoCSVPrinter()) {
            for (Todo todo : todos) {
                csvPrinter.printRecord(todo.getName(), todo.getGameName(), todo.getDetail(), todo.getGoal(), todo.getCount(), todo.getRecentReset(), todo.getResetType(), todo.getTodoID());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo: readGamesFromCSV, parseGames try문 정리
    public static List<Todo> readTodosFromCSV() {
        List<Todo> todos = new ArrayList<>();

        try (FileReader fileReader = new FileReader(TODO_FILE_PATH); CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {
            for (CSVRecord record : csvParser) {
                Todo todo = parseTodos(record);
                todos.add(todo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return todos;
    }

    private static Todo parseTodos(CSVRecord record) {
        String name = record.get("Name");
        String gameName = record.get("GameName");
        String detail = record.get("detail");
        int goal = Integer.parseInt(record.get("goal"));
        int count = Integer.parseInt(record.get("count"));
        LocalDate recentReset = LocalDate.parse(record.get("recentReset"));
        Todo.ResetType resetType = Todo.ResetType.valueOf(record.get("resetType"));
        UUID todoID = parseTodoID(record.get("todoID"));
        return new Todo(name, gameName, detail, goal, count, recentReset, resetType, todoID);
    }

    private static UUID parseTodoID(String string) {
        return UUID.fromString(string.trim());
    }

    public void saveItems(List<Game> games, List<Todo> todos) {

        writeGamesToCSV(games);
        List<Game> integrityCheckGame = readGamesFromCSV(); //Todo: 나중에 tmp파일을 만들어 예외발생시 롤백하도록 구현
        writeTodosToCSV(todos);
        List<Todo> integrityCheckTodo = readTodosFromCSV();
    }

    public void loadItems() { //Todo:메인프레임에 추가하기 전 match 작업
        if (Files.exists(Path.of(GAME_FILE_PATH))) {
            List<Game> games = readGamesFromCSV();
            for (Game game : games) {
                mainFrame.addGame(game);
            }
        }
        if (Files.exists(Path.of(TODO_FILE_PATH))) {
            List<Todo> todos = readTodosFromCSV();
            for (Todo todo : todos) {
                mainFrame.addTodo(todo);
            }
        }
    }
}
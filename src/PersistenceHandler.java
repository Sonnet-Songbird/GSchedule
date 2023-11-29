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
import java.util.*;

public class PersistenceHandler {
    private final MainFrame mainFrame;
    private static PersistenceHandler instance;

    private static final String GAME_FILE_PATH = "Game.csv";
    private static final String TODO_FILE_PATH = "Todo.csv";
    private final boolean GAME_FILE_EXIST;
    private final boolean TODO_FILE_EXIST;

    private PersistenceHandler(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        GAME_FILE_EXIST = Files.exists(Path.of(GAME_FILE_PATH));
        TODO_FILE_EXIST = Files.exists(Path.of(TODO_FILE_PATH));
    }

    public static PersistenceHandler getInstance(MainFrame mainFrame) {
        if (instance == null) {
            instance = new PersistenceHandler(mainFrame);
        }
        return instance;
    }

    private CSVPrinter gameCSVPrinter() throws IOException {
        FileWriter fileWriter = new FileWriter(GAME_FILE_PATH);
        return new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Name", "ResetDayOfWeek", "ResetHour", "TodoIDs"));
    }

    public void writeGamesToCSV(List<Game> games) {
        try (CSVPrinter csvPrinter = gameCSVPrinter()) {
            for (Game game : games) {
                csvPrinter.printRecord(game.getName(), game.getResetDoW(), game.getResetHour(), game.getTodoIDsAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Todo: 나중에 tmp파일을 만들어 예외발생시 예전 파일로 롤백하도록 구현
    }

    //todo: readGamesFromCSV, parseGames try문 정리
    public Map<String, Game> readGamesFromCSV() {
        Map<String, Game> gameMap = new HashMap<>();

        try (FileReader fileReader = new FileReader(GAME_FILE_PATH); CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {
            for (CSVRecord record : csvParser) {
                Game game = parseGames(record);
                gameMap.put(game.getName(), game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameMap;
    }

    private Game parseGames(CSVRecord record) {
        ArrayList<UUID> todoIDs;
        String name = record.get("Name");
        DayOfWeek resetDoW = DayOfWeek.valueOf(record.get("ResetDayOfWeek"));
        int resetHour = Integer.parseInt(record.get("ResetHour"));
        if (TODO_FILE_EXIST) // TODO를 불러오지 못할 경우 쓰레기 값을 생성하지 않음.
            todoIDs = parseTodoIDs(record.get("TodoIDs"));
        else todoIDs = null;
        return new Game(name, resetDoW, resetHour, todoIDs);
    }

    private ArrayList<UUID> parseTodoIDs(String string) {

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

    private CSVPrinter todoCSVPrinter() throws IOException {
        FileWriter fileWriter = new FileWriter(TODO_FILE_PATH);
        return new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Name", "GameName", "detail", "goal", "count", "recentReset", "resetType", "todoID"));
    }

    public void writeTodosToCSV(List<Todo> todos) {
        try (CSVPrinter csvPrinter = todoCSVPrinter()) {
            for (Todo todo : todos) {
                csvPrinter.printRecord(todo.getName(), todo.getGameName(), todo.getDetail(), todo.getGoal(), todo.getCount(), todo.getRecentReset(), todo.getResetType(), todo.getTodoID());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo: readGamesFromCSV, parseGames try문 정리
    public Map<UUID, Todo> readTodosFromCSV() {
        Map<UUID, Todo> todoMap = new HashMap<>();

        try (FileReader fileReader = new FileReader(TODO_FILE_PATH); CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {
            for (CSVRecord record : csvParser) {
                Todo todo = parseTodos(record);
                todoMap.put(todo.getTodoID(), todo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return todoMap;
    }

    private Todo parseTodos(CSVRecord record) {
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

    private UUID parseTodoID(String string) {
        return UUID.fromString(string.trim());
    }

    public void saveItems(List<Game> games, List<Todo> todos) {

        writeGamesToCSV(games);
        writeTodosToCSV(todos);
    }

    public void loadItems() { //Todo:메인프레임에 추가하기 전 match 작업
        List<Game> games;
        Map<String, Game> gameMap;
        List<Todo> todos;
        Map<UUID, Todo> todoMap;
        if (GAME_FILE_EXIST) {
            gameMap = readGamesFromCSV();
            games = new ArrayList<>(gameMap.values());
        } else return;
        if (TODO_FILE_EXIST) {
            todoMap = readTodosFromCSV();
            todos = new ArrayList<>(todoMap.values());
        } else return;

        if (!games.isEmpty() && !todos.isEmpty()) {
            for (Todo todo : todos) {
                Game game = gameMap.get(todo.getGameName());
                todo.setGame(game);
                mainFrame.addTodo(todo);
            }
            for (Game game : games) {
                ArrayList<UUID> todoIDs = game.getTodoIDs();
                game.resetIDs();
                for (UUID todoID : todoIDs) {
                    Todo todo = todoMap.get(todoID);
                    game.register(todo);
                }
                mainFrame.addGame(game);
            }
        }

    }
}

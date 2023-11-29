//Todo: 직렬화 / 대상 : Game, Todo클래스, DefaultListModel,JList initializer참조 복원, 5초마다 자동 저장,


import org.apache.commons.csv.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersistenceHandler {
    private final MainFrame mainFrame;
    private static PersistenceHandler instance;

    private static final String GAME_FILE_PATH = "Game.csv";

    private PersistenceHandler(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    public static PersistenceHandler getInstance(MainFrame mainFrame){
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


    public static List<Game> readGamesFromCSV() {
        List<Game> games = new ArrayList<>();

        try (FileReader fileReader = new FileReader(GAME_FILE_PATH);
             CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {
            for (CSVRecord record : csvParser) {
                String name = record.get("Name");
                DayOfWeek resetDoW = DayOfWeek.valueOf(record.get("ResetDayOfWeek"));
                int resetHour = Integer.parseInt(record.get("ResetHour"));

                System.out.println(record.get("TodoIDs"));
                ArrayList<UUID> todoIDs = parseTodoIDs(record.get("TodoIDs"));
                Game game = new Game(name, resetDoW, resetHour, todoIDs);
                games.add(game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return games;
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

    public void saveGames(List<Game> games) {

        writeGamesToCSV(games);
        List<Game> integrityCheck = readGamesFromCSV(); //Todo: 나중에 tmp파일을 만들어 예외발생시 롤백하도록 구현
    }

    public void loadGames() {
        List<Game> games = readGamesFromCSV();
        for (Game game : games) {
            mainFrame.addGame(game);
        }
    }
}
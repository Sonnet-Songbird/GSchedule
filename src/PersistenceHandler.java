
//Todo: 직렬화 / 대상 : Game, Todo클래스, DefaultListModel,JList initializer참조 복원, 5초마다 자동 저장,


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersistenceHandler {

    private static final String CSV_FILE_PATH = "Game.csv";

    public static void writeGamesToCSV(List<Game> games) {
        try (CSVPrinter csvPrinter = createCSVPrinter()) {
            for (Game game : games) {
                csvPrinter.printRecord(
                        game.getName(),
                        game.getResetDoW(),
                        game.getResetHour(),
                        game.getTodoIDsAsString()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CSVPrinter createCSVPrinter() throws IOException {
        FileWriter fileWriter = new FileWriter(CSV_FILE_PATH);
        return new CSVPrinter(fileWriter, CSVFormat.DEFAULT
                .withHeader("Name", "ResetDayOfWeek", "ResetHour", "TodoID"));
    }

    public static List<Game> readGamesFromCSV() {
        List<Game> games = new ArrayList<>();

        try (FileReader fileReader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader)) {

            for (CSVRecord record : csvParser) {
                String name = record.get("Name");
                DayOfWeek resetDoW = DayOfWeek.valueOf(record.get("ResetDayOfWeek"));
                int resetHour = Integer.parseInt(record.get("ResetHour"));

                ArrayList<UUID> todoID = parseTodoIDs(record.get("TodoID"));

                Game game = new Game(name, resetDoW, resetHour, todoID);
                games.add(game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return games;
    }

    private static ArrayList<UUID> parseTodoIDs(String todoIDString) {
        // Todo ID를 문자열에서 파싱하는 로직을 구현
        return new ArrayList<UUID>();
    }

    public void saveGames(List<Game> games) {

        writeGamesToCSV(games);

        List<Game> readGames = readGamesFromCSV();
    }
}
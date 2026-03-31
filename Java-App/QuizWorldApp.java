import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class QuizWorldApp {
    public static void main(String[] args) {
        Path workspaceRoot = Paths.get("").toAbsolutePath().normalize();
        Path excelPath = workspaceRoot.resolve("Länder-Fakten.xlsx");
        Path countriesPath = workspaceRoot.resolve("Länder");

        DataManager dataManager = new DataManager(excelPath, countriesPath);
        if (!dataManager.verifyDataFiles()) {
            System.out.println("Fehler: Die benötigten Daten wurden nicht gefunden.");
            System.out.println("Bitte stelle sicher, dass 'Länder-Fakten.xlsx' und der Ordner 'Länder' im Projektverzeichnis vorhanden sind.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Quiz-World App Grundgerüst ===");
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("Wähle einen Modus:");
                System.out.println("1 - Daten ansehen");
                System.out.println("2 - Bildquiz (verdeckte Karte)");
                System.out.println("3 - Faktenquiz");
                System.out.println("0 - Beenden");
                System.out.print("Eingabe: ");
                String input = scanner.nextLine().trim();

                switch (input) {
                    case "1" -> runViewMode(scanner, dataManager);
                    case "2" -> runImageQuizMode(scanner, dataManager);
                    case "3" -> runFactQuizMode(scanner, dataManager);
                    case "0" -> {
                        running = false;
                        System.out.println("Programm beendet.");
                    }
                    default -> System.out.println("Ungültige Auswahl. Bitte 0-3 eingeben.");
                }
            }
        }
    }

    private static int readRoundCount(Scanner scanner) {
        System.out.print("Wie viele Länder sollen in einer Runde geprüft werden? ");
        String countInput = scanner.nextLine().trim();
        try {
            int count = Integer.parseInt(countInput);
            return Math.max(1, count);
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Zahl. Es wird 1 verwendet.");
            return 1;
        }
    }

    private static void runViewMode(Scanner scanner, DataManager dataManager) {
        int rounds = readRoundCount(scanner);
        dataManager.startViewMode(scanner, rounds);
    }

    private static void runImageQuizMode(Scanner scanner, DataManager dataManager) {
        int rounds = readRoundCount(scanner);
        dataManager.startImageQuizMode(scanner, rounds);
    }

    private static void runFactQuizMode(Scanner scanner, DataManager dataManager) {
        int rounds = readRoundCount(scanner);
        dataManager.startFactQuizMode(scanner, rounds);
    }
}


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataManager {
    private final Path excelFile;
    private final Path countriesDir;

    public DataManager(Path excelFile, Path countriesDir) {
        this.excelFile = excelFile;
        this.countriesDir = countriesDir;
    }

    public boolean verifyDataFiles() {
        return Files.exists(excelFile) && Files.isRegularFile(excelFile)
                && Files.exists(countriesDir) && Files.isDirectory(countriesDir);
    }

    public List<String> listCountryNames() {
        List<String> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(countriesDir)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    result.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen des Länderordners: " + e.getMessage());
        }
        return result;
    }

    public CountryInfo loadCountryInfo(String countryName) {
        CountryInfo info = new CountryInfo(countryName);
        Path countryFolder = countriesDir.resolve(countryName);
        if (!Files.isDirectory(countryFolder)) {
            return null;
        }

        info.setOutlineImage(findImagePath(countryFolder, "Umriss"));
        info.setLocationImage(findImagePath(countryFolder, "Lage"));
        info.setFlagImage(findImagePath(countryFolder, "Flagge"));

        info.addAttribute("Excel-Datei", excelFile.toString());
        info.addAttribute("Hinweis", "Excel-Daten werden später mit Apache POI geladen.");
        return info;
    }

    private Path findImagePath(Path countryFolder, String categoryName) {
        Path candidate = countryFolder.resolve(categoryName);
        if (Files.exists(candidate)) {
            return candidate;
        }
        return null;
    }

    public void startViewMode(Scanner scanner, int roundCount) {
        System.out.println("=== Daten ansehen ===");
        for (int i = 0; i < roundCount; i++) {
            System.out.print("Land eingeben: ");
            String country = scanner.nextLine().trim();
            CountryInfo info = loadCountryInfo(country);
            if (info == null) {
                System.out.println("Land nicht gefunden: " + country);
                continue;
            }
            printCountryInfo(info);
        }
    }

    public void startImageQuizMode(Scanner scanner, int roundCount) {
        System.out.println("=== Bildquiz (verdeckte Karten) ===");
        System.out.println("Hinweis: Verdeckung und Bildanzeige werden hier als Platzhalter implementiert.");
        System.out.print("Kategorie wählen (Umriss/Lage/Flagge): ");
        String category = scanner.nextLine().trim();
        System.out.print("Verdeckungsgrad wählen (z.B. 25, 50, 75): ");
        String cover = scanner.nextLine().trim();

        List<String> names = listCountryNames();
        for (int i = 0; i < roundCount && i < names.size(); i++) {
            String country = names.get(i);
            System.out.println("Runde " + (i + 1) + ": Bitte errate das Land anhand eines verdeckten " + category + "-Bildes.");
            System.out.println("Verdeckungsgrad: " + cover + "%");
            System.out.print("Deine Antwort: ");
            String answer = scanner.nextLine().trim();
            if (country.equalsIgnoreCase(answer)) {
                System.out.println("Richtig!");
            } else {
                System.out.println("Falsch. Richtige Antwort: " + country);
            }
        }
    }

    public void startFactQuizMode(Scanner scanner, int roundCount) {
        System.out.println("=== Faktenquiz ===");
        System.out.println("Wähle eine Kategorie oder 'zufall': Sprache, Hauptstadt, Einwohnerzahl, Fläche, Währung, BIP.");
        System.out.print("Kategorie: ");
        String category = scanner.nextLine().trim();

        List<String> names = listCountryNames();
        for (int i = 0; i < roundCount && i < names.size(); i++) {
            String country = names.get(i);
            System.out.println("Runde " + (i + 1) + ": Frage für " + country + " (Kategorie: " + category + ")");
            System.out.print("Deine Antwort: ");
            scanner.nextLine();
            System.out.println("Antwort wird später geprüft.");
        }
    }

    private void printCountryInfo(CountryInfo info) {
        System.out.println("--- Informationen für " + info.getName() + " ---");
        info.getAttributes().forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("Umriss: " + displayPath(info.getOutlineImage()));
        System.out.println("Lage: " + displayPath(info.getLocationImage()));
        System.out.println("Flagge: " + displayPath(info.getFlagImage()));
        System.out.println("------------------------------");
    }

    private String displayPath(Path path) {
        return path != null ? path.toString() : "Nicht gefunden";
    }
}

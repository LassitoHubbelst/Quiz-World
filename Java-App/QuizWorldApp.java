import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class QuizWorldApp {
    public static void main(String[] args) {
        Path workspaceRoot = Paths.get("").toAbsolutePath().normalize();
        Path excelPath = workspaceRoot.resolve("Länder-Fakten.xlsx");
        Path countriesPath = workspaceRoot.resolve("Länder");

        DataManager dataManager = new DataManager(excelPath, countriesPath);
        if (!dataManager.verifyDataFiles()) {
            JOptionPane.showMessageDialog(null, "Fehler: Die benötigten Daten wurden nicht gefunden.\nBitte stelle sicher, dass der Ordner 'Länder' im Projektverzeichnis vorhanden ist.\nDie Excel-Datei wird später geladen.");
            return;
        }

        // Starte die GUI
        SwingUtilities.invokeLater(() -> new QuizWorldGUI(dataManager));
    }
}


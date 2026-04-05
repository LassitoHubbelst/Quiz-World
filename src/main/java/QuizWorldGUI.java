import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class QuizWorldGUI extends JFrame {
    private DataManager dataManager;

    public QuizWorldGUI(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Quiz-World App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("=== Quiz-World App ===", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        JButton viewDataButton = new JButton("Daten ansehen");
        viewDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewModeDialog();
            }
        });
        panel.add(viewDataButton);

        JButton imageQuizButton = new JButton("Bildquiz (verdeckte Karte)");
        imageQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openImageQuizDialog();
            }
        });
        panel.add(imageQuizButton);

        JButton factQuizButton = new JButton("Faktenquiz");
        factQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFactQuizDialog();
            }
        });
        panel.add(factQuizButton);

        add(panel);
        setVisible(true);
    }

    private void openViewModeDialog() {
        Map<String, Map<String, String>> allData = dataManager.getAllExcelData();
        if (allData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Excel-Daten gefunden.");
            return;
        }

        StringBuilder sb = new StringBuilder("=== Alle Länder-Daten ===\n\n");
        for (Map.Entry<String, Map<String, String>> entry : allData.entrySet()) {
            sb.append("Land: ").append(entry.getKey()).append("\n");
            for (Map.Entry<String, String> fact : entry.getValue().entrySet()) {
                sb.append("  ").append(fact.getKey()).append(": ").append(fact.getValue()).append("\n");
            }
            sb.append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Daten ansehen", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openImageQuizDialog() {
        String roundCountStr = JOptionPane.showInputDialog(this, "Wie viele Länder sollen in einer Runde geprüft werden?", "1");
        if (roundCountStr == null) return;
        int roundCount;
        try {
            roundCount = Integer.parseInt(roundCountStr.trim());
            roundCount = Math.max(1, roundCount);
        } catch (NumberFormatException e) {
            roundCount = 1;
        }

        String category = JOptionPane.showInputDialog(this, "Kategorie wählen (Umriss/Lage/Flagge):", "Umriss");
        if (category == null) return;
        String cover = JOptionPane.showInputDialog(this, "Verdeckungsgrad wählen (z.B. 25, 50, 75):", "50");
        if (cover == null) return;

        List<String> names = dataManager.listCountryNames();
        for (int i = 0; i < roundCount && i < names.size(); i++) {
            String country = names.get(i);
            String answer = JOptionPane.showInputDialog(this, "Runde " + (i + 1) + ": Bitte errate das Land anhand eines verdeckten " + category + "-Bildes.\nVerdeckungsgrad: " + cover + "%\nDeine Antwort:");
            if (answer == null) break;
            if (country.equalsIgnoreCase(answer.trim())) {
                JOptionPane.showMessageDialog(this, "Richtig!");
            } else {
                JOptionPane.showMessageDialog(this, "Falsch. Richtige Antwort: " + country);
            }
        }
    }

    private void openFactQuizDialog() {
        String roundCountStr = JOptionPane.showInputDialog(this, "Wie viele Länder sollen in einer Runde geprüft werden?", "1");
        if (roundCountStr == null) return;
        int roundCount;
        try {
            roundCount = Integer.parseInt(roundCountStr.trim());
            roundCount = Math.max(1, roundCount);
        } catch (NumberFormatException e) {
            roundCount = 1;
        }

        String category = JOptionPane.showInputDialog(this, "Kategorie wählen (Sprache, Hauptstadt, Einwohnerzahl, Fläche, Währung, BIP oder 'zufall'):", "zufall");
        if (category == null) return;

        List<String> names = dataManager.listCountryNames();
        for (int i = 0; i < roundCount && i < names.size(); i++) {
            String country = names.get(i);
            String answer = JOptionPane.showInputDialog(this, "Runde " + (i + 1) + ": Frage für " + country + " (Kategorie: " + category + ")\nDeine Antwort:");
            if (answer == null) break;
            JOptionPane.showMessageDialog(this, "Antwort wird später geprüft.");
        }
    }

    private void showCountryInfo(CountryInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Informationen für ").append(info.getName()).append(" ---\n");
        info.getAttributes().forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
        sb.append("Umriss: ").append(info.getOutlineImage() != null ? info.getOutlineImage().toString() : "Nicht gefunden").append("\n");
        sb.append("Lage: ").append(info.getLocationImage() != null ? info.getLocationImage().toString() : "Nicht gefunden").append("\n");
        sb.append("Flagge: ").append(info.getFlagImage() != null ? info.getFlagImage().toString() : "Nicht gefunden").append("\n");
        sb.append("------------------------------");

        JOptionPane.showMessageDialog(this, sb.toString());
    }
}
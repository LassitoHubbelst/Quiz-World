import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuizWorldGUI extends JFrame {
    private DataManager dataManager;
    private CountryRepository repository;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private static final String MAIN_MENU = "MainMenu";
    private static final String VIEW_DATA = "ViewData";
    private static final String IMAGE_QUIZ = "ImageQuiz";
    private static final String FACT_QUIZ = "FactQuiz";

    public QuizWorldGUI(DataManager dataManager) {
        this.dataManager = dataManager;
        this.repository = dataManager.getRepository();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Quiz-World App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // CardLayout für Panel-Wechsel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panels erstellen und hinzufügen
        cardPanel.add(createMainMenuPanel(), MAIN_MENU);
        cardPanel.add(createViewDataPanel(), VIEW_DATA);
        cardPanel.add(createImageQuizPanel(), IMAGE_QUIZ);
        cardPanel.add(createFactQuizPanel(), FACT_QUIZ);

        add(cardPanel);
        cardLayout.show(cardPanel, MAIN_MENU);
        setVisible(true);
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("=== Quiz-World App ===", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        JButton viewDataButton = new JButton("Daten ansehen");
        viewDataButton.addActionListener(e -> cardLayout.show(cardPanel, VIEW_DATA));
        panel.add(viewDataButton);

        JButton imageQuizButton = new JButton("Bildquiz (verdeckte Karte)");
        imageQuizButton.addActionListener(e -> cardLayout.show(cardPanel, IMAGE_QUIZ));
        panel.add(imageQuizButton);

        JButton factQuizButton = new JButton("Faktenquiz");
        factQuizButton.addActionListener(e -> cardLayout.show(cardPanel, FACT_QUIZ));
        panel.add(factQuizButton);

        return panel;
    }

    private JPanel createViewDataPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        CountryRepository repository = dataManager.getRepository();
        
        // NORD: Suchzeile
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.add(new JLabel("Suchfeld:"), BorderLayout.WEST);
        JTextField searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.NORTH);

        // MITTE: Split-Panel mit Liste und Details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // LINKS: Länderliste
        DefaultListModel<String> countryListModel = new DefaultListModel<>();
        List<String> allCountries = repository.getAllCountryNames();
        for (String country : allCountries) {
            countryListModel.addElement(country);
        }

        JList<String> countryList = new JList<>(countryListModel);
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(countryList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Länder"));
        splitPane.setLeftComponent(listScrollPane);

        // RECHTS: Details-Bereich
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Details"));
        splitPane.setRightComponent(detailsScrollPane);

        splitPane.setVisible(true);

        // Event-Listener: Klick auf Land zeigt Details
        countryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCountry = countryList.getSelectedValue();
                if (selectedCountry != null) {
                    Country country = repository.getCountry(selectedCountry);
                    if (country != null) {
                        detailsArea.setText(country.getFormattedInfo());
                        detailsArea.setCaretPosition(0);
                    }
                }
            }
        });

        // Event-Listener: Suchfunktion
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String searchText = searchField.getText().toLowerCase().trim();
                countryListModel.clear();

                if (searchText.isEmpty()) {
                    // Zeige alle, wenn Suchfeld leer
                    for (String country : allCountries) {
                        countryListModel.addElement(country);
                    }
                } else {
                    // Filtere nach Suchtext
                    for (String country : allCountries) {
                        if (country.toLowerCase().contains(searchText)) {
                            countryListModel.addElement(country);
                        }
                    }
                }

                // Automatisch erstes Ergebnis wählen
                if (countryListModel.size() > 0) {
                    countryList.setSelectedIndex(0);
                } else {
                    detailsArea.setText("Keine Länder gefunden.");
                }
            }
        });

        panel.add(splitPane, BorderLayout.CENTER);

        // SÜDEN: Zurück-Button
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, MAIN_MENU));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createImageQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Bildquiz - verdeckte Karte", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Eingabefelder
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Anzahl der Länder:"));
        JTextField roundCountField = new JTextField("1");
        inputPanel.add(roundCountField);

        inputPanel.add(new JLabel("Kategorie (Umriss/Lage/Flagge):"));
        JTextField categoryField = new JTextField("Umriss");
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Verdeckungsgrad (25/50/75):"));
        JTextField coverField = new JTextField("50");
        inputPanel.add(coverField);

        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton startButton = new JButton("Quiz starten");
        startButton.addActionListener(e -> {
            String roundCountStr = roundCountField.getText().trim();
            int roundCount;
            try {
                roundCount = Integer.parseInt(roundCountStr);
                roundCount = Math.max(1, roundCount);
            } catch (NumberFormatException ex) {
                roundCount = 1;
            }

            String category = categoryField.getText().trim();
            String cover = coverField.getText().trim();

            List<String> names = dataManager.listCountryNames();
            StringBuilder results = new StringBuilder();
            for (int i = 0; i < roundCount && i < names.size(); i++) {
                String country = names.get(i);
                String answer = JOptionPane.showInputDialog(QuizWorldGUI.this,
                        "Runde " + (i + 1) + ": Bitte errate das Land anhand eines verdeckten " + category + "-Bildes.\nVerdeckungsgrad: " + cover + "%\nDeine Antwort:");
                if (answer == null) break;
                if (country.equalsIgnoreCase(answer.trim())) {
                    results.append("Runde ").append(i + 1).append(": ✓ Richtig!\n");
                } else {
                    results.append("Runde ").append(i + 1).append(": ✗ Falsch. Richtige Antwort: ").append(country).append("\n");
                }
            }
            resultArea.setText(results.toString());
        });

        panel.add(inputPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(startButton);
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, MAIN_MENU));
        buttonPanel.add(backButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFactQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Faktenquiz", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Eingabefelder
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Anzahl der Länder:"));
        JTextField roundCountField = new JTextField("1");
        inputPanel.add(roundCountField);

        inputPanel.add(new JLabel("Kategorie (Sprache/Hauptstadt/Einwohnerzahl/Fläche/Währung/BIP/zufall):"));
        JTextField categoryField = new JTextField("zufall");
        inputPanel.add(categoryField);

        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton startButton = new JButton("Quiz starten");
        startButton.addActionListener(e -> {
            String roundCountStr = roundCountField.getText().trim();
            int roundCount;
            try {
                roundCount = Integer.parseInt(roundCountStr);
                roundCount = Math.max(1, roundCount);
            } catch (NumberFormatException ex) {
                roundCount = 1;
            }

            String category = categoryField.getText().trim();

            List<String> names = dataManager.listCountryNames();
            StringBuilder results = new StringBuilder();
            for (int i = 0; i < roundCount && i < names.size(); i++) {
                String country = names.get(i);
                String answer = JOptionPane.showInputDialog(QuizWorldGUI.this,
                        "Runde " + (i + 1) + ": Frage für " + country + " (Kategorie: " + category + ")\nDeine Antwort:");
                if (answer == null) break;
                results.append("Runde ").append(i + 1).append(": ").append(country).append(" - Antwort wird später geprüft.\n");
            }
            resultArea.setText(results.toString());
        });

        panel.add(inputPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(startButton);
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, MAIN_MENU));
        buttonPanel.add(backButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

}
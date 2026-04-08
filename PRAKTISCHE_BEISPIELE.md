# Praktische Beispiele - Neue Country/Category Struktur

## Beispiel 1: GUI-Integration mit der neuen Struktur

```java
public class QuizWorldGUI extends JFrame {
    private DataManager dataManager;
    private CountryRepository repository;

    public QuizWorldGUI(DataManager dataManager) {
        this.dataManager = dataManager;
        this.repository = dataManager.getRepository();  // Nutze das Repository!
        initializeUI();
    }

    // Quiz basierend auf Kategorien erstellen
    private void runQuizByCategory(Category category) {
        List<Country> countries = new ArrayList<>(repository.getAllCountries());
        
        for (Country country : countries) {
            if (category.isImageCategory()) {
                // Bild-Quiz
                Path imagePath = country.getImage(category.getName());
                if (imagePath != null) {
                    String answer = JOptionPane.showInputDialog(
                        "Erkenne das Land:\n" + category.getDescription()
                    );
                    // ... Validierung
                }
            } else {
                // Daten-Quiz
                String hint = country.getAttribute(category.getName());
                String answer = JOptionPane.showInputDialog(
                    category.getName() + ": " + hint
                );
                // ... Validierung
            }
        }
    }
}
```

---

## Beispiel 2: Ein Quiz erstellen, das automatisch alle Kategorien nutzt

```java
public class SmartQuiz {
    private CountryRepository repository;

    public SmartQuiz(CountryRepository repository) {
        this.repository = repository;
    }

    /**
     * Generisches Quiz für beliebige Kategorien
     */
    public void runUniversalQuiz(List<Category> categoriesToUse, int roundCount) {
        List<Country> countries = new ArrayList<>();
        repository.getAllCountries().stream()
            .limit(roundCount)
            .forEach(countries::add);

        int score = 0;
        for (Country country : countries) {
            // Zufällige Kategorie wählen
            Category category = categoriesToUse.get(
                new Random().nextInt(categoriesToUse.size())
            );

            if (category.isImageCategory()) {
                score += askImageQuestion(country, category);
            } else {
                score += askDataQuestion(country, category);
            }
        }

        System.out.println("Endergebnis: " + score + "/" + countries.size());
    }

    private int askImageQuestion(Country country, Category category) {
        Path imagePath = country.getImage(category.getName());
        if (imagePath == null) return 0;

        String answer = JOptionPane.showInputDialog(
            "Erraten Sie das Land basierend auf: " + category.getDescription()
        );

        return country.getName().equalsIgnoreCase(answer) ? 1 : 0;
    }

    private int askDataQuestion(Country country, Category category) {
        String question = country.getAttribute(category.getName());
        if (question == null) return 0;

        String answer = JOptionPane.showInputDialog(
            "Frage: " + category.getDescription() + "\nHinweis: " + question
        );

        return country.getName().equalsIgnoreCase(answer) ? 1 : 0;
    }
}
```

---

## Beispiel 3: Neue Kategorien dynamisch hinzufügen

```java
public class CategoryManager {
    private CountryRepository repository;

    public void addCustomCategories() {
        // Wirtschafts-Kategorien
        repository.addCategory(new Category(
            "Ressourcen",
            "Wichtige Rohstoffe des Landes",
            "Ressourcen"
        ));

        // Weitere Kategorien
        repository.addCategory(new Category(
            "Bordering-Countries",
            "Nachbarländer",
            "Nachbarn"
        ));

        // Kultur-Kategorien
        repository.addCategory(new Category(
            "Musik",
            "Traditionelle Musik",
            "Musik"
        ));
    }

    /**
     * Dynamisches Quiz basierend auf verfügbaren Kategorien
     */
    public void runCategoryBasedQuiz() {
        System.out.println("Verfügbare Daten-Kategorien:");
        List<Category> dataCategories = repository.getDataCategories();
        
        for (int i = 0; i < dataCategories.size(); i++) {
            Category cat = dataCategories.get(i);
            System.out.println((i+1) + ". " + cat.getName() 
                             + " - " + cat.getDescription());
        }

        // Quiz starten...
    }
}
```

---

## Beispiel 4: Länder-Vergleich mit Kategorien

```java
public class CountryComparison {
    private CountryRepository repository;

    /**
     * Vergleicht zwei Länder basierend auf einer Kategorie
     */
    public void compareCountries(String country1Name, String country2Name, 
                                 String categoryName) {
        Country c1 = repository.getCountry(country1Name);
        Country c2 = repository.getCountry(country2Name);

        if (c1 == null || c2 == null) {
            System.out.println("Ein oder beide Länder nicht gefunden!");
            return;
        }

        String val1 = c1.getAttribute(categoryName);
        String val2 = c2.getAttribute(categoryName);

        System.out.println("=== Vergleich: " + categoryName + " ===");
        System.out.println(country1Name + ": " + val1);
        System.out.println(country2Name + ": " + val2);
    }

    /**
     * Rang-Liste aller Länder nach einer Kategorie (z.B. Bevölkerung)
     */
    public void rankCountriesByCategory(String categoryName) {
        List<Country> countries = new ArrayList<>(repository.getAllCountries());

        // Sortiere nach dem Attribut (erwartet numerische Werte)
        countries.sort((c1, c2) -> {
            String v1 = c1.getAttribute(categoryName);
            String v2 = c2.getAttribute(categoryName);

            if (v1 == null || v2 == null) return 0;

            try {
                return Double.compare(
                    Double.parseDouble(v2),  // Absteigende Reihenfolge
                    Double.parseDouble(v1)
                );
            } catch (NumberFormatException e) {
                return v2.compareTo(v1);     // Text-Vergleich
            }
        });

        System.out.println("=== Top 10 nach " + categoryName + " ===");
        countries.stream()
            .limit(10)
            .forEach(c -> System.out.println(
                c.getName() + ": " + c.getAttribute(categoryName)
            ));
    }
}
```

---

## Beispiel 5: Länder mit nur bestimmten Kategorien laden

```java
public class SelectiveQuiz {
    private CountryRepository repository;

    /**
     * Quiz mit nur ausgewählten Kategorien
     */
    public void runSelectiveQuiz(List<String> selectedCategoryNames) {
        List<Category> selectedCategories = new ArrayList<>();

        for (String name : selectedCategoryNames) {
            Category cat = repository.getCategoryByName(name);
            if (cat != null) {
                selectedCategories.add(cat);
            }
        }

        if (selectedCategories.isEmpty()) {
            System.out.println("Keine gültigen Kategorien ausgewählt!");
            return;
        }

        System.out.println("Quiz mit Kategorien:");
        selectedCategories.forEach(c -> 
            System.out.println("  - " + c.getName() + ": " + c.getDescription())
        );

        // Quiz-Logik...
    }
}
```

---

## Beispiel 6: Statistiken und Reports

```java
public class QuizStatistics {
    private CountryRepository repository;
    private Map<Country, Map<Category, Integer>> scores;

    public QuizStatistics(CountryRepository repository) {
        this.repository = repository;
        this.scores = new HashMap<>();
    }

    /**
     * Zeigt Übersicht des Repository
     */
    public void printRepositoryOverview() {
        System.out.println(repository.getStatistics());
        
        System.out.println("\n=== Alle Kategorien ===");
        for (Category cat : repository.getCategories()) {
            System.out.println("  " + cat.getName() 
                             + " (" + (cat.isImageCategory() ? "Bild" : "Text") + ")");
        }

        System.out.println("\n=== Länder-Vorschau ===");
        repository.getAllCountries().stream()
            .limit(5)
            .forEach(c -> System.out.println("  " + c.getName() 
                                           + " - " + c.getAttributes().size() + " Attribute"));
    }

    /**
     * Zeigt, welche Länder unvollständig sind
     */
    public void findIncompleteCountries() {
        List<Country> incomplete = new ArrayList<>();

        for (Country country : repository.getAllCountries()) {
            if (!country.isComplete()) {
                incomplete.add(country);
            }
        }

        System.out.println("Unvollständige Länder (" + incomplete.size() + "):");
        incomplete.forEach(c -> System.out.println("  " + c.getName() 
                                                 + " - " + c.getAttributes().size() + " Attribute"));
    }
}
```

---

## Schnelle Integration in bestehende Projekte

```java
// In QuizWorldApp.java:
public static void main(String[] args) {
    Path excelPath = Paths.get("Länder-Fakten.xlsx");
    Path countriesPath = Paths.get("Länder");

    DataManager dataManager = new DataManager(excelPath, countriesPath);
    
    // NEU! Direkter Zugriff auf strukturierte Daten
    CountryRepository repo = dataManager.getRepository();
    
    // Beispiel: Automatisches Quiz über alle Daten-Kategorien
    SmartQuiz quiz = new SmartQuiz(repo);
    quiz.runUniversalQuiz(repo.getDataCategories(), 5);
    
    // GUI starten
    SwingUtilities.invokeLater(() -> new QuizWorldGUI(dataManager));
}
```


import java.nio.file.Path;
import java.util.*;

/**
 * Repository-Muster für Länder-Verwaltung.
 * Zentraler Zugriffspunkt für alle Länder-Daten.
 */
public class CountryRepository {
    private final Map<String, Country> countries = new LinkedHashMap<>();
    private final List<Category> categories = new ArrayList<>();

    public CountryRepository() {
        // Initialiere Standard-Kategorien
        initializeStandardCategories();
    }

    /**
     * Initialisiert die Standard-Kategorien
     */
    private void initializeStandardCategories() {
        // Daten-Kategorien
        addCategory(new Category("Sprache", "Hauptsprache des Landes", "Sprache"));
        addCategory(new Category("Hauptstadt", "Die Hauptstadt des Landes", "Hauptstadt"));
        addCategory(new Category("Einwohnerzahl", "Bevölkerungszahl", "Einwohnerzahl"));
        addCategory(new Category("Fläche", "Landfläche in km²", "Fläche"));
        addCategory(new Category("Währung", "Offizielle Währung", "Währung"));
        addCategory(new Category("BIP", "Bruttoinlandsprodukt", "BIP"));

        // Bild-Kategorien
        addCategory(new Category("Umriss", "Landumriss", "Umriss", true));
        addCategory(new Category("Lage", "Geografische Lage", "Lage", true));
        addCategory(new Category("Flagge", "Landesflagge", "Flagge", true));
    }

    /**
     * Fügt eine neue Kategorie hinzu
     */
    public void addCategory(Category category) {
        if (category != null && !categories.contains(category)) {
            categories.add(category);
        }
    }

    /**
     * Gibt alle Kategorien zurück
     */
    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    /**
     * Gibt alle Daten-Kategorien zurück
     */
    public List<Category> getDataCategories() {
        List<Category> dataCategories = new ArrayList<>();
        for (Category cat : categories) {
            if (!cat.isImageCategory()) {
                dataCategories.add(cat);
            }
        }
        return dataCategories;
    }

    /**
     * Gibt alle Bild-Kategorien zurück
     */
    public List<Category> getImageCategories() {
        List<Category> imageCategories = new ArrayList<>();
        for (Category cat : categories) {
            if (cat.isImageCategory()) {
                imageCategories.add(cat);
            }
        }
        return imageCategories;
    }

    /**
     * Findet eine Kategorie nach Name
     */
    public Category getCategoryByName(String name) {
        if (name == null) return null;
        return categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Speichert ein Land im Repository
     */
    public void addCountry(Country country) {
        if (country != null && !country.getName().isEmpty()) {
            countries.put(country.getName(), country);
        }
    }

    /**
     * Holt ein Land nach Name
     */
    public Country getCountry(String name) {
        if (name == null) return null;
        return countries.get(name);
    }

    /**
     * Gibt alle Länder zurück
     */
    public Collection<Country> getAllCountries() {
        return new ArrayList<>(countries.values());
    }

    /**
     * Gibt alle Ländernamen zurück (sortiert)
     */
    public List<String> getAllCountryNames() {
        List<String> names = new ArrayList<>(countries.keySet());
        Collections.sort(names);
        return names;
    }

    /**
     * Prüft, ob ein Land existiert
     */
    public boolean countryExists(String name) {
        return name != null && countries.containsKey(name);
    }

    /**
     * Gibt die Anzahl der Länder zurück
     */
    public int getCountryCount() {
        return countries.size();
    }

    /**
     * Gibt die Anzahl der Kategorien zurück
     */
    public int getCategoryCount() {
        return categories.size();
    }

    /**
     * Löscht ein Land aus dem Repository
     */
    public void removeCountry(String name) {
        if (name != null) {
            countries.remove(name);
        }
    }

    /**
     * Löscht alle Länder
     */
    public void clearCountries() {
        countries.clear();
    }

    /**
     * Gibt Statistiken über die Länder zurück
     */
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Statistiken ===\n");
        sb.append("Anzahl Länder: ").append(countries.size()).append("\n");
        sb.append("Anzahl Kategorien: ").append(categories.size()).append("\n");
        sb.append("Daten-Kategorien: ").append(getDataCategories().size()).append("\n");
        sb.append("Bild-Kategorien: ").append(getImageCategories().size()).append("\n");
        return sb.toString();
    }
}

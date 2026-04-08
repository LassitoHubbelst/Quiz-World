import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Repräsentiert ein Land mit allen seinen Daten und Eigenschaften.
 * Diese Klasse kapselt alle Informationen über ein Land.
 */
public class Country {
    private final String name;
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private final Map<String, Path> images = new HashMap<>();
    private int population;
    private double area;
    private String capital;
    private String language;
    private String currency;
    private double gdp;

    public Country(String name) {
        this.name = Objects.requireNonNull(name, "Land-Name darf nicht null sein");
    }

    // ===== Basis-Accessoren =====
    public String getName() {
        return name;
    }

    // ===== Attribute Management =====
    public void addAttribute(String key, String value) {
        if (key != null && !key.isEmpty() && value != null) {
            attributes.put(key, value);
        }
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return new LinkedHashMap<>(attributes);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    // ===== Bilder Management =====
    /**
     * Setzt ein Bild für eine bestimmte Kategorie
     */
    public void setImage(String categoryName, Path imagePath) {
        if (categoryName != null && !categoryName.isEmpty() && imagePath != null) {
            images.put(categoryName, imagePath);
        }
    }

    /**
     * Holt ein Bild für eine bestimmte Kategorie
     */
    public Path getImage(String categoryName) {
        return images.get(categoryName);
    }

    public Map<String, Path> getImages() {
        return new HashMap<>(images);
    }

    public boolean hasImage(String categoryName) {
        return images.containsKey(categoryName);
    }

    // ===== Alte Bild-Methoden (für Rückwärtskompatibilität) =====
    @Deprecated
    public Path getOutlineImage() {
        return getImage("Umriss");
    }

    @Deprecated
    public void setOutlineImage(Path imagePath) {
        setImage("Umriss", imagePath);
    }

    @Deprecated
    public Path getLocationImage() {
        return getImage("Lage");
    }

    @Deprecated
    public void setLocationImage(Path imagePath) {
        setImage("Lage", imagePath);
    }

    @Deprecated
    public Path getFlagImage() {
        return getImage("Flagge");
    }

    @Deprecated
    public void setFlagImage(Path imagePath) {
        setImage("Flagge", imagePath);
    }

    // ===== Spezifische Daten-Accessoren =====
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getGdp() {
        return gdp;
    }

    public void setGdp(double gdp) {
        this.gdp = gdp;
    }

    // ===== Utility Methoden =====
    /**
     * Gibt eine formatierte Beschreibung des Landes aus
     */
    public String getFormattedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" ===\n");
        if (!attributes.isEmpty()) {
            attributes.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
        }
        if (!images.isEmpty()) {
            sb.append("\nBilder:\n");
            images.forEach((category, path) -> sb.append("  ").append(category).append(": ").append(path).append("\n"));
        }
        return sb.toString();
    }

    /**
     * Prüft, ob alle wesentlichen Daten vorhanden sind
     */
    public boolean isComplete() {
        return !name.isEmpty() && !attributes.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Country country = (Country) obj;
        return name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

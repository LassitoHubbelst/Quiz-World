/**
 * Repräsentiert eine Quiz-Kategorie (z.B. Sprache, Hauptstadt, etc.)
 * Dies ermöglicht einfache Erweiterung um neue Kategorien
 */
public class Category {
    private final String name;
    private final String description;
    private final String excelColumnName;
    private final boolean isImageCategory;

    /**
     * Constructor für Daten-Kategorien (aus Excel)
     */
    public Category(String name, String description, String excelColumnName) {
        this(name, description, excelColumnName, false);
    }

    /**
     * Constructor für Bild-Kategorien
     */
    public Category(String name, String description, String excelColumnName, boolean isImageCategory) {
        this.name = name;
        this.description = description;
        this.excelColumnName = excelColumnName;
        this.isImageCategory = isImageCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExcelColumnName() {
        return excelColumnName;
    }

    public boolean isImageCategory() {
        return isImageCategory;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

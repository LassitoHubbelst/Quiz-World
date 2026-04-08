# Quiz-World Code-Struktur

## Überblick der neuen OOP-Struktur

Die Anwendung wurde refaktoriert, um eine bessere Separation of Concerns und bessere Erweiterbarkeit zu erreichen. Jetzt ist jedes Konzept als eigene Klasse definiert.

## Klassen-Diagramm

```
┌──────────────────────────────────────────────────────────────┐
│                       DataManager                             │
│  - Liest Excel-Daten und Länder-Verzeichnisse                │
│  - Erstellt Country-Objekte                                   │
│  - Verwaltet CountryRepository                               │
└────────────────────┬─────────────────────────────────────────┘
                     │
                     ▼
        ┌────────────────────────────┐
        │  CountryRepository         │
        │  - Zentrale Verwaltung     │
        │  - Speichert Länder        │
        │  - Verwaltet Kategorien    │
        └────────────────────────────┘
                 │           │
        ┌────────┘           └─────────┐
        ▼                                ▼
    ┌─────────────────┐        ┌──────────────────┐
    │   Country       │        │    Category      │
    │ - Name          │        │ - Name           │
    │ - Attributes    │        │ - Description    │
    │ - Images        │        │ - isImageCategory│
    │ - getData()     │        │ - getColumnName()│
    └─────────────────┘        └──────────────────┘
```

## Klassen-Beschreibungen

### 1. **Country** - Die Länder-Klasse
Repräsentiert ein einzelnes Land mit allen seinen Daten.

```java
// Beispiel: Land erstellen und Daten hinzufügen
Country germany = new Country("Deutschland");
germany.addAttribute("Hauptstadt", "Berlin");
germany.addAttribute("Sprache", "Deutsch");
germany.setCapital("Berlin");
germany.setArea(357000);
germany.setPopulation(83000000);
germany.setImage("Flagge", Path("path/to/flag.png"));
```

**Wichtige Methoden:**
- `addAttribute(key, value)` - Fügt beliebige Attribute hinzu
- `getAttribute(key)` - Liest ein Attribut
- `setImage(categoryName, path)` - Speichert ein Bild für eine Kategorie
- `getImage(categoryName)` - Lädt ein Bild einer Kategorie
- `getFormattedInfo()` - Gibt formatierte Land-Informationen aus
- `isComplete()` - Prüft, ob alle wesentlichen Daten vorhanden sind

**Spezifische Eigenschaften:**
- Population (Bevölkerung)
- Area (Fläche)
- Capital (Hauptstadt)
- Language (Sprache)
- Currency (Währung)
- GDP (Bruttoinlandsprodukt)

---

### 2. **Category** - Die Kategorien-Klasse
Definiert wiederverwendbare Quiz-Kategorien.

```java
// Beispiel: Kategorien
Category capital = new Category("Hauptstadt", 
                               "Die Hauptstadt des Landes", 
                               "Hauptstadt");

Category flagImage = new Category("Flagge", 
                                 "Landesflagge", 
                                 "Flagge", 
                                 true);  // true = Image-Kategorie
```

**Typen von Kategorien:**
- **Daten-Kategorien** - Textuelle Informationen aus Excel
- **Bild-Kategorien** - Bildmaterial aus den Länder-Verzeichnissen

**Wichtige Methoden:**
- `getName()` - Name der Kategorie
- `getExcelColumnName()` - Entsprechende Excel-Spalte
- `isImageCategory()` - Prüft, ob es eine Bild-Kategorie ist
- `getDescription()` - Beschreibung der Kategorie

---

### 3. **CountryRepository** - Das Datenverwaltungs-Pattern
Zentral verwaltete In-Memory Datenbank für Länder und Kategorien.

```java
// Beispiel: Repository verwenden
CountryRepository repo = dataManager.getRepository();

// Länder abrufen
Country germany = repo.getCountry("Deutschland");
Collection<Country> allCountries = repo.getAllCountries();
List<String> names = repo.getAllCountryNames();

// Kategorien abrufen
Category capital = repo.getCategoryByName("Hauptstadt");
List<Category> dataCategories = repo.getDataCategories();
List<Category> imageCategories = repo.getImageCategories();

// Länder hinzufügen/entfernen
repo.addCountry(newCountry);
repo.removeCountry("Deutschland");

// Statistiken
System.out.println(repo.getStatistics());
```

**Wichtige Methoden:**
- `addCountry(country)` - Fügt ein Land hinzu
- `getCountry(name)` - Holt ein Land
- `getAllCountries()` - Alle Länder
- `getAllCountryNames()` - Alle Ländernamen (sortiert)
- `countryExists(name)` - Prüft Existenz
- `addCategory(category)` - Fügt Kategorie hinzu
- `getCategories()` - Alle Kategorien
- `getDataCategories()` - Nur Text-Kategorien
- `getImageCategories()` - Nur Bild-Kategorien
- `getCategoryByName(name)` - Findet Kategorie nach Name

---

### 4. **DataManager** - Die Daten-Lade-Klasse
Verwaltet das Laden von Excel-Daten und Bildern, erstellt Country-Objekte und befüllt das Repository.

```java
// Beispiel: DataManager nutzen
Path excelPath = Paths.get("Länder-Fakten.xlsx");
Path countriesDir = Paths.get("Länder");
DataManager dataManager = new DataManager(excelPath, countriesDir);

// Repository abrufen (neu!)
CountryRepository repo = dataManager.getRepository();
Country germany = repo.getCountry("Deutschland");
```

**Was hat sich geändert:**
- `getRepository()` - Gibt das gefüllte Repository zurück (NEU!)
- `loadCountryInfo(name)` - Gibt jetzt `Country` statt `CountryInfo` zurück

---

## Erweiterung um neue Kategorien

### Szenario: Neue Kategorie hinzufügen

```java
// 1. Kategorie definieren
Category regionCategory = new Category(
    "Region",                    // Name
    "Geografische Region",       // Beschreibung
    "Region"                     // Excel-Spalten-Name
);

// 2. Zur Repository hinzufügen
repository.addCategory(regionCategory);

// 3. Daten für Länder setzen
germany.addAttribute("Region", "Mitteleuropa");
```

### Szenario: Neue Bild-Kategorie hinzufügen

```java
// 1. Bild-Kategorie definieren
Category mapCategory = new Category(
    "Karte",                     // Name
    "Detaillierte Landkarte",    // Beschreibung
    "Karte",                     // Excel-Spalte (optional)
    true                         // true = Image-Kategorie!
);

// 2. Zur Repository hinzufügen
repository.addCategory(mapCategory);

// 3. Bild zum Land setzen
germany.setImage("Karte", Path.of("path/to/map.png"));
```

---

## Migrationsnotizen

### Alt → Neu

```java
// ALT:
CountryInfo info = dataManager.loadCountryInfo("Deutschland");

// NEU:
CountryRepository repo = dataManager.getRepository();
Country country = repo.getCountry("Deutschland");

// ODER direktes Laden:
Country country = dataManager.loadCountryInfo("Deutschland");
```

Die alte `CountryInfo` Klasse bleibt für Rückwärts-Kompatibilität bestehen. Sie können aber die neue `Country` Klasse verwenden.

---

## Best Practices

### 1. **Immer das Repository nutzen**
```java
// GUT: Repository für mehrere Länder verwenden
CountryRepository repo = dataManager.getRepository();
for (Country country : repo.getAllCountries()) {
    System.out.println(country.getName());
}

// SCHLECHT: Einzelne Länder laden
for (String name : names) {
    Country country = dataManager.loadCountryInfo(name);  // Mehrfaches Laden
}
```

### 2. **Kategorien vorher definieren**
```java
// In CountryRepository werden Standard-Kategorien automatisch initialisiert
// Zusätzliche Kategorien:
repository.addCategory(new Category("BIP", "Bruttoinlandsprodukt", "BIP"));
```

### 3. **Bilder und Daten trennen**
```java
// Daten aus Excel:
country.addAttribute("Hauptstadt", "Berlin");

// Bilder aus Verzeichnis:
country.setImage("Flagge", Path.of("..."));
```

---

## Zukünftige Erweiterungen

Diese Struktur ermöglicht einfache Erweiterungen:

1. **Neue Quiz-Modi**: Erstelle neue Quiz-Arten, die die Kategorien des Repositories nutzen
2. **Neue Länder-Quellen**: Lade Länder nicht nur aus Excel, sondern auch aus APIs, Datenbanken, etc.
3. **Neue Attribute**: Alle Länder können beliebige Attribute haben
4. **Sprach-Support**: Kategorien-Namen können leicht übersetzt werden
5. **Validierung**: Länder können komplexere Validierungslogik haben
6. **Scoring-System**: Länder können Statistiken über korrekte/falsche Antworten speichern


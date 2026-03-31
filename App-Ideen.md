# Quiz-World Java App - Ideen und Funktionsübersicht

## Ziel
Eine Java-Anwendung in Visual Studio Code entwickeln, die das Quiz-World-Projekt nutzt. Die App soll Daten aus der vorhandenen Excel-Datei und den Länder-Ordnern verwenden, um Lern- und Quiz-Modi rund um Länderinformationen anzubieten.

## Datenquellen
- `Länder-Fakten.xlsx` als zentrale Tabelle mit Länderinformationen
- `Länder/`-Ordnerstruktur mit einzelnen Länderordnern
  - Lage
  - Umriss
  - Flagge

## Grundfunktionen
1. App startet und prüft, ob die benötigten Dateien/Ordner vorhanden sind
2. Benutzer wählt einen Modus
3. Vor jedem Spielmodus kann die Anzahl der Länder pro Runde festgelegt werden

## Modus 1: Daten ansehen
- Eine Suchzeile ermöglicht die Eingabe eines Landesnamens
- Wenn das Land gefunden wird, werden alle verfügbaren Infos angezeigt
  - Daten aus der Excel-Tabelle
  - Bilder aus dem Länderordner: Lage, Umriss, Flagge
- Optional: zusätzliche Details aus der Excel-Tabelle darstellen
  - Sprache
  - Hauptstadt
  - Einwohnerzahl
  - Fläche
  - Währung
  - BIP, BIP pro Kopf
  - Internet-TLD
  - KFZ-Kennzeichen
  - Telefon-Vorwahl

## Modus 2: Bildquiz "Verdeckte Karte"
- Vor Start wählt der Benutzer den Schwierigkeitsgrad der Verdeckung
  - z. B. 25%, 50%, 75% verdeckt oder einfacher/mittel/schwer
- Der Benutzer muss das Land anhand eines verdeckten Bildes erraten
- Drei Bildkategorien:
  - Umriss
  - Lage
  - Flagge
- Die Anzahl der Länder pro Runde wird zu Beginn gewählt
- Für jedes gewählte Land wird ein verdecktes Bild angezeigt
- Der Spieler gibt den Ländernamen ein und erhält Rückmeldung

## Modus 3: Faktenquiz aus Excel
- Zu Beginn wählt der Benutzer aus:
  - eine einzelne Kategorie
  - oder zufällige Kategorien aus der Excel-Tabelle
- Mögliche Kategorien:
  - Sprache
  - Hauptstadt
  - Einwohnerzahl
  - Einwohner-Dichte
  - Fläche
  - Währung
  - Umrechnung in Euro
  - BIP
  - BIP pro Kopf
  - Internet-TLD
  - KFZ-Kennzeichen
  - Telefon-Vorwahl
- Die Anzahl der Länder pro Runde wird ebenfalls gewählt
- Die App stellt Fragen basierend auf den gewählten Kategorien
- Der Spieler gibt Antworten ein und erhält Punkte oder Feedback

## Erweiterungsmöglichkeiten
- Fortschrittsspeicherung / Highscores
- Kategorienfilter nach Kontinent oder Region
- Schwierigkeitseinstellungen für Quizfragen
- Mehrsprachiges Interface (z. B. Deutsch / Englisch)
- Statistiken am Ende einer Runde

## Technische Umsetzungsideen
- Excel-Datei mit Apache POI oder einem einfachen CSV-Export laden
- Bilder aus den Länderordnern per Datei-Pfad anzeigen
- Konsolen-basiertes Interface oder später GUI mit JavaFX/Swing
- Datenmodelle für `Land`, `QuizFrage` und `Spielrunde`

## Nächste Schritte
1. App-Architektur entwerfen
2. Datenzugriff auf Excel und Ordner implementieren
3. Benutzerführung und Modus-Auswahl entwickeln
4. Erste Spielmodi prototypisch umsetzen

## Aktueller Stand
- Grundgerüst in `Java-App/QuizWorldApp.java` erstellt
- `DataManager.java` lädt Landerdaten aus den Ordnern und enthält Platzhalter für Excel-Daten
- `CountryInfo.java` als Datenmodell für Länderinformationen hinzugefügt
- Modus-Auswahl, Rundenzahl-Abfrage und Platzhalter-Logik implementiert
- Für Excel-Import wird später Apache POI empfohlen oder alternativ ein CSV-Export verwendet

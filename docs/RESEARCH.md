# Research-Ergebnisse (Prototyp GymStudio)

## 1. Datenformat für Ex-/Import

**Entscheidung: versioniertes JSON (UTF-8) über das Storage Access Framework (SAF).**

- JSON ist menschenlesbar, debuggbar und mit `kotlinx.serialization` typsicher als CSV. Verschachtelte Daten (Schritte, Hinweise) lassen sich in CSV nur umständlich abbilden.
- Schema mit `version: 1` (siehe `GymExportDto`) erlaubt künftige Migrationen: Beim Import `ignoreUnknownKeys = true`, alte Felder werden ignoriert, neue können ergänzt werden.
- Dateiendungen/MIME: `application/json`, Dateiname `gymstudio-export.json`. Export über `CreateDocument`, Import über `OpenDocument` – kein Storage-Permission nötig, der Nutzer wählt den Speicherort selbst.
- Pläne referenzieren Übungen **über den Namen** (nicht über die Datenbank-ID), damit ein Import auf einem anderen Gerät zuverlässig neu verknüpft werden kann.

## 2. Speicherung eigener Übungen und Trainingspläne

**Room (SQLite), app-privater Speicher (`gymstudio.db`).**

- Room ist transaktional, typgesichert und liefert reaktive `Flow`s – Listen aktualisieren sich automatisch nach Änderungen.
- Listen (Schritte, Hinweise, Übungs-IDs) werden als JSON-Strings in einzelnen Spalten gespeichert (`TypeConverter` mit `org.json`).
- Eigene Einträge sind über `isCustom = true` markiert; vorgefertigte Pläne/Übungen werden beim ersten Start einmalig geseeded (`GymRepository.seedIfEmpty()`).
- Bilder sind keine Dateien, sondern gebündelte Vector Drawables; die Übung speichert nur einen `iconKey`.

## 3. Zuverlässiges Navigieren

**Jetpack Navigation Compose mit einer einzigen `NavHost` und versiegelten Routen** (`ui/navigation/Routes.kt`).

- Jede Screen-Route ist ein `data object` bzw. eine parametrisierte Route mit `create(id)`-Helfer – Tippfehler in Routen-Strings sind damit compile-time nicht möglich.
- Argumente werden als String übergeben und defensiv geparst (`toLongOrNull()`); ungültige IDs rendern keinen Screen statt zu crashen.
- Zurück läuft konsistent über `popBackStack()`; nach dem Speichern wird zurücknavigiert, damit kein toter Screen im Backstack bleibt.

## 4. Edge-Cases

- **Leere Übung**: Der Name ist Pflichtfeld – `Validation.exerciseNameError()` prüft `isBlank()`, das UI zeigt einen inline-Fehler und speichert nicht (Unit-Test in `ValidationTest`).
- **Leerer Trainingsplan**: Plan braucht mindestens eine Übung – `Validation.planExerciseError()`; der Speichern-Button validiert vor dem Insert.
- **Whitespace-Namen**: Namen werden vor dem Speichern getrimmt.
- **Doppelter Import**: Übungen/Pläne mit bereits vorhandenem Namen (case-insensitive) werden übersprungen und gezählt; ungültige JSON-Dateien führen zu einer Fehlermeldung (Snackbar) statt zu einem Crash (`runCatching` + `IllegalArgumentException`).
- **Verwaiste Referenzen**: Fehlt eine importierte Übungsreferenz in einem Plan, wird sie stillscharf entfernt (`mapNotNull`).

## 5. Design der Übungsbilder

**Strichmännchen als Vector Drawables** (`res/drawable/ic_ex_*.xml`) – wie vorgeschlagen.

- Skalieren verlustfrei auf jede Dichte, kosten praktisch nichts im APK, sind theme-fähig (Tint) und funktionieren garantiert offline.
- Alternativen (Fotos/WebP) wären schwer, lizenzrechtlich heikel und verbrauchen Speicher; eigene Fotos des Nutzers wären ein späterer Schritt (SAF + app-privates `filesDir`).
- Für eigene Übungen wählt der Nutzer eines der 9 mitgelieferten Symbole (`IconKeys.All`).

## 6. Einbindung von Bild-Elementen

- In Compose über `Icon(painterResource(id), ...)` bzw. `Image(...)`; die DB speichert nur den `iconKey`, das Mapping zu `@DrawableRes` liegt zentral in `ui/components/Components.kt` (`iconResFor`).
- App-Icon: adaptives Icon (`mipmap-anydpi-v26`) mit Hantel-Vektor auf Türkis – funktioniert ab minSdk 26 ohne PNG-Fallbacks.

## 7. Design-Entscheidungen (Ergänzung)

- **Farben**: Creme (`#F6EFE2`) als Hintergrund, Blau/Türkis (`#0E8FA3`) als Akzent; im Dark Mode warmes Anthrazit mit hellerem Türkis (`#3CC3D1`) für Kontrast.
- **Filigraner Schatten**: `shadowElevation = 2.dp` auf Karten und `defaultElevation = 2.dp` auf Buttons – sichtbar, aber dezent.
- **Schrift**: System-Schriften (Serif-Display + Sans-Serif), damit die App ohne Font-Download offline bleibt. Für eine eigene Schrift später: `.ttf`/`.otf` in `res/font` + `FontFamily(Font(resId))` – ebenfalls offline.
- **Sprache**: `values/strings.xml` (Deutsch, Default) + `values-en/strings.xml`; Umstellung über `attachBaseContext` + `locales_config.xml`, persisted in `SharedPreferences`.

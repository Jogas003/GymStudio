# GymStudio

Offline-Gym-App (Prototyp) für Android. Alle Trainingsdaten werden ausschließlich lokal auf dem Endgerät gespeichert – es wird keine Internetverbindung benötigt und keine `INTERNET`-Permission vergeben.

## Design

- Minimalistisch-industriell: Creme-Hintergrund (`#F6EFE2`, hell) bzw. warmes Dunkel (`#171410`, dunkel)
- Akzentfarbe Blau/Türkis (`#0E8FA3` hell, `#3CC3D1` dunkel) für alle Buttons und Highlights
- Buttons/Karten mit sehr filigranem Schatten (2 dp, geringe Deckkraft)
- Typo: Serif-Display für die Begrüßung, Sans-Serif für alles Weitere (systemseitig, daher offline)
- Sprache: Deutsch / English über den Flagge-Button auf der Landing-Page

## Funktionsumfang (Prototyp)

1. **Landing-Page**: Begrüßung (tageszeitabhängig), Buttons „Trainingsplan" und „Übungen", Zahnrad (Einstellungen) und Flagge (Sprache) in der Top-Bar
2. **Einstellungen**: JSON-Export/Import der persönlichen Daten (über das System-Dateimenü, Storage Access Framework), Hell-/Dunkel-/Systemmodus
3. **Trainingsplan**: „Trainingspläne" (Liste mit Filtern nach Ziel, Kategorie, Zeitlimit und Erfahrung) und „Eigenen Trainingsplan erstellen" (Übungen per Plus-Button auswählen)
4. **Übungen**: Liste aller Übungen mit Suche, „+" oben rechts für eigene Übungen; Detailansicht mit Name, Beschreibung, Schritt-für-Schritt-Anleitung und Hinweisen

## Technischer Aufbau

- Kotlin + Jetpack Compose (Material 3), Navigation Compose mit versiegelten Routen (`ui/navigation/Routes.kt`)
- Persistenz: Room (SQLite) in `data/local`, Seeddaten für vorgefertigte Übungen/Pläne in `data/local/SeedData.kt`
- Ex/Import: versioniertes JSON (`data/export/ExportImport.kt`), Pläne referenzieren Übungen über Namen (geräteübergreifend stabil)
- Übungen-Bilder: gebündelte Vector Drawables (Strichmännchen), Auswahl über `iconKey`
- Einstellungen (Theme, Sprache): `SharedPreferences` (`ui/Prefs.kt`), Sprache über `attachBaseContext` + `values-en`

## In Android Studio öffnen

1. Android Studio öffnen → **Open** → Ordner `GymStudio` wählen
2. Beim ersten Sync lädt Gradle die Abhängigkeiten einmalig (danach läuft der Build offline weiter; die App selbst benötigt nie Netzwerk)
3. Gerät oder Emulator (API 26+) wählen → **Run**
4. Unit-Tests: `./gradlew :app:testDebugUnitTest` bzw. über den Test-Tab (Validation der Edge-Cases)

## Projektstruktur

```
app/src/main/java/com/gymstudio/app/
  MainActivity.kt            Einstieg, Locale
  GymStudioApplication.kt    Repository
  data/model/                Domain-Modelle, Taxonomie (Filter), Icon-Keys
  data/local/                Room-Entities, DAOs, Datenbank, Seeddaten
  data/repository/           GymRepository (CRUD, Seed, Ex/Import)
  data/export/               JSON-DTOs, ImportResult
  domain/Validation.kt       Edge-Case-Validierung (name Pflicht, Plan braucht Übung)
  ui/                        Theme, Prefs, NavHost, Screens, ViewModels, Komponenten
app/src/main/res/drawable/   Strichmännchen-Vektoren
app/src/main/res/values*/    Strings (de/en), Farben, Theme
```

Details zu den Architektur-Entscheidungen siehe `docs/RESEARCH.md`.

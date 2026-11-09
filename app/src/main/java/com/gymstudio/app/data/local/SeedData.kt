package com.gymstudio.app.data.local

import com.gymstudio.app.data.model.Exercise

data class SeedPlan(
    val name: String,
    val goal: String,
    val category: String,
    val durationMinutes: Int,
    val level: String,
    val description: String,
    val exerciseNames: List<String>,
)

object SeedData {
    val exercises = listOf(
        Exercise(
            name = "Kniebeugen",
            category = "legs",
            description = "Die klassische Grundübung für Beine und Gesäß. Stehend mit hüftbreitem Stand.",
            steps = listOf(
                "Stelle dich hüftbreit auf, die Fußspitzen zeigen leicht nach außen",
                "Spanne Bauch und Rücken an, Brust raus",
                "Gehe kontrolliert in die Hocke, bis die Oberschenkel etwa parallel zum Boden sind",
                "Drücke dich über die Fersen kraftvoll wieder nach oben",
            ),
            hints = listOf(
                "Die Knie zeigen in Richtung der Fußspitzen und knicken nicht nach innen",
                "Der Rücken bleibt die ganze Zeit gerade – kein Rundrücken",
                "Atme ein beim Runtergehen und aus beim Hochkommen",
            ),
            iconKey = "squat",
        ),
        Exercise(
            name = "Ausfallschritte",
            category = "legs",
            description = "Einbeinige Übung für Oberschenkel, Gesäß und Gleichgewicht.",
            steps = listOf(
                "Stelle dich aufrecht hin, Hände in die Hüften oder am Körper",
                "Mache einen großen Schritt nach vorne",
                "Senke die Hüfte, bis beide Knie etwa 90 Grad gebeugt sind",
                "Drücke dich über das vordere Bein zurück in den Stand und wechsle die Seite",
            ),
            hints = listOf(
                "Das vordere Knie bleibt über dem Fuß und kippt nicht nach innen",
                "Der Oberkörper bleibt aufrecht, nicht nach vorne kippen",
            ),
            iconKey = "lunge",
        ),
        Exercise(
            name = "Liegestütze",
            category = "chest",
            description = "Körpergewichtsübung für Brust, Schultern und Trizeps.",
            steps = listOf(
                "Stütze dich mit den Händen schulterbreit auf dem Boden ab",
                "Körper bildet eine gerade Linie von Kopf bis Fersen",
                "Senke die Brust kontrolliert Richtung Boden, Ellbogen etwa 45 Grad zum Körper",
                "Drücke dich explosiv wieder nach oben",
            ),
            hints = listOf(
                "Die Hüfte hängt nicht durch und wird nicht zu hoch gestreckt",
                "Der Kopf bleibt in Verlängerung der Wirbelsäule",
                "Bei zu hoher Belastung: Knie am Boden abstützen",
            ),
            iconKey = "pushup",
        ),
        Exercise(
            name = "Bankdrücken",
            category = "chest",
            description = "Drückübung mit Lang- oder Kurzhanteln auf der Bank für die Brust.",
            steps = listOf(
                "Lege dich auf die Bank, Füße fest am Boden",
                "Greife die Stange etwas breiter als schulterbreit",
                "Senke die Stange kontrolliert zur unteren Brust",
                "Drücke die Stange gerade nach oben, bis die Arme gestreckt sind",
            ),
            hints = listOf(
                "Schulterblätter bleiben während der Übung eingezogen und unten",
                "Die Handgelenke bleiben über den Unterarmen, nicht abgeknickt",
                "Nie allein ohne Ablage oder Spotter sehr schwere Gewichte drücken",
            ),
            iconKey = "bench",
        ),
        Exercise(
            name = "Kreuzheben",
            category = "back",
            description = "Zugübung für den gesamten Rücken, Gesäß und die hintere Oberschenkelkette.",
            steps = listOf(
                "Stelle dich hüftbreit vor die Stange, Zehen unter der Stange",
                "Gehe in die Hocke, Rücken gerade, Brust raus",
                "Greife die Stange mit gestreckten Armen, Hände schulterbreit",
                "Hebe die Stange durch Strecken von Hüfte und Knie, bis du aufrecht stehst",
            ),
            hints = listOf(
                "Die Stange bleibt nah am Körper – nicht nach vorne wegführen",
                "Rücken bleibt neutral, nie rund rücken beim Ziehen",
                "Gewicht langsam und kontrolliert wieder absetzen",
            ),
            iconKey = "deadlift",
        ),
        Exercise(
            name = "Klimmzüge",
            category = "back",
            description = "Zugübung an der Stange für den breiten Rückenmuskel und die Arme.",
            steps = listOf(
                "Hänge dich mit gestreckten Armen an die Stange, Griff etwas breiter als schulterbreit",
                "Ziehe die Schulterblätter zuerst nach unten und zusammen",
                "Ziehe den Körper hoch, bis das Kinn über die Stange kommt",
                "Senke dich kontrolliert wieder ab, bis die Arme gestreckt sind",
            ),
            hints = listOf(
                "Nicht schwungvoll reißen, sondern kontrolliert ziehen",
                "Der Körper hängt während der Übung ruhig, kein Pendeln",
            ),
            iconKey = "pullup",
        ),
        Exercise(
            name = "Plank",
            category = "core",
            description = "Statische Halteübung für Bauch, Rücken und stabilité der Körpermitte.",
            steps = listOf(
                "Stütze dich auf Unterarmen und Fußspitzen ab",
                "Körper bildet eine gerade Linie von Kopf bis Fersen",
                "Spanne Bauch, Gesäß und Oberschenkel aktiv an",
                "Halte die Position so lange wie möglich in sauberer Form",
            ),
            hints = listOf(
                "Die Hüfte sackt nicht durch und wird nicht zu hoch gereckt",
                "Atme ruhig weiter, nicht die Luft anhalten",
            ),
            iconKey = "plank",
        ),
        Exercise(
            name = "Bizeps-Curls",
            category = "arms",
            description = "Isolierte Armübung mit Kurzhanteln für den Bizeps.",
            steps = listOf(
                "Stehe aufrecht, Kurzhanteln an den Seiten, Handflächen nach vorne",
                "Oberarme bleiben am Körper fixiert",
                "Beuge die Unterarme und führe die Hanteln Richtung Schultern",
                "Senke die Hanteln langsam und kontrolliert wieder ab",
            ),
            hints = listOf(
                "Nicht mit dem Oberkörper schwungvoll mitgehen",
                "Ellbogen bleiben an derselben Stelle, nur der Unterarm bewegt sich",
            ),
            iconKey = "curl",
        ),
    )

    val plans = listOf(
        SeedPlan(
            name = "Ganzkörper Basis",
            goal = "strength",
            category = "full_body",
            durationMinutes = 45,
            level = "beginner",
            description = "Solider Einstieg, ideal für 3 Einheiten pro Woche.",
            exerciseNames = listOf("Kniebeugen", "Liegestütze", "Kreuzheben", "Plank"),
        ),
        SeedPlan(
            name = "Oberkörper Muskelaufbau",
            goal = "muscle",
            category = "upper_body",
            durationMinutes = 60,
            level = "intermediate",
            description = "Fokus auf Brust, Rücken, Schultern und Arme.",
            exerciseNames = listOf("Liegestütze", "Bankdrücken", "Klimmzüge", "Bizeps-Curls", "Plank"),
        ),
        SeedPlan(
            name = "Beine & Core",
            goal = "muscle",
            category = "lower_body",
            durationMinutes = 30,
            level = "beginner",
            description = "Kompakte Einheit für starke Beine und eine stabile Körpermitte.",
            exerciseNames = listOf("Kniebeugen", "Ausfallschritte", "Plank"),
        ),
        SeedPlan(
            name = "Kurz & intensiv",
            goal = "endurance",
            category = "full_body",
            durationMinutes = 20,
            level = "intermediate",
            description = "Schnelle Ganzkörper-Runde mit kurzen Pausen.",
            exerciseNames = listOf("Liegestütze", "Kniebeugen", "Ausfallschritte", "Plank"),
        ),
    )
}

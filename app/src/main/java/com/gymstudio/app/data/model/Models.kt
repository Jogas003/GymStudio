package com.gymstudio.app.data.model

data class Exercise(
    val id: Long = 0,
    val name: String,
    val category: String,
    val description: String,
    val steps: List<String>,
    val hints: List<String>,
    val iconKey: String,
    val isCustom: Boolean = false,
)

data class TrainingPlan(
    val id: Long = 0,
    val name: String,
    val goal: String,
    val category: String,
    val durationMinutes: Int,
    val level: String,
    val description: String,
    val exerciseIds: List<Long>,
    val isCustom: Boolean = false,
)

data class PlanWithExercises(
    val plan: TrainingPlan,
    val exercises: List<Exercise>,
)

object Taxonomy {
    val Goals = listOf("muscle", "strength", "endurance", "fat_loss")
    val Categories = listOf(
        "full_body", "upper_body", "lower_body",
        "chest", "back", "legs", "arms", "core",
    )
    val Levels = listOf("beginner", "intermediate", "expert")
    val DurationBuckets = listOf("<=20", "<=45", "<=60", ">60")

    fun durationMatches(bucket: String, minutes: Int): Boolean = when (bucket) {
        "<=20" -> minutes <= 20
        "<=45" -> minutes <= 45
        "<=60" -> minutes <= 60
        else -> minutes > 60
    }
}

object IconKeys {
    val All = listOf(
        "squat", "lunge", "pushup", "bench",
        "deadlift", "pullup", "plank", "curl", "default",
    )
}

package com.gymstudio.app.data.export

import com.gymstudio.app.data.model.Exercise
import com.gymstudio.app.data.model.TrainingPlan
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ExerciseDto(
    val name: String,
    val category: String,
    val description: String = "",
    val steps: List<String> = emptyList(),
    val hints: List<String> = emptyList(),
    val iconKey: String = "default",
)

@Serializable
data class PlanDto(
    val name: String,
    val goal: String,
    val category: String,
    val durationMinutes: Int,
    val level: String,
    val description: String = "",
    val exercises: List<String> = emptyList(),
)

@Serializable
data class GymExportDto(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val exercises: List<ExerciseDto>,
    val plans: List<PlanDto>,
)

data class ImportResult(
    val exercisesAdded: Int,
    val plansAdded: Int,
    val skipped: Int,
)

object ExportImport {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun export(exercises: List<Exercise>, plans: List<TrainingPlan>): String {
        val nameById = exercises.associate { it.id to it.name }
        val dto = GymExportDto(
            exercises = exercises.map {
                ExerciseDto(
                    name = it.name,
                    category = it.category,
                    description = it.description,
                    steps = it.steps,
                    hints = it.hints,
                    iconKey = it.iconKey,
                )
            },
            plans = plans.map {
                PlanDto(
                    name = it.name,
                    goal = it.goal,
                    category = it.category,
                    durationMinutes = it.durationMinutes,
                    level = it.level,
                    description = it.description,
                    exercises = it.exerciseIds.mapNotNull { id -> nameById[id] },
                )
            },
        )
        return json.encodeToString(dto)
    }

    fun parse(jsonText: String): GymExportDto = json.decodeFromString(jsonText)
}

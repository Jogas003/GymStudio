package com.gymstudio.app.data.repository

import com.gymstudio.app.data.export.ExportImport
import com.gymstudio.app.data.export.ImportResult
import com.gymstudio.app.data.local.AppDatabase
import com.gymstudio.app.data.local.ExerciseEntity
import com.gymstudio.app.data.local.PlanEntity
import com.gymstudio.app.data.local.SeedData
import com.gymstudio.app.data.local.toDomain
import com.gymstudio.app.data.local.toEntity
import com.gymstudio.app.data.model.Exercise
import com.gymstudio.app.data.model.PlanWithExercises
import com.gymstudio.app.data.model.TrainingPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray

class GymRepository(private val db: AppDatabase) {
    private val exerciseDao = db.exerciseDao()
    private val planDao = db.planDao()

    val exercises: Flow<List<Exercise>> =
        exerciseDao.observeAll().map { list -> list.map { it.toDomain() } }

    val plans: Flow<List<TrainingPlan>> =
        planDao.observeAll().map { list -> list.map { it.toDomain() } }

    val plansWithExercises: Flow<List<PlanWithExercises>> =
        combine(plans, exercises) { planList, exerciseList ->
            planList.map { plan ->
                PlanWithExercises(
                    plan = plan,
                    exercises = plan.exerciseIds.mapNotNull { id ->
                        exerciseList.firstOrNull { it.id == id }
                    },
                )
            }
        }

    fun exerciseById(id: Long): Flow<Exercise?> =
        exerciseDao.observeById(id).map { it?.toDomain() }

    fun planById(id: Long): Flow<TrainingPlan?> =
        planDao.observeById(id).map { it?.toDomain() }

    suspend fun seedIfEmpty() {
        if (exerciseDao.count() == 0) {
            SeedData.exercises.forEach { exerciseDao.insert(it.toEntity()) }
        }
        if (planDao.count() == 0) {
            val idByName = SeedData.exercises.associate { seed ->
                seed.name to (exerciseDao.findByName(seed.name)?.id ?: 0L)
            }
            SeedData.plans.forEach { seedPlan ->
                planDao.insert(
                    TrainingPlan(
                        name = seedPlan.name,
                        goal = seedPlan.goal,
                        category = seedPlan.category,
                        durationMinutes = seedPlan.durationMinutes,
                        level = seedPlan.level,
                        description = seedPlan.description,
                        exerciseIds = seedPlan.exerciseNames.mapNotNull { idByName[it] },
                        isCustom = false,
                    ).toEntity(),
                )
            }
        }
    }

    suspend fun addExercise(
        name: String,
        category: String,
        description: String,
        steps: List<String>,
        hints: List<String>,
        iconKey: String,
    ): Long = exerciseDao.insert(
        Exercise(
            name = name.trim(),
            category = category,
            description = description.trim(),
            steps = steps,
            hints = hints,
            iconKey = iconKey,
            isCustom = true,
        ).toEntity(),
    )

    suspend fun addPlan(
        name: String,
        goal: String,
        category: String,
        durationMinutes: Int,
        level: String,
        description: String,
        exerciseIds: List<Long>,
    ): Long = planDao.insert(
        TrainingPlan(
            name = name.trim(),
            goal = goal,
            category = category,
            durationMinutes = durationMinutes,
            level = level,
            description = description.trim(),
            exerciseIds = exerciseIds,
            isCustom = true,
        ).toEntity(),
    )

    suspend fun exportJson(): String =
        ExportImport.export(exercises.first(), plans.first())

    suspend fun importJson(jsonText: String): ImportResult {
        val dto = try {
            ExportImport.parse(jsonText)
        } catch (e: Exception) {
            throw IllegalArgumentException("Ungültiger GymStudio-Export", e)
        }

        var exercisesAdded = 0
        var skipped = 0
        val idByName = mutableMapOf<String, Long>()

        dto.exercises.forEach { ex ->
            val key = ex.name.trim().lowercase()
            val existing = exerciseDao.findByName(ex.name)
            if (existing != null) {
                idByName[key] = existing.id
                skipped++
            } else {
                val newId = exerciseDao.insert(
                    ExerciseEntity(
                        name = ex.name.trim(),
                        category = ex.category,
                        description = ex.description,
                        stepsJson = JSONArray(ex.steps).toString(),
                        hintsJson = JSONArray(ex.hints).toString(),
                        iconKey = ex.iconKey,
                        isCustom = true,
                    ),
                )
                if (newId > 0) {
                    idByName[key] = newId
                    exercisesAdded++
                } else {
                    val fallback = exerciseDao.findByName(ex.name)
                    if (fallback != null) {
                        idByName[key] = fallback.id
                        skipped++
                    }
                }
            }
        }

        var plansAdded = 0
        dto.plans.forEach { plan ->
            if (planDao.findByName(plan.name) != null) {
                skipped++
            } else {
                val ids = plan.exercises
                    .map { it.trim().lowercase() }
                    .mapNotNull { idByName[it] }
                planDao.insert(
                    PlanEntity(
                        name = plan.name.trim(),
                        goal = plan.goal,
                        category = plan.category,
                        durationMinutes = plan.durationMinutes,
                        level = plan.level,
                        description = plan.description,
                        exerciseIdsJson = JSONArray(ids).toString(),
                        isCustom = true,
                    ),
                )
                plansAdded++
            }
        }

        return ImportResult(
            exercisesAdded = exercisesAdded,
            plansAdded = plansAdded,
            skipped = skipped,
        )
    }
}

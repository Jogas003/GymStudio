package com.gymstudio.app.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymstudio.app.data.model.PlanWithExercises
import com.gymstudio.app.data.model.Taxonomy
import com.gymstudio.app.data.repository.GymRepository
import com.gymstudio.app.domain.Validation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.snapshotFlow
import kotlinx.coroutines.flow.stateIn

class PlanLibraryViewModel(repo: GymRepository) : ViewModel() {
    private val allPlans: StateFlow<List<PlanWithExercises>> =
        repo.plansWithExercises
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goalFilter = mutableStateOf<String?>(null)
    val categoryFilter = mutableStateOf<String?>(null)
    val durationFilter = mutableStateOf<String?>(null)
    val levelFilter = mutableStateOf<String?>(null)

    val filtered: StateFlow<List<PlanWithExercises>> = combine(
        allPlans,
        snapshotFlow { goalFilter.value },
        snapshotFlow { categoryFilter.value },
        snapshotFlow { durationFilter.value },
        snapshotFlow { levelFilter.value },
    ) { plans, goal, category, duration, level ->
        plans.filter { item ->
            (goal == null || item.plan.goal == goal) &&
                (category == null || item.plan.category == category) &&
                (level == null || item.plan.level == level) &&
                (duration == null || Taxonomy.durationMatches(duration, item.plan.durationMinutes))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class PlanBuilderViewModel(private val repo: GymRepository) : ViewModel() {
    val exercises: StateFlow<List<com.gymstudio.app.data.model.Exercise>> =
        repo.exercises.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val name = mutableStateOf("")
    val goal = mutableStateOf<String?>(null)
    val category = mutableStateOf<String?>(null)
    val level = mutableStateOf<String?>(null)
    val durationMinutes = mutableStateOf("45")
    val description = mutableStateOf("")
    val selected = mutableStateOf<Set<Long>>(emptySet())

    val nameError = mutableStateOf<String?>(null)
    val exerciseError = mutableStateOf<String?>(null)

    fun toggleExercise(id: Long) {
        selected.value = if (id in selected.value) {
            selected.value - id
        } else {
            selected.value + id
        }
    }

    suspend fun save(): Boolean {
        nameError.value = Validation.planNameError(name.value)
        exerciseError.value = Validation.planExerciseError(selected.value.size)
        if (nameError.value != null || exerciseError.value != null) return false

        val minutes = durationMinutes.value.trim().toIntOrNull()?.coerceIn(5, 240) ?: 45
        repo.addPlan(
            name = name.value,
            goal = goal.value ?: Taxonomy.Goals.first(),
            category = category.value ?: Taxonomy.Categories.first(),
            durationMinutes = minutes,
            level = level.value ?: Taxonomy.Levels.first(),
            description = description.value,
            exerciseIds = selected.value.toList(),
        )
        return true
    }
}

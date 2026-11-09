package com.gymstudio.app.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gymstudio.app.data.model.Taxonomy
import com.gymstudio.app.data.repository.GymRepository
import com.gymstudio.app.domain.Validation

class AddExerciseViewModel(private val repo: GymRepository) : ViewModel() {
    val name = mutableStateOf("")
    val category = mutableStateOf(Taxonomy.Categories.first())
    val description = mutableStateOf("")
    val steps = mutableStateOf(listOf(""))
    val hints = mutableStateOf(listOf(""))
    val iconKey = mutableStateOf("default")

    val nameError = mutableStateOf<String?>(null)

    fun addStep() {
        steps.value = steps.value + ""
    }

    fun updateStep(index: Int, value: String) {
        steps.value = steps.value.toMutableList().also { it[index] = value }
    }

    fun removeStep(index: Int) {
        if (steps.value.size > 1) {
            steps.value = steps.value.toMutableList().also { it.removeAt(index) }
        }
    }

    fun addHint() {
        hints.value = hints.value + ""
    }

    fun updateHint(index: Int, value: String) {
        hints.value = hints.value.toMutableList().also { it[index] = value }
    }

    fun removeHint(index: Int) {
        if (hints.value.size > 1) {
            hints.value = hints.value.toMutableList().also { it.removeAt(index) }
        }
    }

    suspend fun save(): Boolean {
        nameError.value = Validation.exerciseNameError(name.value)
        if (nameError.value != null) return false

        repo.addExercise(
            name = name.value,
            category = category.value,
            description = description.value,
            steps = steps.value.map { it.trim() }.filter { it.isNotBlank() },
            hints = hints.value.map { it.trim() }.filter { it.isNotBlank() },
            iconKey = iconKey.value,
        )
        return true
    }
}

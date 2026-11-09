package com.gymstudio.app.domain

object Validation {
    const val ERROR_NAME_REQUIRED = "error_name_required"
    const val ERROR_PLAN_NEEDS_EXERCISE = "error_plan_needs_exercise"

    fun exerciseNameError(name: String): String? =
        if (name.isBlank()) ERROR_NAME_REQUIRED else null

    fun planNameError(name: String): String? =
        if (name.isBlank()) ERROR_NAME_REQUIRED else null

    fun planExerciseError(exerciseCount: Int): String? =
        if (exerciseCount <= 0) ERROR_PLAN_NEEDS_EXERCISE else null
}

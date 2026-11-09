package com.gymstudio.app.ui.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object PlansMenu : Route("plans")
    data object PlanLibrary : Route("plan_library")
    data object PlanBuilder : Route("plan_builder")
    data object PlanDetail : Route("plan_detail/{planId}") {
        fun create(planId: Long) = "plan_detail/$planId"
    }
    data object Exercises : Route("exercises")
    data object ExerciseDetail : Route("exercise_detail/{exerciseId}") {
        fun create(exerciseId: Long) = "exercise_detail/$exerciseId"
    }
    data object AddExercise : Route("add_exercise")
    data object Settings : Route("settings")
}

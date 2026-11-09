package com.gymstudio.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gymstudio.app.LocalRepository
import com.gymstudio.app.ui.navigation.Route
import com.gymstudio.app.ui.screens.AddExerciseScreen
import com.gymstudio.app.ui.screens.ExerciseDetailScreen
import com.gymstudio.app.ui.screens.ExercisesScreen
import com.gymstudio.app.ui.screens.HomeScreen
import com.gymstudio.app.ui.screens.PlanBuilderScreen
import com.gymstudio.app.ui.screens.PlanDetailScreen
import com.gymstudio.app.ui.screens.PlanLibraryScreen
import com.gymstudio.app.ui.screens.PlansMenuScreen
import com.gymstudio.app.ui.screens.SettingsScreen
import com.gymstudio.app.ui.theme.GymStudioTheme

@Composable
fun GymStudioApp() {
    val repository = LocalRepository.current
    val darkTheme = when (Prefs.themeState.value) {
        Prefs.THEME_DARK -> true
        Prefs.THEME_LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    GymStudioTheme(darkTheme = darkTheme) {
        val navController = rememberNavController()

        LaunchedEffect(Unit) {
            repository.seedIfEmpty()
        }

        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Route.Home.route,
            ) {
                composable(Route.Home.route) {
                    HomeScreen(navController)
                }
                composable(Route.PlansMenu.route) {
                    PlansMenuScreen(navController)
                }
                composable(Route.PlanLibrary.route) {
                    PlanLibraryScreen(navController)
                }
                composable(Route.PlanBuilder.route) {
                    PlanBuilderScreen(navController)
                }
                composable(Route.PlanDetail.route) { backStackEntry ->
                    val planId = backStackEntry.arguments?.getString("planId")?.toLongOrNull()
                    if (planId != null) {
                        PlanDetailScreen(navController, planId)
                    }
                }
                composable(Route.Exercises.route) {
                    ExercisesScreen(navController)
                }
                composable(Route.ExerciseDetail.route) { backStackEntry ->
                    val exerciseId =
                        backStackEntry.arguments?.getString("exerciseId")?.toLongOrNull()
                    if (exerciseId != null) {
                        ExerciseDetailScreen(navController, exerciseId)
                    }
                }
                composable(Route.AddExercise.route) {
                    AddExerciseScreen(navController)
                }
                composable(Route.Settings.route) {
                    SettingsScreen(navController)
                }
            }
        }
    }
}

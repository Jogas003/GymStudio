package com.gymstudio.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.ui.LocalRepository
import com.gymstudio.app.ui.categoryLabel
import com.gymstudio.app.ui.components.ExerciseIcon
import com.gymstudio.app.ui.components.GymCard
import com.gymstudio.app.ui.components.GymTopBar
import com.gymstudio.app.ui.goalLabel
import com.gymstudio.app.ui.levelLabel
import com.gymstudio.app.ui.navigation.Route

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanDetailScreen(navController: NavController, planId: Long) {
    val repo = LocalRepository.current
    val plan by repo.planById(planId).collectAsStateWithLifecycle(null)
    val allExercises by repo.exercises.collectAsStateWithLifecycle(emptyList())

    Scaffold(
        topBar = {
            GymTopBar(
                title = plan?.name ?: "",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        val currentPlan = plan
        if (currentPlan == null) return@Scaffold
        val planExercises = currentPlan.exerciseIds.mapNotNull { id ->
            allExercises.firstOrNull { it.id == id }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PlanMetaChip(goalLabel(currentPlan.goal))
                PlanMetaChip(categoryLabel(currentPlan.category))
                PlanMetaChip(levelLabel(currentPlan.level))
                PlanMetaChip(stringResource(R.string.minutes, currentPlan.durationMinutes))
            }
            if (currentPlan.description.isNotBlank()) {
                Text(
                    text = currentPlan.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            currentPlan.exerciseIds.forEachIndexed { index, _ ->
                val exercise = planExercises.getOrNull(index) ?: return@forEachIndexed
                GymCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(Route.ExerciseDetail.create(exercise.id)) },
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    ) {
                        ExerciseIcon(iconKey = exercise.iconKey, size = 44.dp)
                        Column {
                            Text(
                                text = "${index + 1}. ${exercise.name}",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = categoryLabel(exercise.category),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanMetaChip(text: String) {
    androidx.compose.material3.Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        )
    }
}

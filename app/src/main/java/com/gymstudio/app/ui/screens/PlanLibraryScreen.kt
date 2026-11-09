package com.gymstudio.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.data.model.PlanWithExercises
import com.gymstudio.app.data.model.Taxonomy
import com.gymstudio.app.ui.LocalRepository
import com.gymstudio.app.ui.categoryLabel
import com.gymstudio.app.ui.components.GymCard
import com.gymstudio.app.ui.components.GymPlaceholder
import com.gymstudio.app.ui.components.GymTopBar
import com.gymstudio.app.ui.durationLabel
import com.gymstudio.app.ui.goalLabel
import com.gymstudio.app.ui.levelLabel
import com.gymstudio.app.ui.navigation.Route
import com.gymstudio.app.ui.viewmodel.PlanLibraryViewModel
import com.gymstudio.app.ui.gymViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanLibraryScreen(navController: NavController) {
    val viewModel = gymViewModel { PlanLibraryViewModel(LocalRepository.current) }
    val plans by viewModel.filtered.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GymTopBar(
                title = stringResource(R.string.library_title),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            FilterRow(
                title = stringResource(R.string.filter_goal),
                options = Taxonomy.Goals,
                selected = viewModel.goalFilter.value,
                onSelected = { viewModel.goalFilter.value = it },
                labelOf = { goalLabel(it) },
            )
            FilterRow(
                title = stringResource(R.string.filter_category),
                options = Taxonomy.Categories,
                selected = viewModel.categoryFilter.value,
                onSelected = { viewModel.categoryFilter.value = it },
                labelOf = { categoryLabel(it) },
            )
            FilterRow(
                title = stringResource(R.string.filter_duration),
                options = Taxonomy.DurationBuckets,
                selected = viewModel.durationFilter.value,
                onSelected = { viewModel.durationFilter.value = it },
                labelOf = { durationLabel(it) },
            )
            FilterRow(
                title = stringResource(R.string.filter_level),
                options = Taxonomy.Levels,
                selected = viewModel.levelFilter.value,
                onSelected = { viewModel.levelFilter.value = it },
                labelOf = { levelLabel(it) },
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(plans, key = { it.plan.id }) { item ->
                    PlanCard(item = item) {
                        navController.navigate(Route.PlanDetail.create(item.plan.id))
                    }
                }
                if (plans.isEmpty()) {
                    item {
                        GymPlaceholder(text = stringResource(R.string.empty_plans))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> FilterRow(
    title: String,
    options: List<T>,
    selected: T?,
    onSelected: (T?) -> Unit,
    labelOf: @Composable (T) -> String,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp),
        ) {
            FilterChip(
                selected = selected == null,
                onClick = { onSelected(null) },
                label = { Text(stringResource(R.string.filter_all)) },
            )
            options.forEach { option ->
                FilterChip(
                    selected = selected == option,
                    onClick = { onSelected(if (selected == option) null else option) },
                    label = { Text(labelOf(option)) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlanCard(
    item: PlanWithExercises,
    onClick: () -> Unit,
) {
    GymCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.plan.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                if (item.plan.isCustom) {
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text(
                            text = stringResource(R.string.plan_custom),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetaChip(goalLabel(item.plan.goal))
                MetaChip(levelLabel(item.plan.level))
                MetaChip(stringResource(R.string.minutes, item.plan.durationMinutes))
            }
            Text(
                text = stringResource(R.string.plan_exercises_count, item.exercises.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MetaChip(text: String) {
    Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

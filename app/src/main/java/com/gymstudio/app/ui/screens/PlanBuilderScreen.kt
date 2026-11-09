package com.gymstudio.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.data.model.Exercise
import com.gymstudio.app.data.model.Taxonomy
import com.gymstudio.app.ui.LocalRepository
import com.gymstudio.app.ui.categoryLabel
import com.gymstudio.app.ui.components.ExerciseIcon
import com.gymstudio.app.ui.components.GymButton
import com.gymstudio.app.ui.components.GymTopBar
import com.gymstudio.app.ui.goalLabel
import com.gymstudio.app.ui.levelLabel
import com.gymstudio.app.ui.viewmodel.PlanBuilderViewModel
import com.gymstudio.app.ui.gymViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlanBuilderScreen(navController: NavController) {
    val viewModel = gymViewModel { PlanBuilderViewModel(LocalRepository.current) }
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            GymTopBar(
                title = stringResource(R.string.builder_title),
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = viewModel.name.value,
                onValueChange = {
                    viewModel.name.value = it
                    viewModel.nameError.value = null
                },
                label = { Text(stringResource(R.string.plan_name_hint)) },
                isError = viewModel.nameError.value != null,
                supportingText = {
                    if (viewModel.nameError.value != null) {
                        Text(stringResource(R.string.error_name_required))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            DropdownField(
                label = stringResource(R.string.plan_goal_label),
                options = Taxonomy.Goals,
                selected = viewModel.goal.value,
                onSelected = { viewModel.goal.value = it },
                labelOf = { goalLabel(it) },
            )
            DropdownField(
                label = stringResource(R.string.plan_category_label),
                options = Taxonomy.Categories,
                selected = viewModel.category.value,
                onSelected = { viewModel.category.value = it },
                labelOf = { categoryLabel(it) },
            )
            DropdownField(
                label = stringResource(R.string.plan_level_label),
                options = Taxonomy.Levels,
                selected = viewModel.level.value,
                onSelected = { viewModel.level.value = it },
                labelOf = { levelLabel(it) },
            )
            OutlinedTextField(
                value = viewModel.durationMinutes.value,
                onValueChange = { viewModel.durationMinutes.value = it.filter { c -> c.isDigit() } },
                label = { Text(stringResource(R.string.plan_duration_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.description.value = it },
                label = { Text(stringResource(R.string.plan_description_hint)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.pick_exercises),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.selected_count, viewModel.selected.value.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (viewModel.exerciseError.value != null) {
                Text(
                    text = stringResource(R.string.error_plan_needs_exercise),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            exercises.forEach { exercise ->
                ExercisePickerRow(
                    exercise = exercise,
                    isSelected = exercise.id in viewModel.selected.value,
                    onToggle = { viewModel.toggleExercise(exercise.id) },
                )
            }

            GymButton(
                text = stringResource(R.string.save),
                onClick = {
                    scope.launch {
                        if (viewModel.save()) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownField(
    label: String,
    options: List<T>,
    selected: T?,
    onSelected: (T) -> Unit,
    labelOf: @Composable (T) -> String,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selected?.let { labelOf(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(labelOf(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ExercisePickerRow(
    exercise: Exercise,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExerciseIcon(iconKey = exercise.iconKey, size = 40.dp)
            Column {
                Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = categoryLabel(exercise.category),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

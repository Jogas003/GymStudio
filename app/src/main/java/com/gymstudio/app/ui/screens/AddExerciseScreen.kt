package com.gymstudio.app.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.data.model.IconKeys
import com.gymstudio.app.data.model.Taxonomy
import com.gymstudio.app.ui.LocalRepository
import com.gymstudio.app.ui.categoryLabel
import com.gymstudio.app.ui.components.ExerciseIcon
import com.gymstudio.app.ui.components.GymButton
import com.gymstudio.app.ui.components.GymTopBar
import com.gymstudio.app.ui.gymViewModel
import com.gymstudio.app.ui.viewmodel.AddExerciseViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddExerciseScreen(navController: NavController) {
    val viewModel = gymViewModel { AddExerciseViewModel(LocalRepository.current) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            GymTopBar(
                title = stringResource(R.string.add_exercise),
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
                label = { Text(stringResource(R.string.ex_name_hint)) },
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
                label = stringResource(R.string.ex_category_label),
                options = Taxonomy.Categories,
                selected = viewModel.category.value,
                onSelected = { viewModel.category.value = it },
                labelOf = { categoryLabel(it) },
            )

            OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.description.value = it },
                label = { Text(stringResource(R.string.ex_description_hint)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            Text(
                text = stringResource(R.string.ex_steps_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            viewModel.steps.value.forEachIndexed { index, step ->
                DynamicTextRow(
                    value = step,
                    label = stringResource(R.string.ex_step_hint, index + 1),
                    onValueChange = { viewModel.updateStep(index, it) },
                    onRemove = { viewModel.removeStep(index) },
                )
            }
            TextButton(onClick = { viewModel.addStep() }) {
                Text(stringResource(R.string.add_step))
            }

            Text(
                text = stringResource(R.string.ex_hints_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            viewModel.hints.value.forEachIndexed { index, hint ->
                DynamicTextRow(
                    value = hint,
                    label = stringResource(R.string.ex_hint_hint, index + 1),
                    onValueChange = { viewModel.updateHint(index, it) },
                    onRemove = { viewModel.removeHint(index) },
                )
            }
            TextButton(onClick = { viewModel.addHint() }) {
                Text(stringResource(R.string.add_hint))
            }

            Text(
                text = stringResource(R.string.icon_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconKeys.All.forEach { key ->
                    val isSelected = viewModel.iconKey.value == key
                    IconButton(
                        onClick = { viewModel.iconKey.value = key },
                        modifier = Modifier
                            .size(48.dp)
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(12.dp),
                                    )
                                } else {
                                    Modifier
                                },
                            ),
                    ) {
                        ExerciseIcon(
                            iconKey = key,
                            size = 32.dp,
                            tint = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }
                }
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

@Composable
private fun DynamicTextRow(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true,
        )
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownField(
    label: String,
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    labelOf: @Composable (T) -> String,
) {
    var expanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    androidx.compose.material3.ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = labelOf(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        androidx.compose.material3.ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                androidx.compose.material3.DropdownMenuItem(
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

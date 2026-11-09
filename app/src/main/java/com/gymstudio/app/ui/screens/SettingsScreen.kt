package com.gymstudio.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.ui.LocalRepository
import com.gymstudio.app.ui.Prefs
import com.gymstudio.app.ui.components.GymButton
import com.gymstudio.app.ui.components.GymTopBar
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = LocalRepository.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val ok = runCatching {
                    val json = repo.exportJson()
                    context.contentResolver.openOutputStream(uri)?.use { output ->
                        output.write(json.toByteArray(Charsets.UTF_8))
                    } != null
                }.getOrDefault(false)
                snackbarHostState.showSnackbar(
                    context.getString(if (ok) R.string.export_success else R.string.export_error),
                )
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val jsonText = runCatching {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        input.readBytes().toString(Charsets.UTF_8)
                    }
                }.getOrNull()
                val result = jsonText?.let { runCatching { repo.importJson(it) }.getOrNull() }
                snackbarHostState.showSnackbar(
                    if (result != null) {
                        context.getString(
                            R.string.import_success,
                            result.exercisesAdded,
                            result.plansAdded,
                            result.skipped,
                        )
                    } else {
                        context.getString(R.string.import_error)
                    },
                )
            }
        }
    }

    Scaffold(
        topBar = {
            GymTopBar(
                title = stringResource(R.string.settings_title),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.theme_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Prefs.THEME_SYSTEM to stringResource(R.string.theme_system),
                    Prefs.THEME_LIGHT to stringResource(R.string.theme_light),
                    Prefs.THEME_DARK to stringResource(R.string.theme_dark),
                ).forEach { (value, label) ->
                    FilterChip(
                        selected = Prefs.themeState.value == value,
                        onClick = { Prefs.setTheme(value) },
                        label = { Text(label) },
                    )
                }
            }

            GymButton(
                text = stringResource(R.string.export_data),
                onClick = { exportLauncher.launch("gymstudio-export.json") },
                modifier = Modifier.fillMaxWidth(),
            )
            GymButton(
                text = stringResource(R.string.import_data),
                onClick = { importLauncher.launch(arrayOf("application/json", "text/json", "*/*")) },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = stringResource(R.string.offline_note),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

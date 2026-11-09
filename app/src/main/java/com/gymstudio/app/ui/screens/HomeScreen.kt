package com.gymstudio.app.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.ui.Prefs
import com.gymstudio.app.ui.components.GymButton
import com.gymstudio.app.ui.components.GymTopBar
import com.gymstudio.app.ui.navigation.Route
import java.util.Calendar

@Composable
fun HomeScreen(navController: NavController) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 11 -> stringResource(R.string.home_greeting_morning)
        hour < 18 -> stringResource(R.string.home_greeting_day)
        else -> stringResource(R.string.home_greeting_evening)
    }

    Scaffold(
        topBar = {
            GymTopBar(
                title = stringResource(R.string.app_name),
                actions = {
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(Icons.Default.Language, contentDescription = stringResource(R.string.language))
                    }
                    IconButton(onClick = { navController.navigate(Route.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(12.dp))
            GymButton(
                text = stringResource(R.string.action_plans),
                onClick = { navController.navigate(Route.PlansMenu.route) },
                modifier = Modifier.fillMaxWidth().height(64.dp),
            )
            GymButton(
                text = stringResource(R.string.action_exercises),
                onClick = { navController.navigate(Route.Exercises.route) },
                modifier = Modifier.fillMaxWidth().height(64.dp),
            )
        }
    }

    if (showLanguageDialog) {
        LanguageDialog(
            onDismiss = { showLanguageDialog = false },
            onSelected = { lang ->
                Prefs.setLang(lang)
                showLanguageDialog = false
                (context as? Activity)?.recreate()
            },
        )
    }
}

@Composable
private fun LanguageDialog(
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.lang_title)) },
        text = {
            Column {
                TextButton(
                    onClick = { onSelected(Prefs.LANG_DE) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.lang_de))
                }
                TextButton(
                    onClick = { onSelected(Prefs.LANG_EN) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.lang_en))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

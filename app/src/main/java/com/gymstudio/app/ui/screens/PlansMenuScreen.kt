package com.gymstudio.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gymstudio.app.R
import com.gymstudio.app.ui.components.GymButton
import com.gymstudio.app.ui.components.GymTopBar
import com.gymstudio.app.ui.navigation.Route

@Composable
fun PlansMenuScreen(navController: NavController) {
    Scaffold(
        topBar = {
            GymTopBar(
                title = stringResource(R.string.plans_title),
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
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GymButton(
                text = stringResource(R.string.plans_prebuilt),
                onClick = { navController.navigate(Route.PlanLibrary.route) },
                modifier = Modifier.fillMaxWidth().height(64.dp),
            )
            GymButton(
                text = stringResource(R.string.plans_create),
                onClick = { navController.navigate(Route.PlanBuilder.route) },
                modifier = Modifier.fillMaxWidth().height(64.dp),
            )
        }
    }
}

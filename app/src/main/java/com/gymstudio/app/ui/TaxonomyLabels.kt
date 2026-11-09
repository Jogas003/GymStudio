package com.gymstudio.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gymstudio.app.R

@Composable
fun goalLabel(key: String): String = stringResource(
    when (key) {
        "muscle" -> R.string.goal_muscle
        "strength" -> R.string.goal_strength
        "endurance" -> R.string.goal_endurance
        "fat_loss" -> R.string.goal_fat_loss
        else -> R.string.filter_all
    },
)

@Composable
fun categoryLabel(key: String): String = stringResource(
    when (key) {
        "full_body" -> R.string.cat_full_body
        "upper_body" -> R.string.cat_upper_body
        "lower_body" -> R.string.cat_lower_body
        "chest" -> R.string.cat_chest
        "back" -> R.string.cat_back
        "legs" -> R.string.cat_legs
        "arms" -> R.string.cat_arms
        "core" -> R.string.cat_core
        else -> R.string.filter_all
    },
)

@Composable
fun levelLabel(key: String): String = stringResource(
    when (key) {
        "beginner" -> R.string.level_beginner
        "intermediate" -> R.string.level_intermediate
        "expert" -> R.string.level_expert
        else -> R.string.filter_all
    },
)

@Composable
fun durationLabel(key: String): String = stringResource(
    when (key) {
        "<=20" -> R.string.duration_up_to_20
        "<=45" -> R.string.duration_up_to_45
        "<=60" -> R.string.duration_up_to_60
        else -> R.string.duration_over_60
    },
)

package com.gymstudio.app.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified T : ViewModel> gymViewModel(
    key: String? = null,
    noinline creator: () -> T,
): T {
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
    }
    return viewModel(key = key, factory = factory)
}

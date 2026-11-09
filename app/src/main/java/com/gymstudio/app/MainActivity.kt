package com.gymstudio.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.gymstudio.app.data.repository.GymRepository
import com.gymstudio.app.ui.GymStudioApp
import com.gymstudio.app.ui.Prefs
import java.util.Locale

val LocalRepository = compositionLocalOf<GymRepository> {
    error("No GymRepository provided")
}

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = Prefs.prefs(newBase).getString("lang", Prefs.LANG_DE) ?: Prefs.LANG_DE
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val repository = (application as GymStudioApplication).repository
        setContent {
            CompositionLocalProvider(LocalRepository provides repository) {
                GymStudioApp()
            }
        }
    }
}

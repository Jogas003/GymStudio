package com.gymstudio.app.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

object Prefs {
    private const val FILE = "gym_prefs"
    private const val KEY_THEME = "theme"
    private const val KEY_LANG = "lang"

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

    const val LANG_DE = "de"
    const val LANG_EN = "en"

    private lateinit var prefs: SharedPreferences

    val themeState = mutableStateOf(THEME_SYSTEM)
    val langState = mutableStateOf(LANG_DE)

    fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun init(context: Context) {
        prefs = prefs(context.applicationContext)
        themeState.value = prefs.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
        langState.value = prefs.getString(KEY_LANG, LANG_DE) ?: LANG_DE
        prefs.registerOnSharedPreferenceChangeListener { p, key ->
            when (key) {
                KEY_THEME -> themeState.value = p.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
                KEY_LANG -> langState.value = p.getString(KEY_LANG, LANG_DE) ?: LANG_DE
            }
        }
    }

    fun setTheme(theme: String) {
        prefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun setLang(lang: String) {
        prefs.edit().putString(KEY_LANG, lang).apply()
    }
}

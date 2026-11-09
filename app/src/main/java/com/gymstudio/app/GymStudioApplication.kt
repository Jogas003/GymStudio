package com.gymstudio.app

import android.app.Application
import com.gymstudio.app.data.local.AppDatabase
import com.gymstudio.app.data.repository.GymRepository
import com.gymstudio.app.ui.Prefs

class GymStudioApplication : Application() {
    val repository: GymRepository by lazy {
        GymRepository(AppDatabase.getInstance(this))
    }

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
    }
}

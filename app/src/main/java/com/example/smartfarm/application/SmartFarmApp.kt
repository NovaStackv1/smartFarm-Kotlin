package com.example.smartfarm.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.smartfarm.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class SmartFarmApp : Application(){
    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging (optional)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize Places SDK if you're using it
        // Places.initialize(applicationContext, BuildConfig.MAP_API_KEY)

        // Note: For Maps SDK, the initialization is usually automatic
        // but you can initialize it explicitly if needed
    }
}
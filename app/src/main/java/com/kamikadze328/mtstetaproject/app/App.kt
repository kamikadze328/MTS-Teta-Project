package com.kamikadze328.mtstetaproject.app

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {


    @Inject
    lateinit var workerConfiguration: Configuration

    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfiguration
    }
}
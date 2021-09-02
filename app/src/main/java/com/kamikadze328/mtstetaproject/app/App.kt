package com.kamikadze328.mtstetaproject.app

import android.app.Application
import androidx.work.*
import com.kamikadze328.mtstetaproject.BuildConfig
import com.kamikadze328.mtstetaproject.background.UpdateMoviesWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerConfiguration: Configuration

    //worker
    private val updateEveryN = 15L
    private val updateEveryTimeUnit = TimeUnit.MINUTES
    private val updateTag = "UpdateMoviesWorker"

    override fun onCreate() {
        super.onCreate()
        setupMoviesUpdateWorker()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfiguration
    }

    private fun setupMoviesUpdateWorker() {
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<UpdateMoviesWorker>(
            updateEveryN, updateEveryTimeUnit
        ).setBackoffCriteria(
            BackoffPolicy.LINEAR,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .addTag(updateTag)
            .build()

        val existingPeriodicWorkPolicy =
            if (BuildConfig.DEBUG) ExistingPeriodicWorkPolicy.REPLACE else ExistingPeriodicWorkPolicy.KEEP

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(updateTag, existingPeriodicWorkPolicy, workRequest)


    }
}
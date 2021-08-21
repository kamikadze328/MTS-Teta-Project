package com.kamikadze328.mtstetaproject.background.notifications.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.kamikadze328.mtstetaproject.background.UpdateMoviesWorker
import com.kamikadze328.mtstetaproject.background.notifications.MovieUploadAndNotifyWorker
import com.kamikadze328.mtstetaproject.data.repository.MovieDetailsRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository

class MyWorkerFactory constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val movieRepository: MovieRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            MovieUploadAndNotifyWorker::class.java.name ->
                MovieUploadAndNotifyWorker(appContext, workerParameters, movieDetailsRepository)
            UpdateMoviesWorker::class.java.name ->
                UpdateMoviesWorker(appContext, workerParameters, movieRepository)
            else -> null
        }
    }
}
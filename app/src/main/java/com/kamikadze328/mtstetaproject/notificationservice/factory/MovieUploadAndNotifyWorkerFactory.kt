package com.kamikadze328.mtstetaproject.notificationservice.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.kamikadze328.mtstetaproject.data.repository.MovieDetailsRepository
import com.kamikadze328.mtstetaproject.notificationservice.MovieUploadAndNotifyWorker

class MovieUploadAndNotifyWorkerFactory constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            MovieUploadAndNotifyWorker::class.java.name ->
                MovieUploadAndNotifyWorker(appContext, workerParameters, movieDetailsRepository)
            else -> null
        }
    }
}
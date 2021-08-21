package com.kamikadze328.mtstetaproject.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateMoviesWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val movieRepository: MovieRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        try {
            movieRepository.refreshPopularMovies()
        } catch (e: Exception) {
            return Result.retry()
        }
        return Result.success()
    }
}
package com.kamikadze328.mtstetaproject.background

import android.content.Context
import android.util.Log
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
            Log.d("kek", "update ")
            val movies = movieRepository.refreshPopularMovies()
            Log.d("kek", "update $movies")

        } catch (e: Exception) {
            return Result.retry()
        }
        return Result.success()
    }
}
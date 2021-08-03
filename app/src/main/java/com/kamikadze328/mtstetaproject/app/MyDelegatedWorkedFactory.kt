package com.kamikadze328.mtstetaproject.app

import androidx.work.DelegatingWorkerFactory
import com.kamikadze328.mtstetaproject.notificationservice.factory.MovieUploadAndNotifyWorkerFactory
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDelegatedWorkedFactory @Inject constructor(
    movieRepository: MovieRepository
) : DelegatingWorkerFactory() {
    init {
        addFactory(MovieUploadAndNotifyWorkerFactory(movieRepository))
    }
}

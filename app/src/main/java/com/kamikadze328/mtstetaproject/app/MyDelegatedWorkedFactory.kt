package com.kamikadze328.mtstetaproject.app

import androidx.work.DelegatingWorkerFactory
import com.kamikadze328.mtstetaproject.data.repository.MovieDetailsRepository
import com.kamikadze328.mtstetaproject.notificationservice.factory.MovieUploadAndNotifyWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDelegatedWorkedFactory @Inject constructor(
    movieDetailsRepository: MovieDetailsRepository
) : DelegatingWorkerFactory() {
    init {
        addFactory(MovieUploadAndNotifyWorkerFactory(movieDetailsRepository))
    }
}

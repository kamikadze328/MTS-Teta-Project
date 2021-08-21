package com.kamikadze328.mtstetaproject.app

import androidx.work.DelegatingWorkerFactory
import com.kamikadze328.mtstetaproject.background.notifications.factory.MyWorkerFactory
import com.kamikadze328.mtstetaproject.data.repository.MovieDetailsRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDelegatedWorkedFactory @Inject constructor(
    movieDetailsRepository: MovieDetailsRepository,
    movieRepository: MovieRepository
) : DelegatingWorkerFactory() {
    init {
        addFactory(MyWorkerFactory(movieDetailsRepository, movieRepository))
    }
}

package com.kamikadze328.mtstetaproject.di

import android.app.Application
import com.kamikadze328.mtstetaproject.data.network.Webservice
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieRepositoryModule {
    @Inject
    @Singleton
    @Provides
    fun provideMovieRepository(webservice: Webservice, application: Application): MovieRepository {
        return MovieRepository(webservice, application)
    }
}
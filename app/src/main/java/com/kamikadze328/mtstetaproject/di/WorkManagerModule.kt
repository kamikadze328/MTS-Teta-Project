package com.kamikadze328.mtstetaproject.di

import android.util.Log
import androidx.work.Configuration
import com.kamikadze328.mtstetaproject.app.MyDelegatedWorkedFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkManagerModule {
    @Singleton
    @Provides
    fun provideWorkManagerConfiguration(
        movieDelegatedWorkedFactory: MyDelegatedWorkedFactory
    ): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(movieDelegatedWorkedFactory)
            .build()
    }
}
package com.kamikadze328.mtstetaproject.di

import android.content.Context
import com.kamikadze328.mtstetaproject.app.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = AppDatabase.instance(app)

    @Inject
    @Provides
    fun provideMovieDao(
        appDatabase: AppDatabase
    ) = appDatabase.movieDao()

    @Inject
    @Provides
    fun provideGenreDao(
        appDatabase: AppDatabase
    ) = appDatabase.genreDao()

    @Inject
    @Provides
    fun provideActorDao(
        appDatabase: AppDatabase
    ) = appDatabase.actorDao()

    @Inject
    @Provides
    fun provideMovieGenreCrossRefDao(
        appDatabase: AppDatabase
    ) = appDatabase.movieGenreCrossRefDao()

    @Inject
    @Provides
    fun provideMovieActorCrossRefDao(
        appDatabase: AppDatabase
    ) = appDatabase.movieActorCrossRefDao()

}
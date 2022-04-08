package com.kamikadze328.mtstetaproject.di

import android.app.Application
import com.kamikadze328.mtstetaproject.app.AppDatabase
import com.kamikadze328.mtstetaproject.data.local.dao.ActorDao
import com.kamikadze328.mtstetaproject.data.local.dao.GenreDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieActorCrossRefDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieGenreCrossRefDao
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.data.repository.ActorRepository
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieDetailsRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {
    @Inject
    @Provides
    fun provideMovieRepository(
        webservice: Webservice,
        movieDao: MovieDao,
        genreRepository: GenreRepository,
        application: Application
    ) = MovieRepository(webservice, application, movieDao, genreRepository)

    @Inject
    @Provides
    fun provideActorRepository(
        webservice: Webservice,
        actorDao: ActorDao,
        application: Application,
        movieActorCrossRefDao: MovieActorCrossRefDao
    ) = ActorRepository(webservice, application, actorDao, movieActorCrossRefDao)

    @Inject
    @Provides
    fun provideGenreRepository(
        webservice: Webservice,
        genreDao: GenreDao,
        application: Application
    ) = GenreRepository(webservice, application, genreDao)

    @Inject
    @Provides
    fun provideMovieDetailsRepository(
        webservice: Webservice,
        movieRepository: MovieRepository,
        genreRepository: GenreRepository,
        actorRepository: ActorRepository,
        movieActorCrossRefDao: MovieActorCrossRefDao,
        movieGenreCrossRefDao: MovieGenreCrossRefDao,
        database: AppDatabase
    ) = MovieDetailsRepository(
        webservice,
        movieRepository,
        genreRepository,
        actorRepository,
        movieActorCrossRefDao,
        movieGenreCrossRefDao,
        database
    )
}
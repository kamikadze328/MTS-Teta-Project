package com.kamikadze328.mtstetaproject.repository

import android.app.Application
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.network.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val webservice: Webservice,
    private val application: Application
) {
    private val movieDetailsError: Movie by lazy {
        Movie(
            id = -1,
            title = application.resources.getString(R.string.movie_loading_error),
            overview = application.resources.getString(R.string.movie_description_loading_error),
            release_date = application.resources.getString(R.string.movie_date_loading),
            vote_average = 0.0,
            ageRestriction = application.resources.getString(R.string.age_restricting_loading),
            genre_ids = emptyList(),
            poster_path = ""
        )
    }

    private val movieDetailsLoading: Movie by lazy {
        Movie(
            id = -2,
            title = application.resources.getString(R.string.movie_name_loading),
            overview = application.resources.getString(R.string.movie_description_loading),
            release_date = application.resources.getString(R.string.movie_date_loading),
            vote_average = 0.0,
            ageRestriction = application.resources.getString(R.string.age_restricting_loading),
            genre_ids = emptyList(),
            poster_path = ""
        )
    }

    suspend fun refreshPopularMovies(): List<Movie> = withContext(Dispatchers.IO) {
        return@withContext webservice.getPopularMovies().slice(0..3)
    }

    suspend fun refreshMovie(movieId: Int): Movie? = withContext(Dispatchers.IO) {
        return@withContext webservice.getMovieById(movieId.toString())
    }

    fun loadMovieLoading(): Movie {
        return movieDetailsLoading
    }

    fun loadMovieError(): Movie {
        return movieDetailsError
    }
}
package com.kamikadze328.mtstetaproject.repository

import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.network.Webservice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val webservice: Webservice
) {
    suspend fun refreshPopularMovies(): List<Movie> {
        return webservice.getPopularMovies()
    }

    suspend fun refreshMovie(movieId: Int): Movie? {
        return webservice.getMovieById(movieId.toString())
    }
}
package com.kamikadze328.mtstetaproject.model

import com.kamikadze328.mtstetaproject.data.features.movies.MoviesDataSource

class MoviesModel(
    private val moviesDataSource: MoviesDataSource
) {
    fun getMovies() = moviesDataSource.getMovies()
}
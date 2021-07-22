package com.kamikadze328.mtstetaproject.model

import com.kamikadze328.mtstetaproject.data.features.movies.MoviesDataSource

class MoviesModel(
    private val moviesDataSource: MoviesDataSource
) {
    fun getMovies() = moviesDataSource.getMovies()
    fun getMovieById(id: Int) = moviesDataSource.getMovieById(id)
    fun getFavouriteMoviesByUserId(id: Int) = moviesDataSource.getFavouriteMoviesByUserId(id)

}
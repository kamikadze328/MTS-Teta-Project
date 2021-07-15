package com.kamikadze328.mtstetaproject.data.features.movies

import com.kamikadze328.mtstetaproject.data.dto.Movie

interface MoviesDataSource {
    fun getMovies(): List<Movie>
}
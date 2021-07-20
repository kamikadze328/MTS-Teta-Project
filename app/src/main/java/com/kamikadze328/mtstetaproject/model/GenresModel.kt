package com.kamikadze328.mtstetaproject.model

import com.kamikadze328.mtstetaproject.data.features.genres.GenresDataSource

class GenresModel(
    private val genresDataSource: GenresDataSource
) {
    fun getGenres() = genresDataSource.getGenres()
}
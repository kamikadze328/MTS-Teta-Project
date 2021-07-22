package com.kamikadze328.mtstetaproject.data.features.genres

import com.kamikadze328.mtstetaproject.data.dto.Genre

interface GenresDataSource {
    fun getGenres(): List<Genre>

    fun getGenreByIt(id: Int): Genre?
}
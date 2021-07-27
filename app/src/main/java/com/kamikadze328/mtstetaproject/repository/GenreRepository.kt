package com.kamikadze328.mtstetaproject.repository

import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.network.Webservice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val webservice: Webservice
) {

    suspend fun refreshGenres(): List<Genre> {
        return webservice.getGenres()
    }

}
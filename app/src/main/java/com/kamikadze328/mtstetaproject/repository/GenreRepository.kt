package com.kamikadze328.mtstetaproject.repository

import android.content.res.Resources
import com.kamikadze328.mtstetaproject.R
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

    fun getLoadingGenre(resources: Resources): Genre =
        Genre(resources.getString(R.string.genre_loading), -1)
}
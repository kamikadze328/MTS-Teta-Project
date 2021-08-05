package com.kamikadze328.mtstetaproject.repository

import android.app.Application
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.network.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val webservice: Webservice,
    private val application: Application
) {
    private val genreError: Genre by lazy {
        Genre(name = application.resources.getString(R.string.genre_loading_error), id = -1)
    }

    private val genreLoading: Genre by lazy {
        Genre(name = application.resources.getString(R.string.genre_loading), id = -2)
    }

    suspend fun refreshGenres(): List<Genre> = withContext(Dispatchers.IO) {
        return@withContext webservice.getGenres()
    }

    suspend fun loadGenresByIds(genre_ids: List<Int>): List<Genre> = withContext(Dispatchers.IO) {
        val allGenres = refreshGenres()
        return@withContext genre_ids.mapNotNull { genreId -> allGenres.find { genre -> genre.id == genreId } }
    }

    fun loadGenreLoading(): Genre {
        return genreLoading
    }

    fun loadGenreError(): Genre {
        return genreError
    }
}
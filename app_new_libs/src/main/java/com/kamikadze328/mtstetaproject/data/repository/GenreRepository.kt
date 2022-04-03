package com.kamikadze328.mtstetaproject.data.repository

import android.app.Application
import android.util.Log
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.local.dao.GenreDao
import com.kamikadze328.mtstetaproject.data.mapper.toGenres
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.data.util.SelectableGenreComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val webservice: Webservice,
    application: Application,
    private val genreDao: GenreDao
) {
    @Volatile
    private var wasGenresRefreshed: AtomicBoolean = AtomicBoolean(false)

    private val genreError: Genre by lazy {
        Genre(name = application.resources.getString(R.string.genre_loading_error), genreId = -1)
    }

    private val genreLoading: Genre by lazy {
        Genre(name = application.resources.getString(R.string.genre_loading), genreId = -2)
    }


    //return fresh genres only in the first time. After it will return cached genres
    suspend fun getAll(): List<Genre> = withContext(Dispatchers.IO) {
        val genres: List<Genre> =
            if (wasGenresRefreshed.get()) refreshAll() else genreDao.getGenre()
        wasGenresRefreshed.set(true)
        return@withContext genres
    }


    //always return fresh genres
    suspend fun refreshAll(): List<Genre> = withContext(Dispatchers.IO) {
        val genres =
            webservice.getGenres().genres.toGenres().sortedWith(SelectableGenreComparator())
        addAllLocal(genres)
        return@withContext genres
    }

    fun addAllLocal(genres: List<Genre>): List<Long> {
        return genreDao.insertAll(genres)
    }

    fun loadGenreLoading(): Genre {
        return genreLoading
    }

    fun loadGenreError(): Genre {
        return genreError
    }
}
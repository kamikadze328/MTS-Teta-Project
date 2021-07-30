package com.kamikadze328.mtstetaproject.repository

import android.app.Application
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.network.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val webservice: Webservice,
    private val application: Application
) {
    private val userError: User by lazy {
        User(
            id = -1, name = application.resources.getString(R.string.user_loading_error),
            password = "", email = "", phone = ""
        )
    }

    private val userLoading: User by lazy {
        User(
            id = -2, name = application.resources.getString(R.string.user_loading),
            password = "", email = "", phone = ""
        )
    }

    suspend fun refreshUser(accountId: Int): User = withContext(Dispatchers.IO) {
        return@withContext webservice.getAccountDetails(accountId.toString())
    }

    suspend fun getFavouriteGenres(accountId: Int): List<Genre> = withContext(Dispatchers.IO) {
        val allGenres = webservice.getGenres()
        val maxGenresCount = 3
        val countGenres =
            webservice.getUserFavouriteMovies(accountId.toString()).flatMap { it.genre_ids }
                .groupingBy { it }.eachCount().toList().sortedByDescending { (_, v) -> v }
                .subList(0, maxGenresCount - 1)

        val isMaxMoreOne = countGenres[0].second > 1
        return@withContext countGenres.filter { isMaxMoreOne && it.second > 1 }
            .mapNotNull { sorted -> allGenres.find { genre -> sorted.first == genre.id } }
    }

    fun loadUserLoading(): User {
        return userLoading
    }

    fun loadUserError(): User {
        return userError
    }
}
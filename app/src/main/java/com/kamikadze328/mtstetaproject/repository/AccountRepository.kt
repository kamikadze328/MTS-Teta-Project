package com.kamikadze328.mtstetaproject.repository

import android.util.Log
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.network.Webservice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val webservice: Webservice
) {

    suspend fun refreshUser(accountId: Int): User {
        Log.d("kek", "refreshUser")
        return webservice.getAccountDetails(accountId.toString())
    }

    suspend fun getFavouriteGenres(accountId: Int): List<Genre> {
        val allGenres = webservice.getGenres()
        val maxGenresCount = 3
        val countGenres =
            webservice.getUserFavouriteMovies(accountId.toString()).flatMap { it.genre_ids }
                .groupingBy { it }.eachCount().toList().sortedByDescending { (_, v) -> v }
                .subList(0, maxGenresCount - 1)

        val isMaxMoreOne = countGenres[0].second > 1
        return countGenres.filter { isMaxMoreOne && it.second > 1 }
            .map { sorted -> allGenres.find { genre -> sorted.first == genre.id }!! }
    }
}
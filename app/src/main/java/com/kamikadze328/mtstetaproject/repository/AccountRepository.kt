package com.kamikadze328.mtstetaproject.repository

import android.util.Log
import com.kamikadze328.mtstetaproject.data.dto.Movie
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

    suspend fun getFavouriteMovies(accountId: Int): List<Movie> {
        return webservice.getUserFavouriteMovies(accountId.toString())
    }


}
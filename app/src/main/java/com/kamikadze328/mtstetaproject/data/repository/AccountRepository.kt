package com.kamikadze328.mtstetaproject.data.repository


import android.app.Application
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.mapper.toUIMovie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.data.util.SharedPrefsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AccountRepository @Inject constructor(
    private val webservice: Webservice,
    private val application: Application,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) {
    val currentUser: FirebaseUser?
        get() = Firebase.auth.currentUser

    private val userError: User by lazy {
        User(
            id = "-1", name = application.resources.getString(R.string.user_loading_error),
            password = "", email = "", phone = "", photoUrl = Uri.EMPTY, emailVerified = true
        )
    }

    private val userLoading: User by lazy {
        User(
            id = "-2", name = application.resources.getString(R.string.user_loading),
            password = "", email = "", phone = "", photoUrl = Uri.EMPTY, emailVerified = true
        )
    }

    //TODO update and refresh user with FirebaseUser
    suspend fun refreshUser(accountId: Int): User = withContext(Dispatchers.IO) {
        return@withContext webservice.getAccountDetails(accountId.toString())
    }

    suspend fun getFavouriteGenres(accountId: String): List<Genre> = withContext(Dispatchers.IO) {
        val allGenres = genreRepository.getAll()
        val maxGenresCount = 3
        val favouriteMovies =
            webservice.getUserFavouriteMovies(accountId).map { it.toUIMovie(allGenres) }

        movieRepository.changeMoviesFavourite(favouriteMovies, true)

        val countGenres = favouriteMovies.flatMap { it.genres }
            .groupingBy { it }.eachCount().toList()
            .sortedByDescending { (_, v) -> v }
            .subList(0, maxGenresCount - 1)

        val isMaxMoreOne = countGenres[0].second > 1
        return@withContext countGenres.filter { isMaxMoreOne && it.second > 1 }.map { it.first }
    }

    fun saveToken(token: String) {
        addSecurePrefsValue(THE_MOVIE_DB_TOKEN, token)
    }

    fun getToken(): String? {
        return getSecurePrefsValue(THE_MOVIE_DB_TOKEN)
    }

    private fun addSecurePrefsValue(key: String, value: String) {
        val prefs = SharedPrefsUtils.createSecure(application.applicationContext)
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getSecurePrefsValue(key: String): String? {
        val prefs = SharedPrefsUtils.createSecure(application.applicationContext)
        return prefs.getString(key, null)
    }


    fun isLogin(): Boolean {
        return currentUser != null
    }

    fun logout() {
        Log.d("kek", "logout")
        movieRepository.setAllNotFavourite()
        Firebase.auth.signOut()
    }

    fun loadUserLoading(): User {
        return userLoading
    }

    fun loadUserError(): User {
        return userError
    }

    companion object {
        const val THE_MOVIE_DB_TOKEN = "th_mv_db_tkn"

    }
}
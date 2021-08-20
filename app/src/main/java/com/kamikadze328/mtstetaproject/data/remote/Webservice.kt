package com.kamikadze328.mtstetaproject.data.remote

import com.kamikadze328.mtstetaproject.BuildConfig
import com.kamikadze328.mtstetaproject.data.remote.dao.CreditResponse
import com.kamikadze328.mtstetaproject.data.remote.dao.GenresResponse
import com.kamikadze328.mtstetaproject.data.remote.dao.MovieRemote
import com.kamikadze328.mtstetaproject.data.remote.dao.MovieRemoteShortResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface Webservice {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_PATH_IMAGE_SMALL_URL = "https://www.themoviedb.org/t/p/w300"
        const val BASE_PATH_IMAGE_URL = "https://www.themoviedb.org/t/p/original"

        private const val API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY
        private const val API_KEY_ARG = "api_key"
        fun create(): Webservice {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .setClient(Pair(API_KEY_ARG, API_KEY))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Webservice::class.java)
        }
    }

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("movie/popular")
    suspend fun getMoviesPopular(): MovieRemoteShortResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path(value = "movie_id") movieId: Long): MovieRemote

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieDetailsCredits(@Path(value = "movie_id") movieId: Long): CreditResponse
}
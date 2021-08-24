package com.kamikadze328.mtstetaproject.data.repository

import android.app.Application
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieDao
import com.kamikadze328.mtstetaproject.data.mapper.toUIMovie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val webservice: Webservice,
    application: Application,
    private val movieDao: MovieDao,
    private val genreRepository: GenreRepository
) {
    private val movieDetailsError: Movie by lazy {
        Movie(
            movieId = -1,
            title = application.resources.getString(R.string.movie_loading_error),
            overview = application.resources.getString(R.string.movie_description_loading_error),
            release_date = application.resources.getString(R.string.movie_date_loading),
            vote_average = 0.0,
            age_restriction = application.resources.getString(R.string.age_restricting_loading),
            poster_path = ""
        )
    }

    private val movieDetailsLoading: Movie by lazy {
        Movie(
            movieId = -2,
            title = application.resources.getString(R.string.movie_name_loading),
            overview = application.resources.getString(R.string.movie_description_loading),
            release_date = application.resources.getString(R.string.movie_date_loading),
            vote_average = 0.0,
            age_restriction = application.resources.getString(R.string.age_restricting_loading),
            poster_path = ""
        )
    }

    suspend fun refreshPopularMovies(): List<Movie> = withContext(Dispatchers.IO) {
        val allGenres = genreRepository.getAll()
        val movies = webservice.getMoviesPopular().results.map { it.toUIMovie(allGenres) }

        addAllLocal(movies)
        return@withContext movies
    }

    fun addLocal(movie: Movie): Long {
        return movieDao.insert(movie)
    }

    fun addAllLocal(movies: List<Movie>): List<Long> {
        return movieDao.insertAll(movies)
    }

    fun addLocalWithTime(movie: Movie): Long {
        return movieDao.insertWithTime(movie)
    }

    fun hasMovieLocal(movieId: Long): Boolean {
        return movieDao.hasMovie(movieId, FRESH_TIMEOUT)
    }

    fun setAllNotFavourite() {
        movieDao.changeMoviesFavourite()
    }

    fun changeMovieFavourite(movieId: Long, isFavourite: Boolean) {
        movieDao.changeMovieFavourite(movieId, isFavourite)
    }

    fun changeMoviesFavourite(movies: List<Movie>, isFavourite: Boolean) {
        movieDao.changeMoviesFavourite(movies.map { it.movieId }, isFavourite)
    }

    fun getAllFavourite() = movieDao.getAllFavourite()

    fun getMovieWithDetails(movieId: Long): Movie? {
        return movieDao.findMovieWithGenresAndActorsById(movieId)
    }

    fun getMovieLoading(): Movie {
        return movieDetailsLoading
    }

    fun getMovieError(): Movie {
        return movieDetailsError
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
    }
}
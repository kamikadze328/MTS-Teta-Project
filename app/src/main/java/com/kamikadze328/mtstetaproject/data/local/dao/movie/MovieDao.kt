package com.kamikadze328.mtstetaproject.data.local.dao.movie

import androidx.room.*
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.dao.BaseDao
import com.kamikadze328.mtstetaproject.data.local.dto.MovieWithActors
import com.kamikadze328.mtstetaproject.data.local.dto.MovieWithGenres
import com.kamikadze328.mtstetaproject.data.mapper.toUIMovie
import javax.inject.Singleton


@Singleton
@Dao
interface MovieDao : BaseDao<Movie> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWithTime(movie: Movie): Long {
        movie.apply {
            updateTime = System.currentTimeMillis()
        }
        return insert(movie)
    }

    @Query("SELECT updateTime FROM Movie WHERE movieId = :movieId")
    fun getMovieUpdateTime(movieId: Long): Long

    fun hasMovie(movieId: Long, timeout: Long): Boolean =
        System.currentTimeMillis() - getMovieUpdateTime(movieId) <= timeout

    @Transaction
    @Query("SELECT * FROM Movie WHERE movieId == :movieId")
    fun findMovieWithGenresById(movieId: Long): MovieWithGenres?

    @Transaction
    @Query("SELECT * FROM Movie WHERE movieId == :movieId")
    fun findMovieWithActorsById(movieId: Long): MovieWithActors?

    //slow and waste memory, but don't need to create special data classes
    //TODO normal query
    @Transaction
    @Query("SELECT * FROM Movie WHERE movieId == :movieId")
    fun findMovieWithGenresAndActorsById(movieId: Long): Movie? {
        val movieWithActors = findMovieWithActorsById(movieId)
        val movieWithGenres = findMovieWithGenresById(movieId)

        return toUIMovie(movieWithGenres, movieWithActors)
    }

    @Transaction
    @Query("SELECT * FROM Movie")
    fun getMoviesWithGenres(): List<MovieWithGenres>

    @Query("SELECT * FROM Movie WHERE movieId == :movieId")
    fun findMovieById(movieId: Long): Movie?

    @Query("UPDATE Movie SET isFavourite = :isFavourite WHERE movieId = :movieId")
    fun changeMovieFavourite(movieId: Long, isFavourite: Boolean)

    @Query("UPDATE Movie SET isFavourite = 0")
    fun changeMoviesFavourite()

    @Query("UPDATE Movie SET isFavourite = :isFavourite WHERE movieId in (:moviesId)")
    fun changeMoviesFavourite(moviesId: List<Long>, isFavourite: Boolean)

    @Query("SELECT * FROM Movie WHERE isFavourite = 1")
    fun getAllFavourite(): List<Movie>
}
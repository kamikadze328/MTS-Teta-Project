package com.kamikadze328.mtstetaproject.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.local.dto.GenreWithMovies
import javax.inject.Singleton

@Singleton
@Dao
interface GenreDao : BaseDao<Genre> {

    @Query("SELECT * FROM Genre")
    fun getGenre(): List<Genre>

    @Transaction
    @Query("SELECT * FROM Genre")
    fun getGenreWithMovies(): List<GenreWithMovies>

    @Query("SELECT * FROM Genre WHERE genreId = :genreId")
    fun getGenreById(genreId: Long): Genre?


    /*@Transaction
    @Query("SELECT * FROM MovieGenreCrossRef JOIN Genre ON movieId = :movieId")
    fun findGenresByMovieId(movieId: Long): List<GenreWithMoviesId>*/
}
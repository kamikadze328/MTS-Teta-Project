package com.kamikadze328.mtstetaproject.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieActorCrossRefDao
import com.kamikadze328.mtstetaproject.data.local.dto.ActorWithMovies
import com.kamikadze328.mtstetaproject.data.local.entity.MovieActorCrossRef
import javax.inject.Singleton

@Singleton
@Dao
interface ActorDao : BaseDao<Actor> {

    @Transaction
    fun insertAllWithMovieId(
        movieId: Long,
        actors: List<Actor>,
        movieActorCrossRefDao: MovieActorCrossRefDao
    ) {
        val actorsId = insertAll(actors)
        val crossRefs = actorsId.map { MovieActorCrossRef(movieId, it) }
        movieActorCrossRefDao.insertAll(crossRefs)
    }

    @Query("SELECT * FROM Actor")
    fun getAll(): List<Actor>

    @Transaction
    @Query("SELECT * FROM Actor")
    fun getWithMovies(): List<ActorWithMovies>

    @Query("SELECT * FROM Actor WHERE actorId = :actorId")
    fun getById(actorId: Long): Actor?
}
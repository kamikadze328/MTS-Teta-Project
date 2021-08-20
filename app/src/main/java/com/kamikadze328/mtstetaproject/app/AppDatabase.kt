package com.kamikadze328.mtstetaproject.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.dao.ActorDao
import com.kamikadze328.mtstetaproject.data.local.dao.GenreDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieActorCrossRefDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieGenreCrossRefDao
import com.kamikadze328.mtstetaproject.data.local.entity.MovieActorCrossRef
import com.kamikadze328.mtstetaproject.data.local.entity.MovieGenreCrossRef
import javax.inject.Singleton

@Singleton
@Database(
    entities = [Movie::class, Genre::class, Actor::class, MovieActorCrossRef::class, MovieGenreCrossRef::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun actorDao(): ActorDao
    abstract fun movieActorCrossRefDao(): MovieActorCrossRefDao
    abstract fun movieGenreCrossRefDao(): MovieGenreCrossRefDao

    companion object {
        private const val DATABASE_NAME = "Movies.db"

        private var instance: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            //.apply { clearAllTables() }
            return instance!!
        }

    }

}
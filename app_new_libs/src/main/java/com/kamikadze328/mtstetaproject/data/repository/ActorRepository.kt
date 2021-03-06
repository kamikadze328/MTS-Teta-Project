package com.kamikadze328.mtstetaproject.data.repository

import android.app.Application
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.local.dao.ActorDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieActorCrossRefDao
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorRepository @Inject constructor(
    private val webservice: Webservice,
    application: Application,
    private val actorsDao: ActorDao,
    private val movieActorCrossRefDao: MovieActorCrossRefDao
) {

    private val actorError: Actor by lazy {
        Actor(
            id = -1,
            name = application.resources.getString(R.string.actors_loading_error)
        ).apply {
            local_profile_res_id = R.drawable.ic_baseline_image_not_supported_24
        }
    }

    private val actorLoading: Actor by lazy {
        Actor(
            id = -2,
            name = application.resources.getString(R.string.actors_loading)
        ).apply {
            local_profile_res_id = R.drawable.ic_baseline_face_24
        }
    }

    suspend fun getByMovie(movieId: Long): List<Actor> = withContext(Dispatchers.IO) {
        val actors = webservice.getMovieDetailsCredits(movieId).cast
        //val actors = webservice.getActorsByMovieId(movieId.toString())
        addLocalWithMovieId(movieId, actors)
        return@withContext actors
    }

    fun addLocalWithMovieId(movieId: Long, actors: List<Actor>) {
        actorsDao.insertAllWithMovieId(movieId, actors, movieActorCrossRefDao)
    }

    fun addAllLocal(actors: List<Actor>): List<Long> {
        return actorsDao.insertAll(actors)
    }

    fun loadActorLoading(): Actor {
        return actorLoading
    }

    fun loadActorError(): Actor {
        return actorError
    }
}
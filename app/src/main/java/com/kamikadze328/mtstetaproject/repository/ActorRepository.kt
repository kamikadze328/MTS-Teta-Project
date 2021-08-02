package com.kamikadze328.mtstetaproject.repository

import android.app.Application
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.network.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorRepository @Inject constructor(
    private val webservice: Webservice,
    application: Application
) {

    private val actorError: Actor by lazy {
        Actor(
            avatarIcon = R.drawable.ic_baseline_image_not_supported_24,
            name = application.resources.getString(R.string.actors_loading_error)
        )

    }

    private val actorLoading: Actor by lazy {
        Actor(
            avatarIcon = R.drawable.ic_baseline_face_24,
            name = application.resources.getString(R.string.actors_loading)
        )
    }

    suspend fun getActorsByMovieId(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
        return@withContext webservice.getActorsByMovieId(movieId.toString())
    }

    fun loadActorLoading(): Actor {
        return actorLoading
    }

    fun loadActorError(): Actor {
        return actorError
    }
}
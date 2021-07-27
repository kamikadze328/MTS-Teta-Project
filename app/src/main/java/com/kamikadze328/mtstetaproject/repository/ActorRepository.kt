package com.kamikadze328.mtstetaproject.repository

import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.network.Webservice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorRepository @Inject constructor(
    private val webservice: Webservice
) {

    suspend fun getActorsByMovieId(movieId: Int): List<Actor> {
        return webservice.getActorsByMovieId(movieId.toString())
    }
}
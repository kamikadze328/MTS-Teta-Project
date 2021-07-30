package com.kamikadze328.mtstetaproject.repository

import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.network.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorRepository @Inject constructor(
    private val webservice: Webservice
) {

    suspend fun getActorsByMovieId(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
        return@withContext webservice.getActorsByMovieId(movieId.toString())
    }
}
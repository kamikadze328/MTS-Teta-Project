package com.kamikadze328.mtstetaproject.model

import com.kamikadze328.mtstetaproject.data.features.actors.ActorsDataSource

class ActorsModel(
    private val actorsDataSource: ActorsDataSource
) {
    fun getActors() = actorsDataSource.getActors()
}

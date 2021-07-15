package com.kamikadze328.mtstetaproject.data.features.actors

import com.kamikadze328.mtstetaproject.data.dto.Actor

interface ActorsDataSource {
    fun getActors(): List<Actor>
}
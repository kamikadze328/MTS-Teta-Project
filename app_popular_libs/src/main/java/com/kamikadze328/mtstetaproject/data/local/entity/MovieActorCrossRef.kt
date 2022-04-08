package com.kamikadze328.mtstetaproject.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["movieId", "actorId"],
    indices = [Index(value = ["movieId"]), Index(value = ["actorId"])]
)
class MovieActorCrossRef(
    val movieId: Long,
    val actorId: Long
)
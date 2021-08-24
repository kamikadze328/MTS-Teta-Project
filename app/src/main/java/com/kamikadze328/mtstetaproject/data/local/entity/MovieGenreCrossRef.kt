package com.kamikadze328.mtstetaproject.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["movieId", "genreId"],
    indices = [Index(value = ["movieId"]), Index(value = ["genreId"])]
)
class MovieGenreCrossRef(
    val movieId: Long,
    val genreId: Long
)
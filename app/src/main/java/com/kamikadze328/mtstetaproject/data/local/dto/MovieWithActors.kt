package com.kamikadze328.mtstetaproject.data.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.entity.MovieActorCrossRef

data class MovieWithActors(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "actorId",
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val actors: List<Actor>
)
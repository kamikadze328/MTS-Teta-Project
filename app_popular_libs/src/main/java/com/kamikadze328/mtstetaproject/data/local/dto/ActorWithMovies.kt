package com.kamikadze328.mtstetaproject.data.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.entity.MovieActorCrossRef

class ActorWithMovies(
    @Embedded val actor: Actor,
    @Relation(
        parentColumn = "actorId",
        entityColumn = "movieId",
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val movie: List<Movie>
)
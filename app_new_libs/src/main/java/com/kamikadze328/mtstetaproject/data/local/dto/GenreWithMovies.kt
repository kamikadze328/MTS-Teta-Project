package com.kamikadze328.mtstetaproject.data.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.entity.MovieGenreCrossRef

data class GenreWithMovies(
    @Embedded val genre: Genre,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "movieId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val movie: List<Movie>
)

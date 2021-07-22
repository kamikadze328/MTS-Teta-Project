package com.kamikadze328.mtstetaproject.data.dto

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val vote_average: Double,
    val ageRestriction: Int,
    val genre_ids: List<Int>,
    val poster_path: String
)
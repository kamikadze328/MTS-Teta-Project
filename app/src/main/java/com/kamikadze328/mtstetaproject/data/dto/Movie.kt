package com.kamikadze328.mtstetaproject.data.dto

data class Movie(
    val title: String,
    val description: String,
    val rateScore: Int,
    val ageRestriction: Int,
    val imageUrl: String
)
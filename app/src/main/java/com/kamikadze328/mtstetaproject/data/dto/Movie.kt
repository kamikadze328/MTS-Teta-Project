package com.kamikadze328.mtstetaproject.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double,
    val ageRestriction: String,
    val genre_ids: List<Int>,
    val poster_path: String
) : Parcelable
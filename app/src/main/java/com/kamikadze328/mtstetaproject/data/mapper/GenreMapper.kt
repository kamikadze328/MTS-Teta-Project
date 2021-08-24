package com.kamikadze328.mtstetaproject.data.mapper

import com.kamikadze328.mtstetaproject.data.dto.Genre

fun Int.toGenre(allGenres: List<Genre>) =
    allGenres.find { it.genreId == toLong() }

fun List<Int>.toGenres(allGenres: List<Genre>) =
    mapNotNull { genreId -> genreId.toGenre(allGenres) }
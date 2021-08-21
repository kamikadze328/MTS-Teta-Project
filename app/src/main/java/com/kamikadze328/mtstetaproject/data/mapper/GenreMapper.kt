package com.kamikadze328.mtstetaproject.data.mapper

import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.remote.dao.GenreRemote

fun Int.toGenre(allGenres: List<Genre>) =
    allGenres.find { it.genreId == toLong() }

fun List<Int>.toGenres(allGenres: List<Genre>) =
    mapNotNull { genreId -> genreId.toGenre(allGenres) }

fun GenreRemote.toGenre() =
    Genre(genreId = id, name = name)

fun List<GenreRemote>.toGenres() =
    map { it.toGenre() }
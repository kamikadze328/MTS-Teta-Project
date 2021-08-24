package com.kamikadze328.mtstetaproject.data.mapper

import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.dto.MovieWithActors
import com.kamikadze328.mtstetaproject.data.local.dto.MovieWithGenres
import com.kamikadze328.mtstetaproject.data.remote.dao.MovieRemote
import com.kamikadze328.mtstetaproject.data.remote.dao.MovieShortRemote

fun MovieShortRemote.toUIMovie(genres: List<Genre>): Movie = Movie(
    movieId = id.toLong(),
    title = title,
    overview = overview,
    release_date = release_date,
    vote_average = vote_average,
    age_restriction = if (adult) "18+" else "6+",
    poster_path = poster_path
).apply {
    this.genres =
        this@toUIMovie.genre_ids.toGenres(allGenres = genres)
    this.actors = emptyList()
}

fun MovieRemote.toUIMovie(actors: List<Actor>): Movie = Movie(
    movieId = id.toLong(),
    title = title,
    overview = overview,
    release_date = release_date,
    vote_average = vote_average,
    age_restriction = if (adult) "18+" else "6+",
    poster_path = poster_path,
).apply {
    this.genres = this@toUIMovie.genres
    this.actors = actors
}

fun MovieWithGenres.toUIMovie(): Movie = movie.apply { genres = this@toUIMovie.genres }
fun MovieWithActors.toUIMovie(): Movie = movie.apply { actors = this@toUIMovie.actors }

fun Movie.toMovieWithGenres(): MovieWithGenres = MovieWithGenres(
    movie = this,
    genres = this.genres
)

fun toUIMovie(withGenres: MovieWithGenres?, withActors: MovieWithActors?): Movie? {
    return when {
        withGenres != null -> withGenres.toUIMovie()
            .apply { actors = withActors?.actors ?: emptyList() }
        withActors != null -> withActors.toUIMovie().apply { genres = emptyList() }
        else -> null
    }
}


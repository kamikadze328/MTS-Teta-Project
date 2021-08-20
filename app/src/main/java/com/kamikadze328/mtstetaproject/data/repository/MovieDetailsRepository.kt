package com.kamikadze328.mtstetaproject.data.repository

import com.kamikadze328.mtstetaproject.app.AppDatabase
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieActorCrossRefDao
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieGenreCrossRefDao
import com.kamikadze328.mtstetaproject.data.local.entity.MovieActorCrossRef
import com.kamikadze328.mtstetaproject.data.local.entity.MovieGenreCrossRef
import com.kamikadze328.mtstetaproject.data.mapper.toUIMovie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.data.util.isFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsRepository @Inject constructor(
    private val webservice: Webservice,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository,
    private val actorRepository: ActorRepository,
    private val movieActorCrossRefDao: MovieActorCrossRefDao,
    private val movieGenreCrossRefDao: MovieGenreCrossRefDao,
    private val database: AppDatabase
) {
    private suspend fun updateMovieLocal(movie: Movie) {
        if (movie.isFully()) {
            database.runInTransaction {
                val id = movieRepository.addLocalWithTime(movie)
                val genresId = genreRepository.addAllLocal(movie.genres)
                val actorId = actorRepository.addAllLocal(movie.actors)

                val crossRefsGenres = genresId.map { MovieGenreCrossRef(id, it) }
                val crossRefsActors = actorId.map { MovieActorCrossRef(id, it) }

                movieGenreCrossRefDao.insertAll(crossRefsGenres)
                movieActorCrossRefDao.insertAll(crossRefsActors)
            }
        } else {
            movieRepository.addLocal(movie)
        }
    }

    suspend fun refreshMovie(movieId: Long): Movie = withContext(Dispatchers.IO) {
        if (movieRepository.hasMovieLocal(movieId)) {
            val cachedMovie = movieRepository.getMovieWithDetails(movieId)

            if (cachedMovie?.isFully() == true) return@withContext cachedMovie
        }
        val actors = actorRepository.getByMovie(movieId)
        val freshMovie = webservice.getMovieDetails(movieId).toUIMovie(actors)

        updateMovieLocal(freshMovie)

        return@withContext freshMovie
    }

    suspend fun loadRandomPopularMovie(): Movie? = withContext(Dispatchers.IO) {
        return@withContext webservice.getMoviesPopular().results.getOrNull(0)
            ?.toUIMovie(genreRepository.getAll())
    }

    fun getMovieLoading(): Movie = movieRepository.getMovieLoading()
    fun getMovieError(): Movie = movieRepository.getMovieError()
}

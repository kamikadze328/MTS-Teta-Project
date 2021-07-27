package com.kamikadze328.mtstetaproject.presentation.moviedetails

import android.util.Log
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.repository.ActorRepository
import com.kamikadze328.mtstetaproject.repository.GenreRepository
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val movieId: MutableLiveData<Int> = MutableLiveData(savedStateHandle[MOVIE_ID_ARG])

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> = _genres

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> = _actors

    companion object {
        const val MOVIE_ID_ARG = "mid"
    }

    fun setMovieId(newMovieId: Int) {
        movieId.value = newMovieId
        savedStateHandle.set(MOVIE_ID_ARG, newMovieId)
        init()
    }

    private fun init() {
        Log.d("kek", "MovieDetailsViewModel init")
        loadMovie()
        loadActors()
        Log.d("kek", "MovieDetailsViewModel init end")
    }


    private fun loadMovie() {
        Log.d("kek", "MovieDetailsViewModel loadMovie")
        viewModelScope.launch(Dispatchers.IO) {
            var allGenres: List<Genre> = emptyList()
            val genreJob = viewModelScope.launch(Dispatchers.IO) {
                allGenres = loadGenres()
            }

            val newMovieState = movieRepository.refreshMovie(movieId.value!!)

            _movie.postValue(newMovieState)

            genreJob.join()
            if (newMovieState != null) {
                _genres.postValue(newMovieState.genre_ids.map { genreId ->
                    allGenres.find { genre -> genre.id == genreId }!!
                })
            }
        }
        Log.d("kek", "MovieDetailsViewModel loadMovie end")
    }


    private suspend fun loadGenres() = genreRepository.refreshGenres()


    private fun loadActors() {
        Log.d("kek", "MovieDetailsViewModel loadActors")
        viewModelScope.launch(Dispatchers.IO) {
            _actors.postValue(actorRepository.getActorsByMovieId(movieId.value!!))
        }
        Log.d("kek", "MovieDetailsViewModel loadActors end")
    }
}
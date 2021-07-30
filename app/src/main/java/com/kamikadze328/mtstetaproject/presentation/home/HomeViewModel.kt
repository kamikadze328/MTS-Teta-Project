package com.kamikadze328.mtstetaproject.presentation.home

import android.util.Log
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.presentation.State
import com.kamikadze328.mtstetaproject.repository.GenreRepository
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val _moviesState: MutableLiveData<State<List<Movie>>> =
        MutableLiveData(State.LoadingState)
    val moviesState: LiveData<State<List<Movie>>> = _moviesState

    private val _genresState: MutableLiveData<State<List<Genre>>> =
        MutableLiveData(State.LoadingState)
    val genresState: LiveData<State<List<Genre>>> = _genresState

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onMoviesLoadFailed)
    }
    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onGenresLoadFailed)
    }

    var kek = 0

    init {
        loadAllData()
    }

    fun loadAllData() {
        loadMovies()
        loadGenres()
    }

    private fun onMoviesLoadFailed(context: CoroutineContext, exception: Throwable) {
        _moviesState.postValue(State.ErrorState(exception))
    }

    private fun loadMovies() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _moviesState.postValue(State.LoadingState)
            val movies = movieRepository.refreshPopularMovies()
            _moviesState.postValue(State.DataState(movies))
        }
    }

    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        _genresState.postValue(State.ErrorState(exception))
    }

    private fun loadGenres() {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _genresState.postValue(State.LoadingState)
            val genres = genreRepository.refreshGenres()
            _genresState.postValue(State.DataState(genres))
        }
    }

    fun loadGenreLoading(): List<Genre> {
        return listOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): List<Genre> {
        return listOf(genreRepository.loadGenreError())
    }
}
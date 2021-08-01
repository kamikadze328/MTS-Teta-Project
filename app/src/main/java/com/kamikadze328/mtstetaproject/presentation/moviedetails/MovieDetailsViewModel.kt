package com.kamikadze328.mtstetaproject.presentation.moviedetails

import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.presentation.State
import com.kamikadze328.mtstetaproject.repository.ActorRepository
import com.kamikadze328.mtstetaproject.repository.GenreRepository
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    //TODO initial args in savedstatehandle
    private val movieId: MutableLiveData<Int> = MutableLiveData(savedStateHandle[MOVIE_ID_ARG])

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> = _actors

    private val _movieState: MutableLiveData<State<Movie>> = MutableLiveData(State.LoadingState)
    val movieState: LiveData<State<Movie>> = _movieState

    private val _genresState: MutableLiveData<State<List<Genre>>> =
        MutableLiveData(State.LoadingState)
    val genresState: LiveData<State<List<Genre>>> = _genresState

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onMoviesLoadFailed)
    }
    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onGenresLoadFailed)
    }


    companion object {
        const val MOVIE_ID_ARG = "mid"
    }

    private fun onMoviesLoadFailed(context: CoroutineContext, exception: Throwable) {
        _movieState.postValue(State.ErrorState(exception))
        onGenresLoadFailed(context, exception)
    }

    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        _genresState.postValue(State.ErrorState(exception))
    }

    fun setMovieId(newMovieId: Int) {
        movieId.value = newMovieId
        savedStateHandle.set(MOVIE_ID_ARG, newMovieId)
        init()
    }

    private fun init() {
        loadMovie()
        loadActors()
    }

    private fun getMovieId() = movieId.value!!


    private fun loadMovie() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _movieState.postValue(State.LoadingState)
            val newMovieState = movieRepository.refreshMovie(getMovieId())
            if (newMovieState != null) {
                _movieState.postValue(State.DataState(newMovieState))
                loadGenresByIds(newMovieState.genre_ids)
            } else {
                throw Exception("Can't load the movie")
            }
        }
    }

    private suspend fun loadGenresByIds(genre_ids: List<Int>) {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _genresState.postValue(State.LoadingState)
            val genres = genreRepository.loadGenresByIds(genre_ids)
            _genresState.postValue(State.DataState(genres))
        }
    }

    private fun loadActors() {
        viewModelScope.launch(Dispatchers.IO) {
            _actors.postValue(actorRepository.getActorsByMovieId(getMovieId()))
        }
    }

    fun loadGenreLoading(): List<Genre> {
        return listOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): List<Genre> {
        return listOf(genreRepository.loadGenreError())
    }

    fun loadMovieLoading(): Movie {
        return movieRepository.loadMovieLoading()
    }

    fun loadMovieError(): Movie {
        return movieRepository.loadMovieError()
    }

}
package com.kamikadze328.mtstetaproject.presentation.moviedetails

import android.util.Log
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.repository.ActorRepository
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieDetailsRepository
import com.kamikadze328.mtstetaproject.data.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieDetailsRepository: MovieDetailsRepository,
    private val actorRepository: ActorRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val movieId: Long =
        MovieDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).movieId

    private val _movieState: MutableLiveData<UIState<Movie>> = MutableLiveData(UIState.LoadingState)
    val movieState: LiveData<UIState<Movie>> = _movieState

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onMoviesLoadFailed)
    }

    init {

        val movie: Movie? = savedStateHandle[MOVIE]

        val isMovieNotCached = movie == null

        if (isMovieNotCached) loadMovie()

        viewModelScope.launch {
            if (!isMovieNotCached) setMovie(movie!!)
        }
    }

    companion object {
        private const val MOVIE = "mv"
    }

    private fun getMovieId() = movieId

    private fun onMoviesLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", "$exception")
        Log.d("kek", exception.stackTraceToString())
        _movieState.postValue(UIState.ErrorState(exception))
    }

    private fun loadMovie() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _movieState.postValue(UIState.LoadingState)
            val newMovieState = movieDetailsRepository.refreshMovie(getMovieId())
                ?: throw Exception("Can't load the movie")

            setMovie(newMovieState)
        }
    }

    private suspend fun setMovie(movie: Movie) = withContext(Dispatchers.Default) {
        _movieState.postValue(UIState.DataState(movie))
        savedStateHandle.set(MOVIE, movie)
    }


    fun loadGenreLoading(): List<Genre> {
        return listOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): List<Genre> {
        return listOf(genreRepository.loadGenreError())
    }

    fun loadMovieLoading(): Movie {
        return movieDetailsRepository.getMovieLoading()
    }

    fun loadMovieError(): Movie {
        return movieDetailsRepository.getMovieError()
    }

    fun loadActorsLoading(): List<Actor> {
        return listOf(actorRepository.loadActorLoading())
    }

    fun loadActorsError(): List<Actor> {
        return listOf(actorRepository.loadActorError())
    }

}
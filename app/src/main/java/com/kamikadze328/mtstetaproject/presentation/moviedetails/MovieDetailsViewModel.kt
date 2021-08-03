package com.kamikadze328.mtstetaproject.presentation.moviedetails

import android.util.Log
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
import kotlinx.coroutines.withContext
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

    private val _actorsState = MutableLiveData<State<List<Actor>>>(State.LoadingState)
    val actorsState: LiveData<State<List<Actor>>> = _actorsState

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
    private val actorsCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onActorsLoadFailed)
    }


    fun setMovieId(newMovieId: Int) {
        movieId.value = newMovieId
        savedStateHandle.set(MOVIE_ID_ARG, newMovieId)
        init()
    }

    private fun init() {
        val movie: Movie? = savedStateHandle[MOVIE]
        val genres: List<Genre>? = savedStateHandle[GENRES]
        val actors: List<Actor>? = savedStateHandle[ACTORS]

        val isMovieNotCached = movie == null
        val isGenresNotCached = genres == null
        val isActorsNotCached = actors == null

        if (isMovieNotCached) loadMovie()
        if (!isMovieNotCached && isGenresNotCached) loadGenresByIds(movie!!.genre_ids)
        if (!isMovieNotCached && isActorsNotCached) loadActors()

        viewModelScope.launch {
            if (!isMovieNotCached) setMovie(movie!!)
            if (!isGenresNotCached) setGenres(genres!!)
            if (!isActorsNotCached) setActors(actors!!)
        }

        Log.d("kek", "init movie details View model")

    }

    companion object {
        private const val MOVIE_ID_ARG = "mid"
        private const val MOVIE = "mv"
        private const val GENRES = "gnrs"
        private const val ACTORS = "actrs"
    }

    private fun getMovieId() = movieId.value!!

    private fun onMoviesLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", "$exception")
        _movieState.postValue(State.ErrorState(exception))
        onActorsLoadFailed(context, exception)
        onGenresLoadFailed(context, exception)
    }

    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", "$exception")
        _genresState.postValue(State.ErrorState(exception))
    }

    private fun onActorsLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", "$exception")
        _actorsState.postValue(State.ErrorState(exception))
    }

    private fun loadMovie() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _movieState.postValue(State.LoadingState)
            val newMovieState = movieRepository.refreshMovie(getMovieId())
                ?: throw Exception("Can't load the movie")

            setMovie(newMovieState)
            loadGenresByIds(newMovieState.genre_ids)
            loadActors()
        }
    }

    private fun loadGenresByIds(genre_ids: List<Int>) {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _genresState.postValue(State.LoadingState)
            val genres = genreRepository.loadGenresByIds(genre_ids)
            setGenres(genres)
        }
    }

    private fun loadActors() {
        viewModelScope.launch(actorsCoroutineExceptionHandler) {
            _actorsState.postValue(State.LoadingState)
            val actors = actorRepository.getActorsByMovieId(getMovieId())
            setActors(actors)
        }
    }

    private suspend fun setMovie(movie: Movie) = withContext(Dispatchers.Default) {
        _movieState.postValue(State.DataState(movie))
        savedStateHandle.set(MOVIE, movie)
    }

    private suspend fun setGenres(genres: List<Genre>) = withContext(Dispatchers.Default) {
        _genresState.postValue(State.DataState(genres))
        savedStateHandle.set(GENRES, genres)
    }

    private suspend fun setActors(actors: List<Actor>) = withContext(Dispatchers.Default) {
        _actorsState.postValue(State.DataState(actors))
        savedStateHandle.set(ACTORS, actors)
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

    fun loadActorsLoading(): List<Actor> {
        return listOf(actorRepository.loadActorLoading())
    }

    fun loadActorsError(): List<Actor> {
        return listOf(actorRepository.loadActorError())
    }

}
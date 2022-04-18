package com.kamikadze328.mtstetaproject.presentation.movies

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.main.CallbackGenreClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) : ViewModel(), CallbackGenreClicked {
    private val _moviesState: MutableLiveData<UIState<List<Movie>>> =
        MutableLiveData(UIState.LoadingState)
    val moviesState: LiveData<UIState<List<Movie>>> = _moviesState

    private val _genresState: MutableLiveData<UIState<SnapshotStateList<Genre>>> =
        MutableLiveData(UIState.LoadingState)
    val genresState: LiveData<UIState<SnapshotStateList<Genre>>> = _genresState

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onMoviesLoadFailed(throwable) }
    }

    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onGenresLoadFailed(throwable) }
    }

    private val _selectedGenresId: MutableSet<Long> = mutableSetOf()

    private var _movies: List<Movie> = emptyList()

    init {
        val movies: List<Movie>? = savedStateHandle[MOVIES]
        val genres: List<Genre>? = savedStateHandle[GENRES]

        if (genres == null) loadGenres()
        if (genres == null) loadMovies()

        viewModelScope.launch {
            movies?.let { updateMovies(it) }
            genres?.let { setGenres(it) }
        }

    }

    companion object {
        private const val MOVIES = "mvs"
        private const val GENRES = "gnrs"

    }

    private fun onMoviesLoadFailed(exception: Throwable) {
        Log.d("kek", "$exception")
        _moviesState.postValue(UIState.ErrorState(exception))
    }

    private fun onGenresLoadFailed(exception: Throwable) {
        _genresState.postValue(UIState.ErrorState(exception))
    }

    private fun loadMovies() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _moviesState.postValue(UIState.LoadingState)
            val movies = movieRepository.refreshPopularMovies()
            Log.d("kek", "loadMovies before update")
            updateMovies(movies)
            Log.d("kek", "loadMovies end")

        }
    }

    private fun loadGenres() {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _genresState.postValue(UIState.LoadingState)
            val genres = genreRepository.refreshAll()
            setGenres(genres)
        }
    }

    private suspend fun updateMovies(movies: List<Movie> = _movies) =
        withContext(Dispatchers.Default) {
            _movies = movies
            val filteredMovies = getFilteredMovies(movies)
            _moviesState.postValue(UIState.DataState(filteredMovies))
            savedStateHandle.set(MOVIES, movies)
        }

    private suspend fun setGenres(genres: List<Genre>) = withContext(Dispatchers.Default) {
        val stateGenresList = mutableStateListOf<Genre>()
        stateGenresList.addAll(genres)
        _genresState.postValue(UIState.DataState(stateGenresList))
        savedStateHandle.set(GENRES, genres)
    }

    private fun getFilteredMovies(movies: List<Movie>): List<Movie> {
        return movies.filter { movie ->
            val containsGenre =
                if (_selectedGenresId.isNotEmpty()) movie.genres.any { it.genreId in _selectedGenresId } else true
            containsGenre
        }
    }

    fun loadGenreLoading(): List<Genre> {
        return listOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): List<Genre> {
        return listOf(genreRepository.loadGenreError())
    }

    override fun onGenreClicked(id: Long) {
        val genres = (_genresState.value as? UIState.DataState)?.data ?: return
        val index = genres.indexOfFirst { it.genreId == id }
        val genre = genres[index]
        val newGenre = genre.copy().apply { isSelected = !genre.isSelected }
        genres.removeAt(index)
        genres.add(index, newGenre)
    }
}
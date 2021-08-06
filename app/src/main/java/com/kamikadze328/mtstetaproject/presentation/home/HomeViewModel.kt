package com.kamikadze328.mtstetaproject.presentation.home

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.presentation.State
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
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val _moviesState: MutableLiveData<State<List<Movie>>> =
        MutableLiveData(State.LoadingState)
    val moviesState: LiveData<State<List<Movie>>> = _moviesState

    private val _genresState: MutableLiveData<State<List<Genre>>> =
        MutableLiveData(State.LoadingState)
    val genresState: LiveData<State<List<Genre>>> = _genresState

    private val _recyclerMoviesState =
        MutableLiveData<Parcelable>(savedStateHandle[RECYCLER_MOVIES_STATE])
    val recyclerMoviesState: LiveData<Parcelable> = _recyclerMoviesState

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onMoviesLoadFailed)
    }
    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onGenresLoadFailed)
    }

    private val _selectedGenresId: MutableSet<Int> = mutableSetOf()
    private var _filterStr: String =
        HomeFragmentArgs.fromSavedStateHandle(savedStateHandle).searchQuery

    private var _movies: List<Movie> = emptyList()
    private var _filteredMovies: List<Movie> = emptyList()

    init {
        Log.d("kek", "init home View model")
        val movies: List<Movie>? = savedStateHandle[MOVIES]
        val genres: List<Genre>? = savedStateHandle[GENRES]

        val isMoviesNotCached = movies == null
        val isGenresNotCached = genres == null

        if (isMoviesNotCached) loadMovies()
        if (isGenresNotCached) loadGenres()

        viewModelScope.launch {
            if (!isMoviesNotCached) updateMovies(movies!!)
            if (!isGenresNotCached) setGenres(genres!!)
        }

        Log.d("kek", "init home View model end")
    }

    companion object {
        private const val RECYCLER_MOVIES_STATE = "rclr_mvs_stt"
        private const val MOVIES = "mvs"
        private const val GENRES = "gnrs"

    }

    fun loadAllData() {
        loadMovies()
        loadGenres()
    }

    private fun onMoviesLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", "onMoviesLoadFailed - ${exception.localizedMessage}")
        _moviesState.postValue(State.ErrorState(exception))
    }

    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        _genresState.postValue(State.ErrorState(exception))
    }

    private fun loadMovies() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _moviesState.postValue(State.LoadingState)
            val movies = movieRepository.refreshPopularMovies()
            updateMovies(movies)
        }
    }

    private fun loadGenres() {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _genresState.postValue(State.LoadingState)
            val genres = genreRepository.refreshGenres()
            setGenres(genres)
        }
    }

    private suspend fun updateMovies(movies: List<Movie> = _movies) =
        withContext(Dispatchers.Default) {
            _movies = movies
            val filteredMovies = getFilteredMovies(movies)
            _moviesState.postValue(State.DataState(filteredMovies))
            savedStateHandle.set(MOVIES, movies)
        }


    private suspend fun setGenres(genres: List<Genre>) = withContext(Dispatchers.Default) {
        _genresState.postValue(State.DataState(genres))
        savedStateHandle.set(GENRES, genres)
    }

    private fun getFilteredMovies(movies: List<Movie>): List<Movie> {
        return movies.filter { movie ->
            val containsGenre =
                if (_selectedGenresId.isNotEmpty()) movie.genre_ids.any { it in _selectedGenresId } else true

            /* Log.d("kek", "${movie.title} ${_filterStr}")
             Log.d("kek", "${movie.title.contains(_filterStr)} && ${containsGenre}")*/

            movie.title.contains(_filterStr, ignoreCase = true) && containsGenre
        }
    }

    fun loadGenreLoading(): List<Genre> {
        return listOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): List<Genre> {
        return listOf(genreRepository.loadGenreError())
    }

    fun setRecyclerMoviesState(layoutManager: RecyclerView.LayoutManager) {
        viewModelScope.launch {
            setRecyclerMoviesState(layoutManager.onSaveInstanceState())
        }
    }

    private suspend fun setRecyclerMoviesState(newState: Parcelable?) =
        withContext(Dispatchers.Default) {
            if (newState != null) _recyclerMoviesState.postValue(newState)
            savedStateHandle.set(RECYCLER_MOVIES_STATE, newState)
        }

    fun clearRecyclerMoviesState() {
        viewModelScope.launch {
            setRecyclerMoviesState(null)
        }
    }

    fun updateGenresFilter(genreId: Int) {
        if (_genresState.value is State.DataState) {
            viewModelScope.launch {
                val genres =
                    (_genresState.value as State.DataState<List<Genre>>).data.toMutableList()
                val index = genres.indexOfFirst { it.id == genreId }
                val genre = genres[index].copy()
                val selectedCount = genres.count { it.isSelected }
                genre.isSelected = !genre.isSelected

                genres.removeAt(index)
                genres.add(selectedCount, genre)

                genres.sort()

                setGenres(genres)
                updateSelectedGenres(genre)
            }
        }
    }

    private suspend fun updateSelectedGenres(genre: Genre) {
        if (genre.isSelected) _selectedGenresId.add(genre.id)
        else _selectedGenresId.remove(genre.id)
        updateMovies()
    }

    fun setNewTextFilter(str: String) {
        _filterStr = str
        viewModelScope.launch {
            updateMovies()
        }
    }

}
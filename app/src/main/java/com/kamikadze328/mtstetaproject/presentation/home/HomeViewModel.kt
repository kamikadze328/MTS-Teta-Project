package com.kamikadze328.mtstetaproject.presentation.home

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository
import com.kamikadze328.mtstetaproject.data.util.SelectableGenreComparator
import com.kamikadze328.mtstetaproject.data.util.UIState
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
    private val _moviesState: MutableLiveData<UIState<List<Movie>>> =
        MutableLiveData(UIState.LoadingState)
    val moviesState: LiveData<UIState<List<Movie>>> = _moviesState

    private val _genresState: MutableLiveData<UIState<List<Genre>>> =
        MutableLiveData(UIState.LoadingState)
    val genresState: LiveData<UIState<List<Genre>>> = _genresState

    private val _recyclerMoviesState =
        MutableLiveData<Parcelable>(savedStateHandle[RECYCLER_MOVIES_STATE])
    val recyclerMoviesState: LiveData<Parcelable> = _recyclerMoviesState

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onMoviesLoadFailed)
    }
    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onGenresLoadFailed)
    }

    private val _selectedGenresId: MutableSet<Long> = mutableSetOf()
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

        if (isGenresNotCached) loadGenres()
        if (isMoviesNotCached) loadMovies()

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
        _moviesState.postValue(UIState.ErrorState(exception))
    }

    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        _genresState.postValue(UIState.ErrorState(exception))
    }

    private fun loadMovies() {
        viewModelScope.launch(moviesCoroutineExceptionHandler) {
            _moviesState.postValue(UIState.LoadingState)
            val movies = movieRepository.refreshPopularMovies()
            updateMovies(movies)
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
        _genresState.postValue(UIState.DataState(genres))
        savedStateHandle.set(GENRES, genres)
    }

    private fun getFilteredMovies(movies: List<Movie>): List<Movie> {
        return movies.filter { movie ->
            val containsGenre =
                if (_selectedGenresId.isNotEmpty()) movie.genres.any { it.genreId in _selectedGenresId } else true
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

    fun updateGenresFilter(genreId: Long) {
        if (_genresState.value is UIState.DataState) {
            viewModelScope.launch {
                val genres =
                    (_genresState.value as UIState.DataState<List<Genre>>).data.toMutableList()
                val index = genres.indexOfFirst { it.genreId == genreId }
                val genre = genres[index].copy()
                val selectedCount = genres.count { it.isSelected }
                genre.isSelected = !genre.isSelected

                genres.removeAt(index)
                genres.add(selectedCount, genre)

                genres.sortWith(SelectableGenreComparator())

                setGenres(genres)
                updateSelectedGenres(genre)
            }
        }
    }

    private suspend fun updateSelectedGenres(genre: Genre) {
        if (genre.isSelected) _selectedGenresId.add(genre.genreId)
        else _selectedGenresId.remove(genre.genreId)
        updateMovies()
    }

    fun setNewTextFilter(str: String) {
        _filterStr = str
        viewModelScope.launch {
            updateMovies()
        }
    }

}
package com.kamikadze328.mtstetaproject.presentation.movies

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.repository.MovieRepository
import com.kamikadze328.mtstetaproject.data.util.SelectableGenreComparator
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

    private val _recyclerMoviesState =
        MutableLiveData<Parcelable>(savedStateHandle[RECYCLER_MOVIES_STATE])
    val recyclerMoviesState: LiveData<Parcelable> = _recyclerMoviesState

    val thereAreMoreMovies = MutableLiveData(true)

    private val moviesCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onMoviesLoadFailed(throwable) }
    }
    private val moviesMoreCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, _ -> onMoviesLoadMoreFailed() }
    }
    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onGenresLoadFailed(throwable) }
    }

    private val _selectedGenresId: MutableSet<Long> = mutableSetOf()
    private var _filterStr: String = ""
        //HomeFragmentArgs.fromSavedStateHandle(savedStateHandle).searchQuery

    private var _movies: List<Movie> = emptyList()
    private var nextPage: Int = 2

    private var _filteredMovies: List<Movie> = emptyList()

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
        private const val RECYCLER_MOVIES_STATE = "rclr_mvs_stt"
        private const val MOVIES = "mvs"
        private const val GENRES = "gnrs"

    }

    fun loadAllData() {
        loadMovies()
        loadGenres()
    }

    private fun onMoviesLoadFailed(exception: Throwable) {
        Log.d("kek", "$exception")
        _moviesState.postValue(UIState.ErrorState(exception))
    }

    private fun onMoviesLoadMoreFailed() {
        thereAreMoreMovies.postValue(false)
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

    fun loadMoreMovies() {
        viewModelScope.launch(moviesMoreCoroutineExceptionHandler) {
            val movies = movieRepository.refreshPopularMovies(nextPage++)
            if (movies.isEmpty()) {
                onMoviesLoadMoreFailed()
                return@launch
            }
            addMovies(movies)
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

    private suspend fun addMovies(movies: List<Movie> = _movies) =
        withContext(Dispatchers.Default) {
            _movies = if (_moviesState.value is UIState.DataState) {
                val oldMovies =
                    (_moviesState.value as UIState.DataState<List<Movie>>).data.toMutableList()
                oldMovies.addAll(movies)
                oldMovies
            } else {
                movies
            }
            val filteredMovies = getFilteredMovies(_movies)
            _moviesState.postValue(UIState.DataState(filteredMovies))
            savedStateHandle.set(MOVIES, _movies)
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
            newState?.let { _recyclerMoviesState.postValue(it) }
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
                    (_genresState.value as UIState.DataState<SnapshotStateList<Genre>>).data.toMutableList()
                val index = genres.indexOfFirst { it.genreId == genreId }
                val genre = genres[index].copy()

                genre.isSelected = !_selectedGenresId.contains(genreId)

                genres.removeAt(index)
                genres.add(_selectedGenresId.size, genre)

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

    override fun onGenreClicked(genreId: Long) {
        val genres =  (_genresState.value as? UIState.DataState)?.data ?: return
        val index = genres.indexOfFirst { it.genreId == genreId }
        val genre = genres[index]
        val newGenre = genre.copy().apply { isSelected = !genre.isSelected }
        genres.removeAt(index)
        genres.add(index, newGenre)
    }

}
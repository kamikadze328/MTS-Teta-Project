package com.kamikadze328.mtstetaproject.presentation.home

import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.repository.GenreRepository
import com.kamikadze328.mtstetaproject.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {


    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> = _genres

    init {
        loadMovies()
        loadGenres()
    }


    private fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _movies.postValue(movieRepository.refreshPopularMovies())
        }
    }

    private fun loadGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            _genres.postValue(genreRepository.refreshGenres())
        }
    }


}


package com.kamikadze328.mtstetaproject.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.features.genres.GenresDataSourceImpl
import com.kamikadze328.mtstetaproject.data.features.movies.MoviesDataSourceImpl
import com.kamikadze328.mtstetaproject.model.GenresModel
import com.kamikadze328.mtstetaproject.model.MoviesModel

class HomeViewModel : ViewModel() {

    private val moviesModel: MoviesModel by lazy {
        MoviesModel(MoviesDataSourceImpl())
    }

    private val genresModel: GenresModel by lazy {
        GenresModel(GenresDataSourceImpl())
    }
    private val movies: MutableLiveData<List<Movie>> =
        MutableLiveData<List<Movie>>(moviesModel.getMovies())


    val filteredMovies: MediatorLiveData<List<Movie>> by lazy {
        MediatorLiveData<List<Movie>>().apply {
            addSource(movies) { value ->
                this.value = value
            }

            addSource(searchStr) { value ->
                this.value = movies.value?.filter { m -> m.title.contains(value) }
            }
        }
    }


    val searchStr = MutableLiveData<String>("")


    val genres: MutableLiveData<List<Genre>> =
        MutableLiveData<List<Genre>>(genresModel.getGenres())

}


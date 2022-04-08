package com.kamikadze328.mtstetaproject.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.movies.MoviesViewModel
import com.kamikadze328.mtstetaproject.ui.common.ListGenre
import com.kamikadze328.mtstetaproject.ui.profile.Header

@Composable
fun Movies(
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val movies = viewModel.moviesState.observeAsState().value
    val genres = viewModel.genresState.observeAsState().value

    val context = LocalContext.current
    Column {
        Header(textRes = R.string.movie_main_header_popular)
        when (genres) {
            is UIState.LoadingState -> viewModel.loadGenreLoading()
            is UIState.ErrorState -> viewModel.loadGenreError()
            is UIState.DataState -> ListGenre(genres = genres.data, callback = viewModel)
            null -> {}
        }

        when (movies) {
            is UIState.LoadingState -> MovieLoading()
            is UIState.ErrorState -> NoMovies()
            is UIState.DataState -> MovieShortGrid(
                movies = movies.data,
                onMovieClick = {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    //navController.navigate(R.na)
                }
            )

            null -> NoMovies()
        }

    }
}

@Composable
private fun NoMovies() {
    Text("NoMovies")
}

@Composable
private fun MovieLoading() {
    Text("MovieLoading")
}
package com.kamikadze328.mtstetaproject.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.moviedetails.MovieDetailsViewModel

@Composable
fun MovieDetails(
    navController: NavController,
    movieId: Long?,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val movie = when (val movieState = viewModel.movieState.observeAsState().value) {
        is UIState.LoadingState -> viewModel.loadMovieLoading()
        is UIState.DataState -> movieState.data
        is UIState.ErrorState -> viewModel.loadMovieError()
        else -> viewModel.loadMovieError()
    }


    Text(movieId.toString())
}
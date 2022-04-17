package com.kamikadze328.mtstetaproject.ui.movie.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.moviedetails.MovieDetailsViewModel
import com.kamikadze328.mtstetaproject.ui.common.Header
import com.kamikadze328.mtstetaproject.ui.common.Rating

@Composable
fun MovieDetails(
    navController: NavController,
    movieId: Long?,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val movieState = viewModel.movieState.observeAsState().value
    val movie = when (movieState) {
        is UIState.LoadingState -> viewModel.loadMovieLoading()
        is UIState.DataState -> movieState.data
        else -> viewModel.loadMovieError()
    }
    val genres = when (movieState) {
        is UIState.LoadingState -> viewModel.loadGenreLoading()
        is UIState.DataState -> movieState.data.genres
        else -> viewModel.loadGenreError()
    }
    val actors = when (movieState) {
        is UIState.LoadingState -> viewModel.loadActorsLoading()
        is UIState.DataState -> movieState.data.actors
        else -> viewModel.loadActorsError()
    }
    Column {
        MovieDetailsHeader(movie = movie, genres = genres, callback = viewModel)
        Rating(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            value = movie.vote_average
        )
        Description(
            modifier = Modifier.padding(20.dp),
            value = movie.overview,
        )
        Header(
            modifier = Modifier.padding(horizontal = 20.dp),
            textRes = R.string.actors,
        )
        ListActors(
            actors = actors, callback = viewModel,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
    }
}

@Composable
fun Description(
    modifier: Modifier = Modifier,
    value: String?
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = value ?: "",
        fontSize = 14.sp,
    )
}

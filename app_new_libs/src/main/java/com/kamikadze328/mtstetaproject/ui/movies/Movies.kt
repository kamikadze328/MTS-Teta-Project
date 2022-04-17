package com.kamikadze328.mtstetaproject.ui.movies

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.movies.MoviesViewModel
import com.kamikadze328.mtstetaproject.ui.NavCommand
import com.kamikadze328.mtstetaproject.ui.common.ListGenre
import com.kamikadze328.mtstetaproject.ui.common.MainHeader

@Composable
fun Movies(
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val movies = viewModel.moviesState.observeAsState().value
    val genres = viewModel.genresState.observeAsState().value
    val context = LocalContext.current
    Column(
        modifier = Modifier,
    ) {
        MainHeader(
            modifier = Modifier
                .padding(bottom = 2.dp)
                .padding(horizontal = 20.dp),
            textRes = R.string.movie_main_header_popular,
        )
        ListGenre(
            genres = when (genres) {
                is UIState.LoadingState -> viewModel.loadGenreLoading()
                is UIState.ErrorState -> viewModel.loadGenreError()
                is UIState.DataState -> genres.data
                null -> viewModel.loadGenreError()
            },
            callback = viewModel
        )

        Spacer(
            modifier = Modifier
                .height(12.dp)
                .padding(horizontal = 20.dp)
        )
        when (movies) {
            is UIState.ErrorState -> NoMovies(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            )

            is UIState.DataState -> MovieShortGrid(
                movies = movies.data,
                onMovieClick = {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate("${NavCommand.MovieDetails.route}/$it")
                }
            )


            else -> NoMovies(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            )

        }
    }
}

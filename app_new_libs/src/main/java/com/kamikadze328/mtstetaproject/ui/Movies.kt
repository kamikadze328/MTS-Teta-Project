package com.kamikadze328.mtstetaproject.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.presentation.home.MoviesViewModel
import com.kamikadze328.mtstetaproject.data.util.UIState
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Movies(
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val movies = viewModel.moviesState.observeAsState().value
    val context = LocalContext.current
    when(movies) {
        is UIState.LoadingState -> { MovieLoading() }
        is UIState.ErrorState -> { NoMovies() }
        is UIState.DataState -> {
            MovieShortGrid(
            movies = movies.data,
                onMovieClick = {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    //navController.navigate(R.na)
                }
            )
        }
        null -> { NoMovies() }
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
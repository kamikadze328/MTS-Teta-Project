package com.kamikadze328.mtstetaproject.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.movies.MoviesViewModel
import com.kamikadze328.mtstetaproject.ui.common.Header
import com.kamikadze328.mtstetaproject.ui.common.ListGenre

@Composable
fun Movies(
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val movies = viewModel.moviesState.observeAsState().value
    val genres = viewModel.genresState.observeAsState().value

    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Header(
            modifier = Modifier
                .padding(bottom = 2.dp)
                .padding(horizontal = 20.dp),
            textRes = R.string.movie_main_header_popular,
            fontSize = 18.sp,
        )

        when (genres) {
            is UIState.LoadingState -> viewModel.loadGenreLoading()
            is UIState.ErrorState -> viewModel.loadGenreError()
            is UIState.DataState -> ListGenre(genres = genres.data, callback = viewModel)
            null -> viewModel.loadGenreError()
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (movies) {
            is UIState.LoadingState -> MovieLoading()
            is UIState.ErrorState -> NoMovies(modifier = Modifier.padding(horizontal = 20.dp))
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
private fun NoMovies(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Header(textRes = R.string.movie_main_no_movies_header)
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.movie_main_no_movies_description),
                textAlign = TextAlign.Center,
            )
            Image(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.movie_main_no_movies_img_size))
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.ic_baseline_videocam_off_24),
                contentDescription = stringResource(R.string.movie_main_no_movies_header),
            )
        }
    }
    Text("NoMovies")
}

@Composable
private fun MovieLoading() {
    Text("MovieLoading")
}
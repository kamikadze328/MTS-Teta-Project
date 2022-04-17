package com.kamikadze328.mtstetaproject.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.presentation.moviedetails.MovieDetailsViewModel

@Composable
fun MovieDetails(
    navController: NavController,
    movieId: Long?,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    Text(movieId.toString())
}
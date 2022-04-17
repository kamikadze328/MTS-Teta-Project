package com.kamikadze328.mtstetaproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kamikadze328.mtstetaproject.ui.movie.details.MovieDetails
import com.kamikadze328.mtstetaproject.ui.movies.Movies
import com.kamikadze328.mtstetaproject.ui.profile.Profile
import com.kamikadze328.mtstetaproject.ui.theme.BasicTetaProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
private fun MyApp() {
    BasicTetaProjectTheme {
        val bottomNavigationItems = listOf(
            Screens.Movies,
            Screens.Profile,
        )

        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                MyAppBottomNavigation(navController, bottomNavigationItems)
            },
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "movies",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    moviesGraph(navController)
                    composable(Screens.Profile.route) { Profile(navController) }
                }
            }
        )
    }
}

fun NavGraphBuilder.moviesGraph(navController: NavController) {
    navigation(startDestination = Screens.Movies.route, route = "movies") {
        composable(Screens.Movies.route) { Movies(navController) }
        composable(
            route = "${NavCommand.MovieDetails.route}/{${NavCommand.MovieDetails.argKey}}",
            arguments = listOf(navArgument(NavCommand.MovieDetails.argKey) {
                type = NavCommand.MovieDetails.navType
            })
        ) { backStackEntry ->
            MovieDetails(
                navController = navController,
                movieId = backStackEntry.arguments?.getLong(NavCommand.MovieDetails.argKey),
            )
        }
    }
}
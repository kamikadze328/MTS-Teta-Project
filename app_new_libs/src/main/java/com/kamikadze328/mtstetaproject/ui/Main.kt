package com.kamikadze328.mtstetaproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    navController,
                    startDestination = Screens.Movies.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(Screens.Movies.route) { Movies(navController) }
                    composable(Screens.Profile.route) { Profile(navController) }
                }
            }
        )
    }
}

@Composable
private fun Main() {

}
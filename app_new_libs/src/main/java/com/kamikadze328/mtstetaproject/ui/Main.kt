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
    BasicMTSTetaProjectTheme {
        val bottomNavigationItems = listOf(
            Screens.Films,
            Screens.Profile,
        )

        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                MyAppBottomNavigation(navController, bottomNavigationItems)
            },
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screens.Films.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screens.Films.route) { Films(navController) }
                composable(Screens.Profile.route) { Profile(navController) }
            }
        }
    }
}

@Composable
private fun Main() {

}
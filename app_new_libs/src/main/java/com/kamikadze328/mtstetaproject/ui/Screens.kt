package com.kamikadze328.mtstetaproject.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kamikadze328.mtstetaproject.R

sealed class Screens(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int,
    val argKey: String? = null
) {
    object Movies : Screens("Movies", R.string.title_home, R.drawable.ic_home_black)
    object Profile : Screens("Profile", R.string.title_profile, R.drawable.ic_profile_black)
}
sealed class NavCommand(
    val route: String,
    val argKey: String,
    val navType: NavType<*>
){
    object MovieDetails : NavCommand("movie_details", "movieId", NavType.LongType)
}


@Composable
fun MyAppBottomNavigation(
    navController: NavHostController,
    items: List<Screens>
) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(stringResource(id = screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                alwaysShowLabel = false,
                onClick = {
                    /*if (currentDestination?.route == Screens.Films.route) {
                        navController.popBackStack(Screens.Films.route, false)
                    } else {*/
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    //}
                }
            )
        }
    }
}
package com.kamikadze328.mtstetaproject.presentation.main

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.databinding.ActivityMainBinding
import com.kamikadze328.mtstetaproject.presentation.movies.HomeFragmentArgs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CallbackMovieClicked, CallbackGenreClicked,
    CallbackActorClicked {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                val args = HomeFragmentArgs.Builder().setSearchQuery(query).build().toBundle()
                navController.navigate(R.id.navigation_home, args)
            }
        }
    }


    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination?.id == R.id.navigation_home) {
                        navController.popBackStack(R.id.navigation_home, false)
                    }
                }
                R.id.navigation_profile -> {
                }
                else -> {
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    //02.08.2021 nav_version = "2.4.0-alpha05"
    //There is the problem (bug??? - this problem is on google navigation example https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample)
    //On click system back we should navigate to start destination (by default behaviour).
    //But for example start destination had the back stack.
    //System back button ignore this back stack.
    //BUT if we click on the bottomNavigationView on start destination, back stack will be open.
    //A - home; B - child of A. C - another menu item.
    //A -> B -> C -> (click system back - see start destination of app) A -> (click start destination's bottomNavigationView item - see C) C -> WHF??
    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.navigation_home && binding.bottomNavigationView.selectedItemId != R.id.navigation_home) {
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        } else {
            super.onBackPressed()
        }
    }


    override fun onMovieClicked(id: Long) {
        /* val actions = HomeFragmentDirections.actionHomeToMovieDetails(movieId)
        navController.navigate(actions)*/
    }

    override fun onGenreClicked(id: Long) {
        //TODO("Not yet implemented")
    }

    override fun onActorClicked(id: Long) {
        //TODO("Not yet implemented")
    }
}

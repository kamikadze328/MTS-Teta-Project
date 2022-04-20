package com.kamikadze328.mtstetaproject.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.databinding.ActivityMainBinding
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

    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setupStartDestination(navHostFragment)

        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupStartDestination(navHostFragment: NavHostFragment) {
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)
        graph.setStartDestination(R.id.navigation_home)
        val startDestination = when (intent?.action) {
            "com.kamikadze328.mtstetaproject.PROFILE_ACTION" -> R.id.navigation_profile
            "com.kamikadze328.mtstetaproject.MOVIES_ACTION" -> R.id.navigation_home
            else -> R.id.navigation_home
        }
        graph.setStartDestination(startDestination)
        navHostFragment.navController.graph = graph
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


    override fun onMovieClicked(id: Long) {}

    override fun onGenreClicked(id: Long) {}

    override fun onActorClicked(id: Long) {}
}

package com.kamikadze328.mtstetaproject.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
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

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate main")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavController()
    }


    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


    override fun onMovieClicked(movieId: Int) {
        /* val actions = HomeFragmentDirections.actionHomeToMovieDetails(movieId)
        navController.navigate(actions)*/
    }

    override fun onGenreClicked(genreId: Int) {
        //TODO("Not yet implemented")
    }

    override fun onActorClicked(actorId: Int) {
        //TODO("Not yet implemented")
    }
}

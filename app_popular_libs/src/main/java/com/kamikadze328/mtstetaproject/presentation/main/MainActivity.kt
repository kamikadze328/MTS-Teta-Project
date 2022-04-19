package com.kamikadze328.mtstetaproject.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.app.App
import com.kamikadze328.mtstetaproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CallbackMovieClicked, CallbackGenreClicked,
    CallbackActorClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val start: Long
        get() = (application as App).start

    private val resultList = mutableListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()

        calculateTime()
    }

    private fun calculateTime() {
        resultList.add(System.currentTimeMillis() - start)
    }

    override fun onResume() {
        super.onResume()
        resultList.add(System.currentTimeMillis() - start)
        Log.d("kekTime", "xml       = $resultList")
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


    override fun onMovieClicked(id: Long) {}

    override fun onGenreClicked(id: Long) {}

    override fun onActorClicked(id: Long) {}
}

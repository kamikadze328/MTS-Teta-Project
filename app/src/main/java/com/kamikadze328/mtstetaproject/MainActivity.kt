package com.kamikadze328.mtstetaproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kamikadze328.mtstetaproject.databinding.ActivityMainBinding
import com.kamikadze328.mtstetaproject.fragment.CallbackMovieClicked
import com.kamikadze328.mtstetaproject.fragment.HomeFragment
import com.kamikadze328.mtstetaproject.fragment.MovieDetailsFragment
import com.kamikadze328.mtstetaproject.fragment.ProfileFragment


class MainActivity : AppCompatActivity(), CallbackMovieClicked {
    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var currentFragment: Fragment


    companion object {
        private const val HOME_FRAGMENT_TAG = "home_fragment_tag"
        private const val PROFILE_FRAGMENT_TAG = "profile_fragment_tag"
        private val MOVIE_DETAILS_FRAGMENT_TAG: (Int) -> String =
            { movieId -> "moviedetails_fragment_tag$movieId" }

        private const val CURRENT_FRAGMENT_TAG = "current_fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationSelectedListener()

        if (savedInstanceState == null) {
            initFragments()
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        } else {
            val currentFragmentTag =
                savedInstanceState.getString(CURRENT_FRAGMENT_TAG, HOME_FRAGMENT_TAG)
            reInitFragments(currentFragmentTag)
        }

        //initNavController()

    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun initFragments() {
        homeFragment = HomeFragment.newInstance()
        profileFragment = ProfileFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment, homeFragment, HOME_FRAGMENT_TAG)
            .add(R.id.nav_host_fragment, profileFragment, PROFILE_FRAGMENT_TAG)
            .hide(profileFragment)
            .commit()
        currentFragment = homeFragment
    }

    private fun reInitFragments(currentFragmentTag: String) {
        homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG) as HomeFragment
        profileFragment =
            supportFragmentManager.findFragmentByTag(PROFILE_FRAGMENT_TAG) as ProfileFragment

        currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag) as Fragment
    }

    private fun setNavigationSelectedListener() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_profile -> {
                    openFragment(profileFragment)
                }

                R.id.navigation_home -> {
                    openFragment(homeFragment)
                }
                else -> return@setOnItemSelectedListener false
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun clearNavigationSelectedListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(null)
    }


    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .hide(currentFragment)
            .show(fragment)
            .commit()
        currentFragment = fragment
    }

    override fun onMovieClicked(movieId: Int) {
        val movieDetails = MovieDetailsFragment.newInstance(movieId, currentFragment.tag!!)
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, movieDetails, MOVIE_DETAILS_FRAGMENT_TAG(movieId))
            .hide(movieDetails)
            .commit()

        openFragment(movieDetails)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (currentFragment is MovieDetailsFragment) {
            supportFragmentManager
                .beginTransaction()
                .remove(currentFragment)
                .commit()
        }

        currentFragment = supportFragmentManager.fragments.find { f -> f.isVisible }!!

        clearNavigationSelectedListener()
        when (currentFragment) {
            is ProfileFragment -> setNavigationItem(R.id.navigation_profile)
            is HomeFragment -> setNavigationItem(R.id.navigation_home)
            is MovieDetailsFragment -> {
                when (currentFragment.requireArguments()
                    .getString(MovieDetailsFragment.PARENT_ID_ARG)!!) {
                    HOME_FRAGMENT_TAG -> setNavigationItem(R.id.navigation_home)
                    PROFILE_FRAGMENT_TAG -> setNavigationItem(R.id.navigation_profile)
                    else -> throw IllegalStateException()
                }
            }
            else -> throw IllegalStateException()
        }
        setNavigationSelectedListener()
    }

    private fun setNavigationItem(itemId: Int) {
        binding.bottomNavigationView.selectedItemId = itemId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_FRAGMENT_TAG, currentFragment.tag)
        super.onSaveInstanceState(outState)
    }
}

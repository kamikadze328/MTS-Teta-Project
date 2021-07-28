package com.kamikadze328.mtstetaproject.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.databinding.ActivityMainBinding
import com.kamikadze328.mtstetaproject.presentation.home.HomeFragment
import com.kamikadze328.mtstetaproject.presentation.moviedetails.MovieDetailsFragment
import com.kamikadze328.mtstetaproject.presentation.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CallbackMovieClicked, CallbackGenreClicked,
    CallbackActorClicked {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate main")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationSelectedListener()


        if (savedInstanceState == null) {
            initFragments()
            setNavigationItem(R.id.navigation_home)
        } else {
            reInitFragments()
        }

        //initNavController()
    }

    /*private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }*/

    private fun initFragments() {
        val transaction = supportFragmentManager.beginTransaction()

        ProfileFragment.newInstance(10).let {
            viewModel.setProfileFragment(it)

            transaction
                .add(R.id.nav_host_fragment, it, MainViewModel.PROFILE_FRAGMENT_TAG)
                .hide(it)
        }

        HomeFragment.newInstance().let {
            viewModel.setCurrentFragment(it)
            viewModel.setHomeFragment(it)

            transaction
                .add(R.id.nav_host_fragment, it, MainViewModel.HOME_FRAGMENT_TAG)
        }

        transaction.commit()
    }

    private fun reInitFragments() {
        viewModel.setHomeFragment(supportFragmentManager.findFragmentByTag(MainViewModel.HOME_FRAGMENT_TAG) as HomeFragment)
        viewModel.setProfileFragment(supportFragmentManager.findFragmentByTag(MainViewModel.PROFILE_FRAGMENT_TAG) as ProfileFragment)
        viewModel.setCurrentFragment(supportFragmentManager.findFragmentByTag(viewModel.getCurrentFragmentTag()) as Fragment)
    }

    private fun setNavigationSelectedListener() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_profile -> {
                    openFragment(viewModel.getProfileFragment())
                }

                R.id.navigation_home -> {
                    openFragment(viewModel.getHomeFragment())
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
            .hide(viewModel.getCurrentFragment())
            .show(fragment)
            .commit()
        viewModel.setCurrentFragment(fragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        viewModel.getCurrentFragment().let {
            if (it is MovieDetailsFragment) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(it)
                    .commit()
            }
        }

        supportFragmentManager.fragments.find { f -> f.isVisible }!!.let { currFrag ->
            viewModel.setCurrentFragment(currFrag)

            clearNavigationSelectedListener()
            when (currFrag) {
                is ProfileFragment -> setNavigationItem(R.id.navigation_profile)
                is HomeFragment -> setNavigationItem(R.id.navigation_home)
                is MovieDetailsFragment -> {
                    when (currFrag.requireArguments()
                        .getString(MovieDetailsFragment.PARENT_ID_ARG)!!) {
                        MainViewModel.HOME_FRAGMENT_TAG -> setNavigationItem(R.id.navigation_home)
                        MainViewModel.PROFILE_FRAGMENT_TAG -> setNavigationItem(R.id.navigation_profile)
                        else -> throw IllegalStateException()
                    }
                }
                else -> throw IllegalStateException()
            }

            setNavigationSelectedListener()
        }
    }

    private fun setNavigationItem(itemId: Int) {
        binding.bottomNavigationView.selectedItemId = itemId
    }

    override fun onMovieClicked(movieId: Int) {
        MovieDetailsFragment.newInstance(movieId, viewModel.getCurrentFragmentTag())
            .let { detailsFrag ->
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.nav_host_fragment,
                        detailsFrag,
                        MainViewModel.MOVIE_DETAILS_FRAGMENT_TAG(movieId)
                    )
                    .hide(detailsFrag)
                    .commit()

                openFragment(detailsFrag)
            }
    }

    override fun onGenreClicked(genreId: Int) {
        //TODO("Not yet implemented")
    }

    override fun onActorClicked(actorId: Int) {
        //TODO("Not yet implemented")
    }
}

package com.kamikadze328.mtstetaproject.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.databinding.ActivityMainBinding
import com.kamikadze328.mtstetaproject.notificationservice.MovieUploadAndNotifyWorker
import com.kamikadze328.mtstetaproject.notificationservice.MyFirebaseMessagingService
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
        debugFirebaseToken()

        //Only in debug purpose
        scheduleJob("kekeke")
    }

    /**
     * The real work executes in [MyFirebaseMessagingService].
     * This method exists only in debug purpose
     */
    //Only in debug purpose
    private fun scheduleJob(messageBody: String) {
        val data = Data.Builder()
            .putString(MyFirebaseMessagingService.MESSAGE_ARG, messageBody)
            .build()

        val work: WorkRequest = OneTimeWorkRequestBuilder<MovieUploadAndNotifyWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(work)
    }

    private fun debugFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("kek", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("kek", msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
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
        Log.d("kek", "onSupportNavigateUp")
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
            Log.d("kek", "here!!!!!")
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        } else {
            super.onBackPressed()
        }
        navController.navigateUp()
        Log.d("kek", "${navController.backQueue}")
        Log.d("kek", "to - ${navController.currentDestination}")
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

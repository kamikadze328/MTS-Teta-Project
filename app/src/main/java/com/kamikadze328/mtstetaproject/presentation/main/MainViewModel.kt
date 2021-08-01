package com.kamikadze328.mtstetaproject.presentation.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kamikadze328.mtstetaproject.presentation.home.HomeFragment
import com.kamikadze328.mtstetaproject.presentation.profile.ProfileFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _currentFragment = MutableLiveData<Fragment>()
    private val _currentFragmentTag = MutableLiveData<String>()
    val currentFragment: LiveData<Fragment> = _currentFragment

    private val _homeFragment = MutableLiveData<Fragment>()
    val homeFragment: LiveData<Fragment> = _homeFragment

    private val _profileFragment = MutableLiveData<Fragment>()
    val profileFragment: LiveData<Fragment> = _profileFragment

    companion object {
        const val HOME_FRAGMENT_TAG = "hm_fr_tg"
        const val PROFILE_FRAGMENT_TAG = "prfl_fr_tg"
        val MOVIE_DETAILS_FRAGMENT_TAG: (Int) -> String =
            { movieId -> "moviedetails_fr_tg$movieId" }

    }

    fun getCurrentFragment() = currentFragment.value!!
    fun setCurrentFragment(fragment: Fragment) {
        _currentFragmentTag.value = fragment.tag
        _currentFragment.value = fragment
    }

    fun getHomeFragment() = homeFragment.value!!
    fun setHomeFragment(fragment: HomeFragment) {
        _homeFragment.value = fragment
    }

    fun getProfileFragment() = profileFragment.value!!
    fun setProfileFragment(fragment: ProfileFragment) {
        _profileFragment.value = fragment
    }

    fun getCurrentFragmentTag() = _currentFragmentTag.value!!
}
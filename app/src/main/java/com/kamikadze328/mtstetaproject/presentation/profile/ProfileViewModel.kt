package com.kamikadze328.mtstetaproject.presentation.profile

import android.util.Log
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val accountId: MutableLiveData<Int> = MutableLiveData(savedStateHandle[PROFILE_ID_ARG])

    private val _favouriteMovies = MutableLiveData<List<Movie>>()
    val favouriteMovies: LiveData<List<Movie>> = _favouriteMovies

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user


    private val _changedUser = MutableLiveData<User>(savedStateHandle[CHANGED_USER_ARG])
    val changedUser: LiveData<User> = _changedUser

    private val _wasDataChanged = MutableLiveData(savedStateHandle[WAS_DATA_CHANGED_ARG] ?: false)
    val wasDataChanged: LiveData<Boolean> = _wasDataChanged

    init {
        savedStateHandle.set(WAS_DATA_CHANGED_ARG, this._wasDataChanged.value)
    }

    companion object {
        const val PROFILE_ID_ARG = "uid"
        const val CHANGED_USER_ARG = "ch_usr"
        const val WAS_DATA_CHANGED_ARG = "ws_dt_ch"

    }

    fun setAccountId(newAccountId: Int) {
        savedStateHandle.set(PROFILE_ID_ARG, newAccountId)
        accountId.value = newAccountId
        init()
    }

    fun getAccountId(): Int = accountId.value!!

    private fun init() {
        loadFavouritesMovies()
        loadUser()
    }

    private fun loadFavouritesMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteMovies.postValue(accountRepository.getFavouriteMovies(accountId.value!!))
        }
    }

    private fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.refreshUser(accountId.value!!).let {
                _user.postValue(it)
                setChangedUser(it)
                setWasDataChanged(false)
            }
        }
    }

    private fun setWasDataChanged(newVal: Boolean){
        savedStateHandle.set(WAS_DATA_CHANGED_ARG, newVal)
        this._wasDataChanged.postValue(newVal)
    }

    private fun setChangedUser(user: User) {
        user.id = accountId.value!!
        savedStateHandle.set(CHANGED_USER_ARG, user)
        _changedUser.postValue(user)
    }

    fun updateChangedUser(newChangedUser: User) {
        setChangedUser(newChangedUser)
        user.value?.let {
            setWasDataChanged(wasDataChanged(it, newChangedUser))
        }
    }

    private fun wasDataChanged(oldUser: User, newChangedUser: User): Boolean =
        oldUser.compareTo(newChangedUser) != 0

}
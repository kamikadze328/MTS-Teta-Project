package com.kamikadze328.mtstetaproject.presentation.profile

import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val accountId: MutableLiveData<Int> = MutableLiveData(savedStateHandle[PROFILE_ID_ARG])

    private val _favouriteGenres = MutableLiveData<List<Genre>>()
    val favouriteGenres: LiveData<List<Genre>> = _favouriteGenres

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
            _favouriteGenres.postValue(accountRepository.getFavouriteGenres(getAccountId()))
        }
    }

    private fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.refreshUser(getAccountId()).let {
                withContext(Dispatchers.Main) {
                    _user.value = it
                    setChangedUser(it)
                    setWasDataChanged(false)
                }
            }
        }
    }

    private fun setWasDataChanged(newVal: Boolean) {
        savedStateHandle.set(WAS_DATA_CHANGED_ARG, newVal)
        this._wasDataChanged.value = newVal
    }

    private fun setChangedUser(user: User) {
        user.id = getAccountId()
        savedStateHandle.set(CHANGED_USER_ARG, user)
        _changedUser.value = user
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
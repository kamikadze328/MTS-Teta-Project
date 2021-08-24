package com.kamikadze328.mtstetaproject.presentation.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.mapper.toUser
import com.kamikadze328.mtstetaproject.data.repository.AccountRepository
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    private val genreRepository: GenreRepository,
    application: Application
) : AndroidViewModel(application) {
    private val uid: MutableLiveData<String> = MutableLiveData(savedStateHandle[PROFILE_ID])

    private val _isAuthorized = MutableLiveData(true)
    val isAuthorized: LiveData<Boolean> = _isAuthorized

    private val _favouriteGenresState: MutableLiveData<UIState<List<Genre>>> =
        MutableLiveData(UIState.LoadingState)
    val favouriteGenresState: LiveData<UIState<List<Genre>>> = _favouriteGenresState

    private val _changedUser = MutableLiveData<User>(savedStateHandle[CHANGED_USER])
    val changedUser: LiveData<User> = _changedUser

    private val _userState: MutableLiveData<UIState<User>> = MutableLiveData(UIState.LoadingState)
    val userState: LiveData<UIState<User>> = _userState

    private val _wasDataChanged = MutableLiveData(savedStateHandle[WAS_DATA_CHANGED] ?: false)
    val wasDataChanged: LiveData<Boolean> = _wasDataChanged

    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onGenresLoadFailed)
    }
    private val userCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onUserLoadFailed)
    }

    companion object {
        private const val PROFILE_ID = "uid"
        private const val CHANGED_USER = "ch_usr"
        private const val WAS_DATA_CHANGED = "ws_dt_ch"
        private const val GENRES = "grns"
    }

    init {
        init()
    }

    fun init() {
        setupUser()

        val genres: List<Genre>? = savedStateHandle[GENRES]
        val isGenresNotCached = genres == null
        if (isGenresNotCached) loadFavouritesGenres()

        viewModelScope.launch {
            if (!isGenresNotCached) setFavouriteGenres(genres!!)
        }
    }

    private fun setUserId(uid: String) {
        savedStateHandle.set(PROFILE_ID, uid)
        this.uid.value = uid
    }

    fun getUseId() = uid.value!!


    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", exception.stackTraceToString())
        _favouriteGenresState.postValue(UIState.ErrorState(exception))
    }

    private fun loadFavouritesGenres() {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _favouriteGenresState.postValue(UIState.LoadingState)
            val genres = accountRepository.getFavouriteGenres(getUseId())
            setFavouriteGenres(genres)
        }
    }

    private suspend fun setFavouriteGenres(genres: List<Genre>) = withContext(Dispatchers.Default) {
        _favouriteGenresState.postValue(UIState.DataState(genres))
        savedStateHandle.set(GENRES, genres)
    }

    private fun onUserLoadFailed(context: CoroutineContext, exception: Throwable) {
        Log.d("kek", "$exception")
        _userState.postValue(UIState.ErrorState(exception))
        onGenresLoadFailed(context, exception)
    }

    private fun setupUser() {
        _userState.postValue(UIState.LoadingState)
        accountRepository.currentUser?.let {
            _isAuthorized.value = true
            setNewUser(it.toUser())
        } ?: logout()
    }

    private fun setNewUser(user: User) {
        setUserId(user.id)
        _userState.postValue(UIState.DataState(user))
        setChangedUser(user)
        setWasDataChanged(false)
    }

    private fun setWasDataChanged(newVal: Boolean) {
        savedStateHandle.set(WAS_DATA_CHANGED, newVal)
        this._wasDataChanged.value = newVal
    }

    private fun setChangedUser(user: User) {
        user.id = getUseId()
        savedStateHandle.set(CHANGED_USER, user)
        _changedUser.value = user
    }

    fun updateChangedUser(newChangedUser: User) {
        setChangedUser(newChangedUser)
        _userState.value?.let {
            if (it is UIState.DataState)
                setWasDataChanged(wasDataChanged(it.data, newChangedUser))
        }
    }

    fun logout() {
        accountRepository.logout()
        _isAuthorized.value = false
    }

    private fun wasDataChanged(oldUser: User, newChangedUser: User): Boolean =
        oldUser.compareTo(newChangedUser) != 0

    fun loadGenreLoading(): List<Genre> {
        return listOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): List<Genre> {
        return listOf(genreRepository.loadGenreError())
    }

    fun loadUserLoading(): User {
        return accountRepository.loadUserLoading()
    }

    fun loadUserError(): User {
        return accountRepository.loadUserError()
    }
}
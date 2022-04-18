package com.kamikadze328.mtstetaproject.presentation.profile

import android.app.Application
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.repository.AccountRepository
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    private val genreRepository: GenreRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _favouriteGenresState: MutableLiveData<UIState<List<Genre>>> =
        MutableLiveData(UIState.LoadingState)
    val favouriteGenresState: LiveData<UIState<List<Genre>>> = _favouriteGenresState

    private val _userState: MutableLiveData<UIState<User>> = MutableLiveData(UIState.LoadingState)
    val userState: LiveData<UIState<User>> = _userState

    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onGenresLoadFailed(throwable) }
    }

    companion object {
        private const val PROFILE_ID = "uid"
        private const val CHANGED_USER = "ch_usr"
        private const val GENRES = "grns"
    }

    init {
        init()
    }

    fun init() {
        setupUser()

        val genres: List<Genre>? = savedStateHandle[GENRES]
        if (genres == null) loadFavouritesGenres()

        viewModelScope.launch {
            genres?.let { setFavouriteGenres(it) }
        }
    }

    private fun setUserId(uid: String) {
        savedStateHandle.set(PROFILE_ID, uid)
    }

    private fun onGenresLoadFailed(exception: Throwable) {
        _favouriteGenresState.postValue(UIState.ErrorState(exception))
    }

    private fun loadFavouritesGenres() {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _favouriteGenresState.postValue(UIState.LoadingState)
            val genres = accountRepository.getFavouriteGenres()
            setFavouriteGenres(genres)
        }
    }

    private suspend fun setFavouriteGenres(genres: List<Genre>) = withContext(Dispatchers.Default) {
        _favouriteGenresState.postValue(UIState.DataState(genres))
        savedStateHandle.set(GENRES, genres)
    }

    private fun setupUser() {
        _userState.postValue(UIState.LoadingState)
        val user = accountRepository.currentUser

        setNewUser(user)
    }

    private fun setNewUser(user: User) {
        setUserId(user.uid)
        _userState.postValue(UIState.DataState(user))
        setChangedUser(user)
    }

    private fun setChangedUser(user: User) {
        savedStateHandle.set(CHANGED_USER, user)
    }

    fun updateChangedUser(newChangedUser: User) {
        setChangedUser(newChangedUser)
    }

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

    fun logout() {
        accountRepository.logout()
    }
}
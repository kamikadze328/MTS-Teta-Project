package com.kamikadze328.mtstetaproject.presentation.profile

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.repository.AccountRepository
import com.kamikadze328.mtstetaproject.data.repository.GenreRepository
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.main.CallbackGenreClicked
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
) : AndroidViewModel(application), CallbackGenreClicked {
    private val _favouriteGenresState: MutableLiveData<UIState<SnapshotStateList<Genre>>> =
        MutableLiveData(UIState.LoadingState)
    val favouriteGenresState: LiveData<UIState<SnapshotStateList<Genre>>> = _favouriteGenresState

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

    private fun init() {
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
        val stateGenresList = mutableStateListOf<Genre>()
        stateGenresList.addAll(genres)
        _favouriteGenresState.postValue(UIState.DataState(stateGenresList))
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

    private fun getOldUser(): User? = (_userState.value as? UIState.DataState)?.data

    fun updateUserName(newValue: String) {
        val oldUser = getOldUser() ?: return
        val newUser = oldUser.copy(name = newValue)
        updateChangedUser(newUser)
    }

    fun updateUserPassword(newValue: String) {
        val oldUser = getOldUser() ?: return
        val newUser = oldUser.copy(password = newValue)
        updateChangedUser(newUser)
    }

    fun updateUserEmail(newValue: String) {
        val oldUser = getOldUser() ?: return
        val newUser = oldUser.copy(email = newValue)
        updateChangedUser(newUser)
    }

    fun updateUserPhone(newValue: String) {
        val oldUser = getOldUser() ?: return
        val newUser = oldUser.copy(phone = newValue)
        updateChangedUser(newUser)
    }

    private fun updateChangedUser(newChangedUser: User) {
        setChangedUser(newChangedUser)
    }

    fun loadGenreLoading(): SnapshotStateList<Genre> {
        return mutableStateListOf(genreRepository.loadGenreLoading())
    }

    fun loadGenreError(): SnapshotStateList<Genre> {
        return mutableStateListOf(genreRepository.loadGenreError())
    }

    fun loadUserLoading(): User {
        return accountRepository.loadUserLoading()
    }

    fun loadUserError(): User {
        return accountRepository.loadUserError()
    }

    override fun onGenreClicked(genreId: Long) {
        val genres = (_favouriteGenresState.value as? UIState.DataState)?.data ?: return
        val index = genres.indexOfFirst { it.genreId == genreId }
        val genre = genres[index]
        val newGenre = genre.copy().apply { isSelected = !genre.isSelected }
        genres.removeAt(index)
        genres.add(index, newGenre)
    }

    fun logout() {}
}
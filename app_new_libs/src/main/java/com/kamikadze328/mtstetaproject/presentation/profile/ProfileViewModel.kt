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
    private val uid: MutableLiveData<String> = MutableLiveData(savedStateHandle[PROFILE_ID])

    private val _isAuthorized = MutableLiveData(true)
    val isAuthorized: LiveData<Boolean> = _isAuthorized

    private val _favouriteGenresState: MutableLiveData<UIState<SnapshotStateList<Genre>>> =
        MutableLiveData(UIState.LoadingState)
    val favouriteGenresState: LiveData<UIState<SnapshotStateList<Genre>>> = _favouriteGenresState

    private val _changedUser = MutableLiveData<User>(savedStateHandle[CHANGED_USER])
    val changedUser: LiveData<User> = _changedUser

    private val _userState: MutableLiveData<UIState<User>> = MutableLiveData(UIState.LoadingState)
    val userState: LiveData<UIState<User>> = _userState

    private val _wasDataChanged: MutableLiveData<UserState> = MutableLiveData()
    val wasDataChanged: LiveData<UserState> = _wasDataChanged


    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onGenresLoadFailed(throwable) }
    }
    private val userCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable -> onUserLoadFailed(throwable) }
    }

    private val isEmailUpdate = MutableLiveData(false)
    private val isNameUpdate = MutableLiveData(false)

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
        val isGenresNotCached = genres == null
        if (genres == null) loadFavouritesGenres()

        viewModelScope.launch {
            genres?.let { setFavouriteGenres(it) }
        }
    }

    private fun setUserId(uid: String) {
        savedStateHandle.set(PROFILE_ID, uid)
        this.uid.value = uid
    }

    fun getUseId() = uid.value!!


    private fun onGenresLoadFailed(exception: Throwable) {
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
        val stateGenresList = mutableStateListOf<Genre>()
        stateGenresList.addAll(genres)
        _favouriteGenresState.postValue(UIState.DataState(stateGenresList))
        savedStateHandle.set(GENRES, genres)
    }

    private fun onUserLoadFailed(exception: Throwable) {
        _userState.postValue(UIState.ErrorState(exception))
        onGenresLoadFailed(exception)
    }

    private fun setupUser() {
        _userState.postValue(UIState.LoadingState)
        val user = accountRepository.currentUser

        _isAuthorized.value = true
        setNewUser(user)
    }

    private fun setNewUser(user: User) {
        setUserId(user.uid)
        _userState.postValue(UIState.DataState(user))
        setChangedUser(user)
        setWasDataChanged(UserState.DEFAULT)
    }

    private fun setWasDataChanged(newVal: UserState) {
        this._wasDataChanged.value = newVal
    }

    private fun setChangedUser(user: User) {
        user.uid = getUseId()
        savedStateHandle.set(CHANGED_USER, user)
        _changedUser.value = user
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

    fun updateChangedUser(newChangedUser: User) {
        setChangedUser(newChangedUser)
        val oldUser = getOldUser() ?: return
        setWasDataChanged(wasDataChanged(oldUser, newChangedUser))
    }

    fun updateUser() {
        val currentUser = _changedUser.value ?: return
        viewModelScope.launch {
            accountRepository.updateUser(currentUser)
        }
    }

    fun logout() {
        accountRepository.logout()
        _isAuthorized.value = false
    }

    private fun wasDataChanged(oldUser: User, newChangedUser: User): UserState =
        if (oldUser.compareTo(newChangedUser) != 0) UserState.DEFAULT else UserState.WAS_DATA_CHANGED

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
        val genres =  (_favouriteGenresState.value as? UIState.DataState)?.data ?: return
        val index = genres.indexOfFirst { it.genreId == genreId }
        val genre = genres[index]
        val newGenre = genre.copy().apply { isSelected = !genre.isSelected }
        genres.removeAt(index)
        genres.add(index, newGenre)
    }
}
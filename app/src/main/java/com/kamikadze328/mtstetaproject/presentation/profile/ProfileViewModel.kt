package com.kamikadze328.mtstetaproject.presentation.profile

import android.app.Application
import androidx.lifecycle.*
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.presentation.State
import com.kamikadze328.mtstetaproject.repository.AccountRepository
import com.kamikadze328.mtstetaproject.repository.GenreRepository
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
    private val accountId: MutableLiveData<Int> = MutableLiveData(savedStateHandle[PROFILE_ID_ARG])

    private val _favouriteGenresState: MutableLiveData<State<List<Genre>>> =
        MutableLiveData(State.LoadingState)
    val favouriteGenresState: LiveData<State<List<Genre>>> = _favouriteGenresState

    private val _changedUser = MutableLiveData<User>(savedStateHandle[CHANGED_USER_ARG])
    val changedUser: LiveData<User> = _changedUser

    private val _userState: MutableLiveData<State<User>> = MutableLiveData()
    val userState: LiveData<State<User>> = _userState

    private val _wasDataChanged = MutableLiveData(savedStateHandle[WAS_DATA_CHANGED_ARG] ?: false)
    val wasDataChanged: LiveData<Boolean> = _wasDataChanged

    private val genresCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onGenresLoadFailed)
    }
    private val userCoroutineExceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler(::onUserLoadFailed)
    }

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
        loadFavouritesGenres()
        loadUser()
    }

    private fun onGenresLoadFailed(context: CoroutineContext, exception: Throwable) {
        _favouriteGenresState.postValue(State.ErrorState(exception))
    }

    private fun loadFavouritesGenres() {
        viewModelScope.launch(genresCoroutineExceptionHandler) {
            _favouriteGenresState.postValue(State.LoadingState)
            val genres = accountRepository.getFavouriteGenres(getAccountId())
            _favouriteGenresState.postValue(State.DataState(genres))
        }
    }

    private fun onUserLoadFailed(context: CoroutineContext, exception: Throwable) {
        _userState.postValue(State.ErrorState(exception))
        onGenresLoadFailed(context, exception)
    }

    private fun loadUser() {
        viewModelScope.launch(userCoroutineExceptionHandler) {
            _userState.postValue(State.LoadingState)
            accountRepository.refreshUser(getAccountId()).let {
                withContext(Dispatchers.Main) {
                    setWasDataChanged(false)
                    _userState.value = State.DataState(it)
                    setChangedUser(it)
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
        _userState.value?.let {
            if (it is State.DataState)
                setWasDataChanged(wasDataChanged(it.data, newChangedUser))
        }
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
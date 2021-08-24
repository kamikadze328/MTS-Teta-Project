package com.kamikadze328.mtstetaproject.presentation.login

import android.app.Activity
import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseUser
import com.kamikadze328.mtstetaproject.data.repository.AccountRepository
import com.kamikadze328.mtstetaproject.data.util.network.auth.FirebaseAuth
import com.kamikadze328.mtstetaproject.data.util.network.auth.FirebaseAuthCallback
import com.kamikadze328.mtstetaproject.data.util.network.auth.FirebaseAuthState
import com.kamikadze328.mtstetaproject.data.util.phone.isPhoneNumberAfterFormatOk
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedLoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    application: Application,
    private val accountRepository: AccountRepository
) : AndroidViewModel(application) {
    companion object {
        const val PHONE_STR = "phn"
        const val IS_CODE_RESENT_TIMER_OVER = "tmr_ovr"
        const val RESEND_TIMEOUT_MS = 60000L
    }

    private var _firebaseAuth: FirebaseAuth? = null
    private val firebaseAuth: FirebaseAuth get() = _firebaseAuth!!

    private var phoneNumber: String = savedStateHandle[PHONE_STR] ?: ""

    private val _codeResendTimeout = MutableLiveData(RESEND_TIMEOUT_MS / 1000)
    val codeResendTimeout: LiveData<Long> = _codeResendTimeout

    private val _codeResendTimeoutIsOver =
        MutableLiveData(savedStateHandle[IS_CODE_RESENT_TIMER_OVER] ?: false)
    val codeResendTimeoutIsOver: LiveData<Boolean> = _codeResendTimeoutIsOver

    private val _state: MutableLiveData<FirebaseAuthState> =
        MutableLiveData(FirebaseAuthState.WaitUserPhoneNumber())
    val state: LiveData<FirebaseAuthState> = _state

    private val codeResendTimer = object : CountDownTimer(RESEND_TIMEOUT_MS, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            _codeResendTimeout.postValue(millisUntilFinished / 1000)
        }

        override fun onFinish() {
            setCodeResendTimeoutIsOver(true)
        }
    }

    /*init {
        clearAndReInit()
    }*/

    private val callback: FirebaseAuthCallback = object : FirebaseAuthCallback {
        override fun signInSuccessful(user: FirebaseUser?) {
            if (user != null) {
                _state.postValue(FirebaseAuthState.AuthWasSuccessful)
                Log.d("kek", "signInSuccessful - $user")

            } else {
                signInFailed(false, null)
            }
        }

        override fun signInFailed(isVerificationCodeInvalid: Boolean, exception: Exception?) {
            _state.postValue(
                FirebaseAuthState.CodeWasSentToUser(
                    isCodeInvalid = isVerificationCodeInvalid,
                    exceptionType = exception?.javaClass?.simpleName
                )
            )
            Log.d("kek", "signInFailed - $exception")
        }

        override fun onTokenSent() {
            startCodeResendTimer()
            _state.postValue(FirebaseAuthState.CodeWasSentToUser())
        }

        override fun verificationFailed(
            isInvalidRequest: Boolean,
            isSmsQuotaHasBeenExceeded: Boolean
        ) {
            _state.postValue(
                FirebaseAuthState.CodeSendingToUserInvalid(
                    isInvalidRequest = isInvalidRequest,
                    isSmsQuotaHasBeenExceeded = isSmsQuotaHasBeenExceeded
                )
            )
        }

        override fun verificationCompleted() {
            _state.postValue(FirebaseAuthState.CodeChecked)
        }
    }

    fun getPhoneNumber() = phoneNumber

    private fun startCodeResendTimer() {
        setCodeResendTimeoutIsOver(false)
        codeResendTimer.start()
    }

    fun init(activity: Activity) {
        _firebaseAuth?.let {
            it.activity = activity
        } ?: kotlin.run {
            _firebaseAuth = FirebaseAuth(callback, activity)
        }

        if (accountRepository.isLogin()) {
            _state.value = FirebaseAuthState.AuthWasSuccessful
        } else if (_state.value is FirebaseAuthState.AuthWasSuccessful) {
            clearAndReInit()
        }
    }

    fun sendVerificationCode(isResend: Boolean = false) {
        if (isPhoneNumberAfterFormatOk(phoneNumber)) {
            _state.postValue(FirebaseAuthState.PhoneValidSendingCodeToUser)
            if (isResend) firebaseAuth.startPhoneNumberVerification(phoneNumber)
            else firebaseAuth.resendVerificationCode(phoneNumber)
        } else {
            _state.postValue(FirebaseAuthState.WaitUserPhoneNumber(isPhoneNumberNotValid = true))
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        savedStateHandle[PHONE_STR] = phoneNumber
    }


    private fun setCodeResendTimeoutIsOver(newState: Boolean) {
        _codeResendTimeoutIsOver.postValue(newState)
        savedStateHandle[IS_CODE_RESENT_TIMER_OVER] = newState
    }

    fun chooseAnotherPhoneNumber() {
        clearAndReInit()
    }


    fun submitCode(code: String) {
        _state.postValue(FirebaseAuthState.CodeChecking)
        firebaseAuth.verifyPhoneNumberWithCode(code = code)
    }

    private fun clearAndReInit() {
        _state.value = FirebaseAuthState.WaitUserPhoneNumber()
        setPhoneNumber("")
        setCodeResendTimeoutIsOver(false)
        codeResendTimer.cancel()
    }

}
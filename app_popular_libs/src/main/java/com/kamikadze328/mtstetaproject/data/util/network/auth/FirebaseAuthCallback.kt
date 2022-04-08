package com.kamikadze328.mtstetaproject.data.util.network.auth

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthCallback {
    fun signInSuccessful(user: FirebaseUser?)
    fun signInFailed(isVerificationCodeInvalid: Boolean = true, exception: Exception?)
    fun onTokenSent()
    fun verificationFailed(
        isInvalidRequest: Boolean = false,
        isSmsQuotaHasBeenExceeded: Boolean = false
    )

    fun verificationCompleted()

    //fun verifyPhoneNumberWithCode()
}
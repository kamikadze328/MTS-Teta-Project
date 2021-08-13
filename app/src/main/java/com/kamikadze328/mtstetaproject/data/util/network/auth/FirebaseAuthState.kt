package com.kamikadze328.mtstetaproject.data.util.network.auth

sealed class FirebaseAuthState() {
    class WaitUserPhoneNumber(
        val isPhoneNumberNotValid: Boolean = false
    ) : FirebaseAuthState()

    data class CodeSendingToUserInvalid(
        val isInvalidRequest: Boolean = false,
        val isSmsQuotaHasBeenExceeded: Boolean = false
    ) : FirebaseAuthState()

    object PhoneValidSendingCodeToUser : FirebaseAuthState()
    data class CodeWasSentToUser(
        var isCanResend: Boolean = false,
        val isCodeInvalid: Boolean = false,
        val exceptionType: String? = null
    ) : FirebaseAuthState()

    object CodeChecking : FirebaseAuthState()
    object CodeChecked : FirebaseAuthState()
    object AuthWasSuccessful : FirebaseAuthState()
}
package com.kamikadze328.mtstetaproject.data.util.network.auth

import android.app.Activity
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class FirebaseAuth(private val myCallback: FirebaseAuthCallback, var activity: Activity) {

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    var auth: FirebaseAuth = Firebase.auth


    companion object {
        private const val TAG = "PhoneAuthActivitykek"
    }

    // [START phone_auth_callbacks]
    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                myCallback.verificationCompleted()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                // Invalid request
                val isInvalidRequest = e is FirebaseAuthInvalidCredentialsException
                val isSmsQuotaHasBeenExceeded = e is FirebaseTooManyRequestsException

                myCallback.verificationFailed(isInvalidRequest, isSmsQuotaHasBeenExceeded)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                // Save verification ID and resending token so we can use them later
                myCallback.onTokenSent()
            }
        }
    // [END phone_auth_callbacks]

    fun startPhoneNumberVerification(phoneNumber: String) {
        Log.d(TAG, "startPhoneNumberVerification")
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    fun verifyPhoneNumberWithCode(verificationId: String? = storedVerificationId, code: String) {
        // [START verify_with_code]
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            myCallback.signInFailed(isVerificationCodeInvalid = true, exception = e)
        }

        // [END verify_with_code]
    }

    // [START resend_verification]

    //TODO work wierd
    fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken? = resendToken
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    onSuccessfulSignIn(task)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    onFailedSignIn(task)
                }
            }
    }

    private fun onSuccessfulSignIn(task: Task<AuthResult>) {
        Log.d(TAG, "signInWithCredential:success")
        myCallback.signInSuccessful(task.result?.user)
    }

    private fun onFailedSignIn(task: Task<AuthResult>) {
        Log.w(TAG, "signInWithCredential:failure", task.exception)
        val isVerificationCodeInvalid =
            task.exception is FirebaseAuthInvalidCredentialsException
        myCallback.signInFailed(isVerificationCodeInvalid, task.exception)
    }

    // [END sign_in_with_phone]
}
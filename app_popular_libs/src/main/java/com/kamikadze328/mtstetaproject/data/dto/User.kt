package com.kamikadze328.mtstetaproject.data.dto

import android.net.Uri
import android.os.Parcelable
import android.telephony.PhoneNumberUtils
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid: String,
    val name: String,
    val password: String,
    val email: String,
    val phone: String,
    val photoUrl: Uri = Uri.EMPTY,
    val emailVerified: Boolean = false
) : Parcelable, Comparable<User> {
    override fun compareTo(other: User): Int {
        val idCompare = uid.compareTo(other.uid)
        return if (
            idCompare == 0 &&
            name.compareTo(other.name) == 0 &&
            password.compareTo(other.password) == 0 &&
            email.compareTo(other.email) == 0 &&
            PhoneNumberUtils.compare(phone, other.phone)
        ) 0
        else -1
    }
}
package com.kamikadze328.mtstetaproject.data.dto

import android.os.Parcelable
import android.telephony.PhoneNumberUtils
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var name: String,
    var password: String,
    var email: String,
    var phone: String
) : Parcelable, Comparable<User> {
    override fun compareTo(other: User): Int {
        val idCompare = id.compareTo(other.id)
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
package com.kamikadze328.mtstetaproject.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var name: String,
    var password: String,
    var email: String,
    var phone: String
) :
    Parcelable
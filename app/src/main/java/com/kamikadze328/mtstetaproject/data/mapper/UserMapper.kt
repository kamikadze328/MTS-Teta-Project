package com.kamikadze328.mtstetaproject.data.mapper

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.kamikadze328.mtstetaproject.data.dto.User

fun FirebaseUser.toUser() = User(
    id = uid,
    name = displayName ?: "Установите имя",
    photoUrl = photoUrl ?: Uri.EMPTY,
    password = "",
    phone = phoneNumber ?: "",
    email = email ?: "",
    emailVerified = isEmailVerified,
)

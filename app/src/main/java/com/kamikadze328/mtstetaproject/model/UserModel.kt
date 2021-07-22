package com.kamikadze328.mtstetaproject.model

import com.kamikadze328.mtstetaproject.data.features.users.UsersDataSource

class UserModel(
    private val usersDataSource: UsersDataSource
) {
    fun getCurrentUser() = usersDataSource.getCurrentUser()
}
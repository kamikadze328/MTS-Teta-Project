package com.kamikadze328.mtstetaproject.data.features.users

import com.kamikadze328.mtstetaproject.data.dto.User

interface UsersDataSource {
    fun getCurrentUser(): User
}
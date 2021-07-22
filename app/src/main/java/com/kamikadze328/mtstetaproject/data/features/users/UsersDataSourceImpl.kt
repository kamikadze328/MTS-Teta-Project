package com.kamikadze328.mtstetaproject.data.features.users

import com.kamikadze328.mtstetaproject.data.dto.User

class UsersDataSourceImpl : UsersDataSource {
    override fun getCurrentUser(): User {
        return User(
            id = 123,
            name = "Банан",
            password = "12345678",
            email = "bananbananovich@gmail.com",
            phone = "+79999999999"
        )
    }
}
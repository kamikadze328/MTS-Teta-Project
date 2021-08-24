package com.kamikadze328.mtstetaproject.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Actor(
    @PrimaryKey val actorId: Long,
    val avatarIcon: Int,
    val name: String
)

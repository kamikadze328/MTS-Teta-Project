package com.kamikadze328.mtstetaproject.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Actor(
    @PrimaryKey
    @ColumnInfo(name = "actorId")
    val id: Int,
    val adult: Boolean = false,
    val cast_id: Long = 0L,
    val character: String = "",
    val credit_id: String = "",
    val gender: Int? = null,
    val known_for_department: String = "",
    val name: String,
    val order: Int = 0,
    val original_name: String = "",
    val popularity: Double = .0,
    val profile_path: String? = null
) {
    @Ignore
    var local_profile_res_id: Int? = null
}

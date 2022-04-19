package com.kamikadze328.mtstetaproject.data.dto

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
@Stable
data class Genre(
    @PrimaryKey val genreId: Long,
    val name: String,
) : Parcelable {
    @Ignore
    @IgnoredOnParcel
    var isSelected: Boolean = false
}

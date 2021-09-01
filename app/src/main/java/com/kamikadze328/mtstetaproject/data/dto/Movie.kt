package com.kamikadze328.mtstetaproject.data.dto

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Movie(
    @PrimaryKey val movieId: Long,
    val title: String,
    val overview: String?,
    val release_date: String,
    val vote_average: Double,
    val age_restriction: String,
    var poster_path: String?,
    val isFavourite: Boolean = false,
    //@ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    //@ColumnInfo(defaultValue = "(datetime('now'))")
    //@ColumnInfo(defaultValue = "(strftime('%s','now'))")
    var updateTime: Long = System.currentTimeMillis()
) : Parcelable {
    @Ignore
    @IgnoredOnParcel
    var genres: List<Genre> = emptyList()

    @Ignore
    @IgnoredOnParcel
    var actors: List<Actor> = emptyList()

    companion object : Parceler<Movie> {
        override fun Movie.write(parcel: Parcel, flags: Int) {
            parcel.writeLong(movieId)
            parcel.writeString(title)
            parcel.writeString(overview)
            parcel.writeString(release_date)
            parcel.writeDouble(vote_average)
            parcel.writeString(age_restriction)
            parcel.writeString(poster_path)
            parcel.writeList(genres)
            parcel.writeList(actors)
        }

        override fun create(parcel: Parcel): Movie =
            Movie(
                movieId = parcel.readLong(),
                title = parcel.readString() ?: "",
                overview = parcel.readString(),
                release_date = parcel.readString() ?: "",
                vote_average = parcel.readDouble(),
                age_restriction = parcel.readString() ?: "",
                poster_path = parcel.readString(),
            ).apply {
                val genres: List<Genre> = mutableListOf()
                parcel.readList(genres, MutableList::class.java.classLoader)
                this.genres = genres

                val actors: List<Actor> = mutableListOf()
                parcel.readList(actors, MutableList::class.java.classLoader)
                this.actors = actors
            }
    }

}
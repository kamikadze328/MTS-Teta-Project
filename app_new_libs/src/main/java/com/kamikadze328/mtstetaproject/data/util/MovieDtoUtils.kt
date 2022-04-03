package com.kamikadze328.mtstetaproject.data.util

import com.kamikadze328.mtstetaproject.data.dto.Movie

fun Movie.isFully(): Boolean {
    return genres.isNotEmpty() && actors.isNotEmpty()
}

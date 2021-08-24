package com.kamikadze328.mtstetaproject.data.remote.dao

import com.kamikadze328.mtstetaproject.data.dto.Actor

data class CreditResponse(
    val cast: List<Actor>,
    val crew: List<Crew>,
    val id: Int
)
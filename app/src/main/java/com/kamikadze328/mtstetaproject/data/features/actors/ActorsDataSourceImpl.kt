package com.kamikadze328.mtstetaproject.data.features.actors

import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Actor


class ActorsDataSourceImpl : ActorsDataSource {
    override fun getActors(): List<Actor> = listOf(
        Actor(
            R.drawable.img_actor_statham,
            "Джейсон Стэйтем"
        ),
        Actor(
            R.drawable.img_actor_mccallany,
            "Холт Маккэллани"
        ),
        Actor(
            R.drawable.img_actor_hartnett,
            "Джош Харнетт"
        )
    )

}


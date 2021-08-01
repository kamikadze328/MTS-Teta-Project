package com.kamikadze328.mtstetaproject.adapter.moviedetailsactor

import androidx.recyclerview.widget.DiffUtil
import com.kamikadze328.mtstetaproject.data.dto.Actor

class ActorCallback : DiffUtil.ItemCallback<Actor>() {
    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}
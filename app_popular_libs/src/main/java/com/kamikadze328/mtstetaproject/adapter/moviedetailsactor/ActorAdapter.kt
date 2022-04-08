package com.kamikadze328.mtstetaproject.adapter.moviedetailsactor

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kamikadze328.mtstetaproject.data.dto.Actor

class ActorAdapter : ListAdapter<Actor, ActorViewHolder>(ActorCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size
}

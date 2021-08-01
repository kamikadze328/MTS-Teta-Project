package com.kamikadze328.mtstetaproject.adapter.moviedetailsactor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Actor

class ActorViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val iconAvatar: ImageView = view.findViewById(R.id.actor_image)
    private val textName: TextView = view.findViewById(R.id.actor_text)
    fun bind(actor: Actor) {
        val icon = ContextCompat.getDrawable(view.context, actor.avatarIcon)
        iconAvatar.setImageDrawable(icon)
        textName.text = actor.name
    }


    companion object {
        fun from(parent: ViewGroup): ActorViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_actor, parent, false)
            return ActorViewHolder(view)
        }
    }
}
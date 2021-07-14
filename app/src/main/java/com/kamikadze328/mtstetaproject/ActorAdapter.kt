package com.kamikadze328.mtstetaproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.data.dto.Actor


class ActorAdapter(
    private val actors: List<Actor>
) : RecyclerView.Adapter<ActorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_actor, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): Actor {
        return actors[position]
    }

    override fun getItemCount(): Int = actors.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconAvatar: ImageView = view.findViewById(R.id.actor_image)
        private val textName: TextView = view.findViewById(R.id.actor_text)
        fun bind(actor: Actor) {

            iconAvatar.setImageDrawable(actor.avatarIcon)
            textName.text = actor.name
        }
    }
}

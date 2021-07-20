package com.kamikadze328.mtstetaproject.adapter.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre

class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val root: TextView = view.findViewById(R.id.movie_genre)

    fun bind(data: Genre, click: (id: Int) -> Unit) {
        root.text = data.name
        root.setOnClickListener { click(data.id) }
    }

    companion object {
        fun from(parent: ViewGroup): GenreViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_genre, parent, false)
            return GenreViewHolder(view)
        }
    }
}
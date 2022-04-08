package com.kamikadze328.mtstetaproject.adapter.genre

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre

class GenreViewHolder(view: View) : GenreViewHolderSealed(view) {
    private val root: TextView = view.findViewById(R.id.genre_root)

    fun bind(data: Genre, click: (id: Long) -> Unit) {
        root.text = data.name
        itemView.isActivated = data.isSelected
        root.setTypeface(null, if (data.isSelected) Typeface.BOLD else Typeface.NORMAL)
        root.setOnClickListener { click(data.genreId) }
    }

    companion object {
        fun from(parent: ViewGroup): GenreViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_genre, parent, false)
            return GenreViewHolder(view)
        }
    }
}
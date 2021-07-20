package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kamikadze328.mtstetaproject.R

class HeaderViewHolder(view: View) : MovieViewHolderSealed(view) {
    private val titleView: TextView = view.findViewById(R.id.movie_list_header)
    fun bind(title: String) {
        titleView.text = title
    }

    companion object {
        fun from(parent: ViewGroup): HeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_movies_list_header, parent, false)
            return HeaderViewHolder(view)
        }
    }
}
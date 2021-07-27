package com.kamikadze328.mtstetaproject.adapter.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamikadze328.mtstetaproject.R

class GenreLoadingViewHolder(view: View) : GenreViewHolderSealed(view) {

    companion object {
        fun from(parent: ViewGroup): GenreLoadingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_genre, parent, false)
            return GenreLoadingViewHolder(view)
        }
    }
}
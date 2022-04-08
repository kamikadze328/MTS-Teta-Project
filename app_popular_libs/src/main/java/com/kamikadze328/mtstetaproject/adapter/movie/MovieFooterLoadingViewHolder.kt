package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamikadze328.mtstetaproject.R

class MovieFooterLoadingViewHolder(view: View) : MovieViewHolderSealed(view) {
    companion object {
        fun from(parent: ViewGroup): MovieFooterLoadingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view =
                layoutInflater.inflate(R.layout.item_movies_list_footer_loading, parent, false)
            return MovieFooterLoadingViewHolder(view)
        }
    }
}
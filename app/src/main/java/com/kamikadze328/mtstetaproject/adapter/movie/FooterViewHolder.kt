package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamikadze328.mtstetaproject.R

class FooterViewHolder(view: View) : MovieViewHolderSealed(view) {

    companion object {
        fun from(parent: ViewGroup): FooterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_movies_list_footer, parent, false)
            return FooterViewHolder(view)
        }
    }
}
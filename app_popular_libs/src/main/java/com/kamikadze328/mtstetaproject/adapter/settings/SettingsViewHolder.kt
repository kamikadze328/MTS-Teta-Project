package com.kamikadze328.mtstetaproject.adapter.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.R

class SettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.settings_item_name)
    private val root: ConstraintLayout = view.findViewById(R.id.settings_item_root)

    fun bind(name: String, click: (name: String) -> Unit) {
        textView.text = name
        root.setOnClickListener { click(name) }
    }

    companion object {
        fun from(parent: ViewGroup): SettingsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_setting_menu, parent, false)
            return SettingsViewHolder(view)
        }
    }
}
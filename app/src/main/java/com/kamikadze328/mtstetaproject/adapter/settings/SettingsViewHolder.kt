package com.kamikadze328.mtstetaproject.adapter.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.R

class SettingsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.settings_item_name)
    private val root: ConstraintLayout = view.findViewById(R.id.settings_item_root)

    fun bind(nameId: Int, click: (nameId: Int) -> Unit) {

        textView.text = view.context.resources.getString(nameId)
        root.setOnClickListener { click(nameId) }
    }

    companion object {
        fun from(parent: ViewGroup): SettingsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_setting_menu, parent, false)
            return SettingsViewHolder(view)
        }
    }
}
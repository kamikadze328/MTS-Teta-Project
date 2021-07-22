package com.kamikadze328.mtstetaproject.adapter.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SettingsAdapter(
    private val dataSet: Array<Int>,
    private val clickOnItem: (nameId: Int) -> Unit,
) :
    RecyclerView.Adapter<SettingsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        return SettingsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(dataSet[position], clickOnItem)
    }

    override fun getItemCount(): Int = dataSet.size

}
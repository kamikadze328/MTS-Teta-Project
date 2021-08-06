package com.kamikadze328.mtstetaproject.data.dto

data class Genre(val name: String, val id: Int, var isSelected: Boolean = false) :
    Comparable<Genre> {
    override fun compareTo(other: Genre): Int {
        return if (isSelected != other.isSelected)
            other.isSelected.compareTo(isSelected)
        else name.compareTo(other.name)
    }

}
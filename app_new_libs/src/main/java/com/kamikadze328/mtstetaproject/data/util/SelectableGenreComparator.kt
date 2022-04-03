package com.kamikadze328.mtstetaproject.data.util

import com.kamikadze328.mtstetaproject.data.dto.Genre

class SelectableGenreComparator : Comparator<Genre> {

    override fun compare(o1: Genre?, o2: Genre?): Int {
        if (o1 == null && o2 == null) return 0
        if (o1 == null) return -1
        if (o2 == null) return 1
        return if (o1.isSelected != o2.isSelected)
            o2.isSelected.compareTo(o1.isSelected)
        else o1.name.compareTo(o2.name)
    }
}
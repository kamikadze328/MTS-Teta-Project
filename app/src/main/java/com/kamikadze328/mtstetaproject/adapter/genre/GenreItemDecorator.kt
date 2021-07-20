package com.kamikadze328.mtstetaproject.adapter.genre

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class GenreItemDecorator(@Px private val offset: Int, @Px private val firstOffset: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        val itemCount = state.itemCount

        when (itemPosition) {
            0 -> {
                outRect.left = firstOffset
            }
            itemCount - 1 -> {
                outRect.right = firstOffset
                outRect.left = offset
            }
            else -> {
                outRect.left = offset
            }
        }

    }
}
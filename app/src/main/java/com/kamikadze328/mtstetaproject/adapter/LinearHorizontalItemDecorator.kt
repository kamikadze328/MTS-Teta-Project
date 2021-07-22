package com.kamikadze328.mtstetaproject.adapter

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView


class LinearHorizontalItemDecorator(
    @Px private val offset: Int,
    @Px private val offsetFirst: Int,
    @Px private val offsetLast: Int
) : RecyclerView.ItemDecoration() {
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
                outRect.left = offsetFirst
                outRect.right = offset / 2
            }
            itemCount - 1 -> {
                outRect.left = offset / 2
                outRect.right = offsetLast
            }
            else -> {
                outRect.left = offset / 2
                outRect.right = offset / 2
            }
        }
    }
}
package com.kamikadze328.mtstetaproject.adapter.movie

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView


class MovieItemDecoration(
    @Px private val offsetBetween: Int,
    @Px private val offsetBottom: Int,
    private val spanCount: Int
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
        val isHeader = /*itemPosition == 0*/ false
        val isFooter = itemPosition == itemCount - 1

        when {
            isFooter || isHeader -> {
                outRect.bottom = offsetBetween
                outRect.left = offsetBetween
                outRect.right = offsetBetween
            }
            itemPosition % spanCount == /*1*/ 0 -> {
                outRect.left = offsetBetween
                outRect.right = offsetBetween / 2
                outRect.bottom = offsetBottom
            }
            itemPosition % spanCount == /*0*/ 1 -> {
                outRect.left = offsetBetween / 2
                outRect.right = offsetBetween
                outRect.bottom = offsetBottom
            }
            else -> {
                outRect.left = offsetBetween / 2
                outRect.right = offsetBetween / 2
                outRect.bottom = offsetBottom
            }
        }
    }
}
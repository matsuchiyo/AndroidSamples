package com.example.overlappingrecyclerviewsample

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OverlappingItemDecoration(
    private val context: Context,
    private val marginTopInDp: Float = 24f,
    private val marginBottomInDp: Float = 24f,
    private val offsetYFromPreviousItemInDp: Float
): RecyclerView.ItemDecoration() {

    var marginTopInPx = 0f
    var marginBottomInPx = 0f
    var offsetYFromPreviousItemInPx = 0f

    init {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        marginTopInPx = marginTopInDp * metrics.density
        marginBottomInPx = marginBottomInDp * metrics.density
        offsetYFromPreviousItemInPx = offsetYFromPreviousItemInDp * metrics.density
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.top = marginTopInPx.toInt()

        } else {
            val itemHeight: Float = parent.getChildAt(0)?.height?.toFloat() ?: 0f
            outRect.top = (offsetYFromPreviousItemInPx - itemHeight).toInt()

            if (position == (parent.adapter?.itemCount ?: 0) - 1) {
                outRect.bottom = marginBottomInPx.toInt()
            }
        }
    }
}
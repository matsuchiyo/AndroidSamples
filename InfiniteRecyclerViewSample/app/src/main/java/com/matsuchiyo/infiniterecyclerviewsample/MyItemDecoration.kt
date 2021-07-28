package com.matsuchiyo.infiniterecyclerviewsample

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MyItemDecoration(
    private var context: Context,
    private val spaceInDp: Int,
    private val startMarginInDp: Int,
    private val endMarginInDp: Int
): RecyclerView.ItemDecoration() {

    private var spaceInPx: Int = 0
    private var startMarginInPx: Int = 0
    private var endMarginInPx: Int = 0

    init {
        val displayMetrics = context.resources.displayMetrics
        spaceInPx = (spaceInDp * displayMetrics.density).toInt()
        startMarginInPx = (startMarginInDp * displayMetrics.density).toInt()
        endMarginInPx = (endMarginInDp * displayMetrics.density).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val count = parent.adapter?.itemCount ?: 0
//        Log.i("AAA", "*** parent.width ${parent.width}")

        if (position == 0) {
            outRect.left = startMarginInPx

        } else {
            outRect.left = spaceInPx

            if (position == count - 1) {
                outRect.right = endMarginInPx
            }
        }
    }
}
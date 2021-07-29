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

    private val density = context.resources.displayMetrics.density

    init {
        spaceInPx = (spaceInDp * density).toInt()
        startMarginInPx = (startMarginInDp * density).toInt()
        endMarginInPx = (endMarginInDp * density).toInt()
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

        outRect.left = (density * 4).toInt()
        outRect.right = (density * 4).toInt()

        if (position == 0) {
            outRect.left = (density * 16).toInt()
        }

        if (position == count - 1) {
            outRect.right = (density * 16).toInt()
        }
    }
}
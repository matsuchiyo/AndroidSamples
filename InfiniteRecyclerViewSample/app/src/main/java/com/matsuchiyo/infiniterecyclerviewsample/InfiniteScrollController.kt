package com.matsuchiyo.infiniterecyclerviewsample

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollController(private val context: Context): RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        Log.d("AAA", "*** newState2: $newState")
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

            val adapter = recyclerView.adapter as? ItemAdapter ?: return
            val realItemCount = adapter.getRealItemCount()
            val loopCount = ItemAdapter.LOOP_COUNT_FOR_INFINITE_SCROLL
            val totalCount = realItemCount * loopCount

            val lowerThreshold = totalCount * 0.1
            val upperThreshold = totalCount * 0.9

            val isPositionSmaller = firstVisibleItemPosition < lowerThreshold
            val isPositionLarger = firstVisibleItemPosition > upperThreshold
            if (isPositionSmaller || isPositionLarger) {
                val itemView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition)
                val itemViewWidth = itemView?.width ?: return
                val multiplier = (realItemCount * (loopCount * 0.4).toInt())
                Log.d("AAA", "*** itemWidth: $itemViewWidth, multiplier: $multiplier")
                val dX = itemViewWidth * multiplier
                if (isPositionSmaller) {
                    recyclerView.scrollBy(dX, 0)
                } else if (isPositionLarger) {
                    recyclerView.scrollBy(-dX, 0)
                }
            }
        }
    }
}
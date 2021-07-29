package com.matsuchiyo.infiniterecyclerviewsample

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

// https://stackoverflow.com/questions/38416146/recyclerview-smoothscroll-to-position-in-the-center-android
class MyLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        Log.d("AAA", "*** MyLinearLayoutManager.smoothScrollToPosition")
        startSmoothScroll(MyLinearSmoothScroller(recyclerView!!.context).also {
            it.targetPosition = position
        })
    }
}

class MyLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
    }
}
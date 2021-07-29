package com.matsuchiyo.infiniterecyclerviewsample

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

// スクロール完了時に、最も近くのアイテムの全体が表示されるようにするクラスです。
// MyLinearLayoutManagerと組み合わせると、最も近くのアイテムが中央に表示されるようになります。
class CardScrollController(private val context: Context): RecyclerView.OnScrollListener() {
    var currentState = -1
    var isScrollForcibly = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        Log.d("AAA", "*** newState: $newState")
        val oldState = currentState
        currentState = newState

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            isScrollForcibly = false
        }

        // 弾かずにドラッグを止めた場合も、近くのアイテムが中央に来るようにスクロール。
        if (oldState == RecyclerView.SCROLL_STATE_DRAGGING && newState == RecyclerView.SCROLL_STATE_IDLE) {
            forceToScroll(recyclerView)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        Log.d("AAA", "*** dx: $dx")

        if (isScrollForcibly) {
            return
        }

        if (currentState != RecyclerView.SCROLL_STATE_SETTLING) {
            return
        }

        // ドラッグ後弾いた勢いでスクロールしているときに、スピードが落ちてきたら、近くのアイテムが中央に来るようにスクロール。
        if (dx > -5 && dx < 5) {
            forceToScroll(recyclerView)
        }
    }

    private fun forceToScroll(recyclerView: RecyclerView) {
//        Log.d("AAA", "*** first: ${(recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()}")
//        Log.d("AAA", "*** last: ${(recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()}")
        /*
        val offsetX = recyclerView.computeHorizontalScrollOffset()
        val cardWidth = recyclerView.height * (343f / 216f)
        val density = context.resources.displayMetrics.density
        Log.d("AAA", "*** density: $density")
        val space = 8 * density
        val startMargin = 12 * density

        val newPosition = ((offsetX - startMargin) / (space + cardWidth)).roundToInt()
         */

        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val firstVisibleView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition) ?: return

        var firstVisibleViewLocation = intArrayOf(0, 0)
        firstVisibleView.getLocationInWindow(firstVisibleViewLocation)

        val recyclerViewLocationX = 0

        val firstVisibleViewOffsetX = recyclerViewLocationX - firstVisibleViewLocation[0]
        val isFirstAlmostVisible = firstVisibleViewOffsetX < (firstVisibleView.width / 2)

        val newPosition = if (isFirstAlmostVisible) firstVisibleItemPosition else firstVisibleItemPosition + 1

//        val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
//        val newPosition = (firstVisibleItemPosition + lastVisibleItemPosition) / 2

        Log.d("AAA", "*** forceToScroll newPosition: $newPosition")
        recyclerView.smoothScrollToPosition(newPosition)

        isScrollForcibly = true
    }
}
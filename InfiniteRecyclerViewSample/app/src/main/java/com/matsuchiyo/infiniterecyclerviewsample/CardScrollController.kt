package com.matsuchiyo.infiniterecyclerviewsample

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

// スクロール完了時に、最も近くのアイテムの全体が表示されるようにするクラスです。
// MyLinearLayoutManagerと組み合わせると、最も近くのアイテムが中央に表示されるようになります。
class CardScrollController(val context: Context): RecyclerView.OnScrollListener() {
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
        val offsetX = recyclerView.computeHorizontalScrollOffset()
        val cardWidth = recyclerView.height * (343f / 216f)
        val space = 8 * context.resources.displayMetrics.density
        val startMargin = space

        val newPosition = ((offsetX - startMargin) / (space + cardWidth)).roundToInt()

        recyclerView.smoothScrollToPosition(newPosition)

        isScrollForcibly = true
    }
}
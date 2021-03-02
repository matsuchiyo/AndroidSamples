package com.example.carousel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import java.util.*
import kotlin.math.roundToInt


class CarouselView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    companion object {
        const val AUTO_SCROLL_DURATION_MILLIS: Long = 3000
        const val CORNER_RADIUS_DP: Float = 40f
        const val PAGE_CONTROL_DOT_SIZE_DP: Int = 4
    }

    private val clipOutlineProvider = @SuppressLint("NewApi") object : ViewOutlineProvider() {
        @SuppressLint("NewApi")
        override fun getOutline(view: View, outline: Outline) {
            val cornerRadiusPx = CORNER_RADIUS_DP * context.resources.displayMetrics.density
            outline.setRoundRect(0, 0, view.width, view.height, cornerRadiusPx)
        }
    }

    private lateinit var viewPager: ViewPager2
    private var adapter: MyAdapter? = null

    private lateinit var pageControlLinearLayout: LinearLayout

    var images: List<Int> = listOf()
    set(value) {
        setItemsToAdapter(value)
        createPageControl(value.size)

        if (images.size > 1) {
            viewPager.setCurrentItem(1, false) // 2ページ目から始める。1ページ目には、無限スクロールのため最後の要素が設定されているため。
        }
        
        field = value
    }

    private var timer: Timer? = null

    init {
        initializeViews()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            View.VISIBLE -> {
                clearTimer()
                timer = Timer()
                timer?.schedule(object: TimerTask() {
                    override fun run() {
                        scrollToNextPage()
                    }
                }, AUTO_SCROLL_DURATION_MILLIS, AUTO_SCROLL_DURATION_MILLIS)
            }
            View.INVISIBLE, View.GONE -> {
                clearTimer()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun initializeViews() {
        viewPager = ViewPager2(context).also {
            it.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            it.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            it.outlineProvider = clipOutlineProvider
            it.clipToOutline = true
            addView(it)
        }

        adapter = MyAdapter(context, images)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                val realIndex = (viewPager.currentItem - 1 + images.size) % images.size
                updatePageControl(realIndex)

                if (state == ViewPager2.SCROLL_STATE_IDLE && images.size > 1) {
                    val newFakeIndex = realIndex + 1
                    viewPager.setCurrentItem(newFakeIndex, false)
                }
            }
        })

        pageControlLinearLayout = LinearLayout(context).also {
            it.orientation = LinearLayout.HORIZONTAL
            it.setHorizontalGravity(Gravity.CENTER)
            val pageControlDotSizeInPx = (PAGE_CONTROL_DOT_SIZE_DP * context.resources.displayMetrics.density).roundToInt()
            it.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, pageControlDotSizeInPx, Gravity.BOTTOM).also { params ->
                params.bottomMargin = (8 * context.resources.displayMetrics.density).roundToInt()
            }
            addView(it)
        }
    }

    private fun setItemsToAdapter(images: List<Int>) {
        adapter?.items = images
        adapter?.notifyDataSetChanged()
    }

    private fun scrollToNextPage() {
        val itemCount = adapter?.itemCount ?: return
        if (itemCount > 1) {
            val nextIndex = (viewPager.currentItem + 1) % itemCount
            viewPager.setCurrentItem(nextIndex, true)
        }
    }

    private fun clearTimer() {
        timer?.cancel()
        timer = null
    }

    private fun createPageControl(count: Int) {
        pageControlLinearLayout.removeAllViews()

        if (count < 2 || count > 10) {
            return
        }

        for (i in 0 until count) {
            pageControlLinearLayout.addView(View(context).also {
                it.setBackgroundResource(R.drawable.background_page_control_dot)
                it.alpha = if (i == 0) 1.0f else 0.5f

                val density = context.resources.displayMetrics.density
                val dotSizeDp = (PAGE_CONTROL_DOT_SIZE_DP * density).roundToInt()
                it.layoutParams = LayoutParams(dotSizeDp, dotSizeDp).also { params ->
                    if (i > 0) {
                        params.leftMargin = (4 * density).roundToInt()
                    }
                }
            })
        }
    }

    private fun updatePageControl(index: Int) {
        pageControlLinearLayout.children.forEachIndexed { i, view ->
            view.alpha = if (i == index) 1.0f else 0.5f
        }
    }

}
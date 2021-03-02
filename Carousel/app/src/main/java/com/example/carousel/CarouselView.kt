package com.example.carousel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2


class CarouselView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    private val clipOutlineProvider = @SuppressLint("NewApi") object : ViewOutlineProvider() {
        @SuppressLint("NewApi")
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 40f)
        }
    }

    private val viewPager: ViewPager2 = ViewPager2(context)
    private var adapter: MyAdapter? = null

    var images: List<Int> = listOf()
    set(value) {
        Log.i("BBB", "*** set images: $value")
        configureViewPager(value)
        field = value
    }

    init {
        initializeViews()
    }

    @SuppressLint("NewApi")
    private fun initializeViews() {
        Log.i("BBB", "*** initializeViews")
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        viewPager.outlineProvider = clipOutlineProvider
        viewPager.clipToOutline = true
        addView(viewPager)
    }


    private fun configureViewPager(images: List<Int>) {
        Log.i("BBB", "*** configureViewPager")
//        val items = listOf(R.drawable.baseline_add_24, R.drawable.baseline_clear_24, R.drawable.baseline_keyboard_arrow_left_24)
        adapter = MyAdapter(context, images)
        viewPager.adapter = adapter
        if (images.size > 1) {
            viewPager.setCurrentItem(1, false) // 2ページ目から始める。1ページ目には、無限スクロールのため最後の要素が設定されているため。
        }

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    val realIndex = (viewPager.currentItem - 1 + images.size) % images.size
                    val newFakeIndex = realIndex + 1
                    viewPager.setCurrentItem(newFakeIndex, false)
                }
            }
        })
    }
}
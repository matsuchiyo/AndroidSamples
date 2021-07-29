package com.matsuchiyo.infiniterecyclerviewsample

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureAnimatedTransition()

        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = MyLinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        recyclerView.addItemDecoration(MyItemDecoration(this, 8, 16, 16))

        val adapter = ItemAdapter()
        recyclerView.adapter = adapter

        viewModel.items.observe(this, Observer {
            Log.d("AAA", "*** viewMOdel.items.observe")
            adapter.items = it
            adapter.notifyDataSetChanged()

            // いまいちだけど、このタイミングだと、recyclerViewのサイズが取得できないため。
            val recyclerViewWidth = windowManager.currentWindowMetrics.bounds.width()
            val recyclerViewHeight = recyclerViewWidth * (216 / 375f)
            val itemWidth = recyclerViewHeight * (343 / 216f)
            val itemLeftSpaceWidth = 4 * resources.displayMetrics.density
            val offset = (recyclerViewWidth - itemWidth) / 2f - itemLeftSpaceWidth

            val realFirstPosition = 0
            val firstPosition = realFirstPosition + adapter.getRealItemCount() * (ItemAdapter.LOOP_COUNT_FOR_INFINITE_SCROLL * 0.5).toInt()
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(firstPosition, offset.toInt())
        })

        recyclerView.addOnScrollListener(CardScrollController(this))
        recyclerView.addOnScrollListener(InfiniteScrollController(this))

        // https://developer.android.com/training/transitions/start-activity?hl=ja
        adapter.setOnItemClickListener { item, view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val intent = Intent(this, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.ITEM, item)
                }
                val options = ActivityOptions.makeSceneTransitionAnimation(this, view, "card")
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun configureAnimatedTransition() {
        // https://developer.android.com/training/transitions/start-activity?hl=ja
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                exitTransition = Fade()
                sharedElementExitTransition = ChangeBounds()
            }
        }
    }
}
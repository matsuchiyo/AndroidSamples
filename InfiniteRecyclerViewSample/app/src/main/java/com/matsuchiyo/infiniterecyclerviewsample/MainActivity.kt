package com.matsuchiyo.infiniterecyclerviewsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val offset = 8 * resources.displayMetrics.density
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(3, offset.toInt())
        })

        var state = 0

        var isScrolling = false

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("AAA", "*** newState: $newState")
                state = newState

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrolling = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d("AAA", "*** dx: $dx")

                if (!isScrolling && state == RecyclerView.SCROLL_STATE_SETTLING && (dx > -5 && dx < 5)) {
                    Log.d("AAA", "*** recyclerView.height: ${recyclerView.height}, ${recyclerView.scrollX}")

                    val offsetX = recyclerView.computeHorizontalScrollOffset()
                    val cardWidth = recyclerView.height * (343f / 216f)
                    val space = 8 * resources.displayMetrics.density
                    val startMargin = space

                    val newPosition = ((offsetX - startMargin) / (space + cardWidth)).roundToInt()

                    recyclerView.smoothScrollToPosition(newPosition)

                    isScrolling = true
                }
            }
        })
    }
}
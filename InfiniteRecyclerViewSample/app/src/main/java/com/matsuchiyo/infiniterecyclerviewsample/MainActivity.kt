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
        recyclerView.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        recyclerView.addItemDecoration(MyItemDecoration(this, 8, 16, 16))

        val adapter = ItemAdapter()
        recyclerView.adapter = adapter

        viewModel.items.observe(this, Observer {
            Log.i("AAA", "*** viewMOdel.items.observe")
            adapter.items = it
            adapter.notifyDataSetChanged()
//            recyclerView.scrollToPosition(3)
//            recyclerView.scrollX -= (8 * resources.displayMetrics.density).toInt()
//            recyclerView.scrollTo(recyclerView.scrollX - (8 * resources.displayMetrics.density).toInt(), recyclerView.scrollY)
//            recyclerView.scrollTo(-100, 100)
            val offset = 8 * resources.displayMetrics.density
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(3, offset.toInt())
        })

        var state = 0

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.i("AAA", "*** newState: $newState")
                state = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
                Log.i("AAA", "*** dx: $dx")

                if (state == RecyclerView.SCROLL_STATE_SETTLING && dx < 5) {
                        Log.i("AAA", "*** recyclerView.height: ${recyclerView.height}, ${recyclerView.scrollX}")
                    val newPosition = ((recyclerView.scrollX - 8f) / (8 * resources.displayMetrics.density + recyclerView.height * 343f / 216f)).roundToInt()

                    val offset = 8 * resources.displayMetrics.density
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(newPosition, offset.toInt())
                }
            }
        })
    }
}